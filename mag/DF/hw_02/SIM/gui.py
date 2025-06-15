import sys
import serial.tools.list_ports
from PyQt6.QtWidgets import (
    QApplication,
    QMainWindow,
    QWidget,
    QVBoxLayout,
    QHBoxLayout,
    QGroupBox,
    QLabel,
    QComboBox,
    QLineEdit,
    QPushButton,
    QTextEdit,
    QTableWidget,
    QTableWidgetItem,
    QHeaderView,
    QMessageBox,
    QStatusBar,
)
from PyQt6.QtCore import Qt, QThread, pyqtSignal
from reader.simReader import (
    SIMReader,
    SIMError,
)


class SIMWorker(QThread):
    """Worker thread for SIM operations to prevent GUI freezing"""

    log_signal = pyqtSignal(str)
    sms_signal = pyqtSignal(list)
    status_signal = pyqtSignal(str)
    error_signal = pyqtSignal(str)

    def __init__(self, port, baudrate, timeout, pin):
        super().__init__()
        self.port = port
        self.baudrate = baudrate
        self.timeout = timeout
        self.pin = pin
        self.sim = None

    def run(self):
        """Main thread execution"""
        try:
            self.log_signal.emit(f"Connecting to {self.port}...")
            self.sim = SIMReader(
                port=self.port,
                baudrate=self.baudrate,
                timeout=self.timeout,
                pin=self.pin,
            )
            self.status_signal.emit(f"Connected to {self.port}")

            # Handle PIN if required
            if self.sim.requires_pin:
                if self.pin:
                    if self.sim.enter_pin(self.pin):
                        self.log_signal.emit("SIM unlocked successfully")
                    else:
                        self.error_signal.emit("PIN entry failed")
                        return
                else:
                    self.error_signal.emit("SIM requires PIN but none provided")
                    return

            # Read SMS messages
            self.log_signal.emit("Reading SMS messages...")
            messages = self.sim.read_sms()
            self.sms_signal.emit(messages)
            self.log_signal.emit(f"Found {len(messages)} messages")

        except SIMError as e:
            self.error_signal.emit(f"SIM Error: {str(e)}")
        except Exception as e:
            self.error_signal.emit(f"Unexpected error: {str(e)}")
        finally:
            if self.sim:
                self.sim.serial_conn.close()
                self.log_signal.emit("Connection closed")


class SIMReaderGUI(QMainWindow):
    """Main application window for SIM Reader"""

    def __init__(self):
        super().__init__()
        self.setWindowTitle("SIM Card Reader")
        self.setGeometry(100, 100, 800, 600)

        # Central widget and main layout
        central_widget = QWidget()
        self.setCentralWidget(central_widget)
        main_layout = QVBoxLayout(central_widget)

        # Connection settings group
        connection_group = QGroupBox("Connection Settings")
        connection_layout = QVBoxLayout()

        # Port selection
        port_layout = QHBoxLayout()
        port_layout.addWidget(QLabel("Serial Port:"))
        self.port_combo = QComboBox()
        self.port_combo.setEditable(True)
        port_layout.addWidget(self.port_combo)
        self.refresh_btn = QPushButton("Refresh")
        self.refresh_btn.clicked.connect(self.refresh_ports)
        port_layout.addWidget(self.refresh_btn)
        connection_layout.addLayout(port_layout)

        # Settings row
        settings_layout = QHBoxLayout()
        settings_layout.addWidget(QLabel("Baudrate:"))
        self.baudrate_edit = QLineEdit("9600")
        settings_layout.addWidget(self.baudrate_edit)

        settings_layout.addWidget(QLabel("Timeout (s):"))
        self.timeout_edit = QLineEdit("2")
        settings_layout.addWidget(self.timeout_edit)

        settings_layout.addWidget(QLabel("PIN:"))
        self.pin_edit = QLineEdit()
        self.pin_edit.setEchoMode(QLineEdit.EchoMode.Password)
        self.pin_edit.setMaxLength(8)
        settings_layout.addWidget(self.pin_edit)
        connection_layout.addLayout(settings_layout)

        # Connect button
        self.connect_btn = QPushButton("Connect and Read SMS")
        self.connect_btn.clicked.connect(self.start_reading)
        connection_layout.addWidget(self.connect_btn)

        connection_group.setLayout(connection_layout)
        main_layout.addWidget(connection_group)

        # SMS display table
        self.sms_table = QTableWidget()
        self.sms_table.setColumnCount(4)
        self.sms_table.setHorizontalHeaderLabels(
            ["ID", "Sender", "Timestamp", "Preview"]
        )
        self.sms_table.horizontalHeader().setSectionResizeMode(
            3, QHeaderView.ResizeMode.Stretch
        )
        self.sms_table.setEditTriggers(QTableWidget.EditTrigger.NoEditTriggers)
        self.sms_table.setSelectionBehavior(QTableWidget.SelectionBehavior.SelectRows)
        self.sms_table.doubleClicked.connect(self.show_full_message)
        main_layout.addWidget(QLabel("SMS Messages:"))
        main_layout.addWidget(self.sms_table)

        # Log display
        log_group = QGroupBox("Log")
        log_layout = QVBoxLayout()
        self.log_display = QTextEdit()
        self.log_display.setReadOnly(True)
        log_layout.addWidget(self.log_display)
        log_group.setLayout(log_layout)
        main_layout.addWidget(log_group)

        # Refresh ports after log_display is initialized
        self.refresh_ports()

        # Status bar
        self.status_bar = QStatusBar()
        self.setStatusBar(self.status_bar)

        # Thread reference
        self.worker_thread = None

    def refresh_ports(self):
        """Refresh available serial ports"""
        self.port_combo.clear()
        ports = serial.tools.list_ports.comports()
        for port in ports:
            self.port_combo.addItem(port.device, port.device)
        if not ports:
            self.port_combo.addItem("No ports found", "")
        self.log("Available ports refreshed")

    def log(self, message):
        """Add message to log display"""
        self.log_display.append(message)
        self.statusBar().showMessage(message)

    def start_reading(self):
        """Start SIM reading process in a worker thread"""
        if self.worker_thread and self.worker_thread.isRunning():
            QMessageBox.warning(
                self, "Operation in Progress", "Already reading SIM. Please wait."
            )
            return

        # Get input values
        port = self.port_combo.currentText()
        try:
            baudrate = int(self.baudrate_edit.text())
            timeout = int(self.timeout_edit.text())
        except ValueError:
            QMessageBox.critical(
                self, "Invalid Input", "Baudrate and timeout must be integers"
            )
            return

        pin = self.pin_edit.text() or None

        # Validate inputs
        if not port:
            QMessageBox.critical(
                self, "Missing Port", "Please select a valid serial port"
            )
            return

        # Clear previous results
        self.sms_table.setRowCount(0)
        self.log("Starting SIM read operation...")

        # Create and start worker thread
        self.worker_thread = SIMWorker(port, baudrate, timeout, pin)
        self.worker_thread.log_signal.connect(self.log)
        self.worker_thread.sms_signal.connect(self.display_sms)
        self.worker_thread.status_signal.connect(self.log)
        self.worker_thread.error_signal.connect(self.handle_error)
        self.worker_thread.finished.connect(self.on_thread_finish)
        self.worker_thread.start()

        # Disable UI during operation
        self.set_ui_enabled(False)

    def display_sms(self, messages):
        """Display SMS messages in the table"""
        self.sms_table.setRowCount(len(messages))
        for row, msg in enumerate(messages):
            self.sms_table.setItem(row, 0, QTableWidgetItem(msg["index"]))
            self.sms_table.setItem(row, 1, QTableWidgetItem(msg["sender"]))
            self.sms_table.setItem(row, 2, QTableWidgetItem(msg["timestamp"]))

            # Create preview (first 40 characters)
            preview = msg["body"][:40] + ("..." if len(msg["body"]) > 40 else "")
            preview_item = QTableWidgetItem(preview)
            preview_item.setData(
                Qt.ItemDataRole.UserRole, msg["body"]
            )  # Store full message
            self.sms_table.setItem(row, 3, preview_item)

    def show_full_message(self, index):
        """Show full SMS message when row is double-clicked"""
        row = index.row()
        full_message = self.sms_table.item(row, 3).data(Qt.ItemDataRole.UserRole)
        sender = self.sms_table.item(row, 1).text()

        msg_box = QMessageBox(self)
        msg_box.setWindowTitle(f"Message from {sender}")
        msg_box.setText(full_message)
        msg_box.exec()

    def handle_error(self, error_message):
        """Display error messages"""
        QMessageBox.critical(self, "Error", error_message)
        self.log(f"Error: {error_message}")

    def on_thread_finish(self):
        """Clean up after thread completes"""
        self.set_ui_enabled(True)
        self.log("Operation completed")

    def set_ui_enabled(self, enabled):
        """Enable/disable UI controls during operations"""
        self.port_combo.setEnabled(enabled)
        self.refresh_btn.setEnabled(enabled)
        self.baudrate_edit.setEnabled(enabled)
        self.timeout_edit.setEnabled(enabled)
        self.pin_edit.setEnabled(enabled)
        self.connect_btn.setEnabled(enabled)

    def closeEvent(self, event):
        """Ensure thread is stopped when closing window"""
        if self.worker_thread and self.worker_thread.isRunning():
            self.worker_thread.terminate()
            self.worker_thread.wait()
        event.accept()


if __name__ == "__main__":
    app = QApplication(sys.argv)
    window = SIMReaderGUI()
    window.show()
    sys.exit(app.exec())
