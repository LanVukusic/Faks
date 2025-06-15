import os
import pty
import time
import logging
import random
import threading
from datetime import datetime, timedelta
from typing import Dict, List, Optional


class AdvancedSIMEmulator:
    """Realistic virtual SIM card with comprehensive serial interface."""

    def __init__(self, pin: str = "1234"):
        self.pin = pin
        self.locked = True
        self.attempts = 3
        self._setup_logging()
        self._create_virtual_port()
        self._init_databases()
        self._command_buffer = ""
        self._init_network_params()
        threading.Thread(target=self._simulate_background_events, daemon=True).start()

    def _setup_logging(self):
        self.logger = logging.getLogger("AdvancedSIMEmulator")
        self.logger.setLevel(logging.INFO)
        handler = logging.StreamHandler()
        handler.setFormatter(
            logging.Formatter("%(asctime)s - %(name)s - %(levelname)s - %(message)s")
        )
        self.logger.addHandler(handler)

    def _create_virtual_port(self):
        """Create virtual serial port pair."""
        self.master_fd, slave_fd = pty.openpty()
        self.slave_name = os.ttyname(slave_fd)
        self.logger.info(f"Virtual SIM created at {self.slave_name}")

    def _init_databases(self):
        """Initialize phone databases with realistic dummy data."""
        self.phonebook = [
            {"index": 1, "name": "Emergency", "number": "112", "type": 145},
            {"index": 2, "name": "Voicemail", "number": "+386123456", "type": 129},
            {"index": 3, "name": "Anna", "number": "+38664123456", "type": 129},
            {"index": 4, "name": "Marko", "number": "+38665123456", "type": 129},
            {"index": 5, "name": "Service", "number": "+18005551234", "type": 145},
        ]

        self.sms_messages = [
            {
                "id": 1,
                "sender": "Vodafone",
                "text": "Welcome to Vodafone! Your balance is 15.23 EUR",
                "status": "REC UNREAD",
                "timestamp": datetime.now() - timedelta(minutes=15),
            },
            {
                "id": 2,
                "sender": "+38640123456",
                "text": "Meeting moved to 14:30 tomorrow",
                "status": "REC READ",
                "timestamp": datetime.now() - timedelta(hours=2),
            },
            {
                "id": 3,
                "sender": "Twitter",
                "text": "Login code: 7B9K2. Do not share this code.",
                "status": "REC UNREAD",
                "timestamp": datetime.now() - timedelta(minutes=5),
            },
        ]

    def _init_network_params(self):
        """Initialize network parameters with realistic values."""
        self.imsi = "293410123456789"
        self.iccid = "89445001001234567890"
        self.network = "Vodafone SI"
        self.signal_strength = random.randint(12, 31)
        self.battery_level = random.randint(65, 100)

    def _process_command(self, cmd: str) -> str:
        """Process AT commands with enhanced SIM logic."""
        cmd = cmd.strip().upper()
        self.logger.debug(f"CMD: {cmd}")

        # Basic commands
        if cmd == "AT":
            return "OK\r\n"
        elif cmd == "AT+CPIN?":
            return "+CPIN: SIM PIN\r\n" if self.locked else "+CPIN: READY\r\n"
        elif cmd.startswith("AT+CPIN="):
            return self._handle_pin(cmd)
        elif not self.locked and cmd == 'AT+CMGL="ALL"':
            return self._generate_sms_response()
        elif not self.locked and cmd.startswith("AT+CMGR="):
            return self._read_sms(cmd)
        elif not self.locked and cmd.startswith("AT+CMGD="):
            return self._delete_sms(cmd)

        # Phonebook commands
        elif cmd == "AT+CPBS?":
            return '+CPBS: "SM",0,250\r\nOK\r\n'
        elif cmd.startswith("AT+CPBR="):
            return self._read_phonebook(cmd)

        # Network commands
        elif cmd == "AT+CIMI":
            return f"{self.imsi}\r\nOK\r\n"
        elif cmd == "AT+CCID":
            return f'+CCID: "{self.iccid}"\r\nOK\r\n'
        elif cmd == "AT+COPS?":
            return f'+COPS: 0,0,"{self.network}",7\r\nOK\r\n'
        elif cmd == "AT+CSQ":
            return f"+CSQ: {self.signal_strength},99\r\nOK\r\n"
        elif cmd == "AT+CBC":
            return f"+CBC: 0,{self.battery_level}\r\nOK\r\n"

        return "ERROR\r\n"

    def _handle_pin(self, cmd: str) -> str:
        """Handle PIN authentication with lockout simulation."""
        try:
            entered_pin = cmd.split('"')[1]
            if entered_pin == self.pin:
                self.locked = False
                return "OK\r\n"

            self.attempts -= 1
            if self.attempts <= 0:
                return "+CME ERROR: SIM PUK2 required\r\n"
            return f"+CME ERROR: incorrect password ({self.attempts} attempts left)\r\n"
        except IndexError:
            return "ERROR\r\n"

    def _generate_sms_response(self) -> str:
        """Generate formatted SMS listing."""
        response = []
        for msg in self.sms_messages:
            timestamp = msg["timestamp"].strftime("%y/%m/%d,%H:%M:%S+00")
            response.append(
                f'+CMGL: {msg["id"]},"{msg["status"]}","{msg["sender"]}",,"{timestamp}"'
            )
            response.append(msg["text"])
        return "\r\n".join(response) + "\r\nOK\r\n"

    def _read_sms(self, cmd: str) -> str:
        """Read specific SMS message."""
        try:
            msg_id = int(cmd.split("=")[1].strip())
            for msg in self.sms_messages:
                if msg["id"] == msg_id:
                    timestamp = msg["timestamp"].strftime("%y/%m/%d,%H:%M:%S+00")
                    return (
                        f'+CMGR: "{msg["status"]}","{msg["sender"]}",,"{timestamp}"\r\n'
                        f"{msg['text']}\r\nOK\r\n"
                    )
            return "ERROR\r\n"
        except (ValueError, IndexError):
            return "ERROR\r\n"

    def _delete_sms(self, cmd: str) -> str:
        """Delete SMS message (simulated only)."""
        return "OK\r\n"

    def _read_phonebook(self, cmd: str) -> str:
        """Read phonebook entries."""
        try:
            entries = cmd.split("=")[1].split(",")
            start = int(entries[0])
            end = int(entries[1]) if len(entries) > 1 else start

            response = []
            for entry in self.phonebook:
                if start <= entry["index"] <= end:
                    response.append(
                        f'+CPBR: {entry["index"]},"{entry["number"]}",{entry["type"]},"{entry["name"]}"'
                    )
            return "\r\n".join(response) + "\r\nOK\r\n"
        except (ValueError, IndexError):
            return "ERROR\r\n"

    def _simulate_background_events(self):
        """Simulate real-time network changes."""
        while True:
            time.sleep(30)
            self.signal_strength = random.randint(10, 31)
            self.battery_level = max(5, self.battery_level - random.randint(0, 2))
            if random.random() < 0.3:
                new_sms = {
                    "id": max(msg["id"] for msg in self.sms_messages) + 1,
                    "sender": "System",
                    "text": f"Update: Signal strength {self.signal_strength}/31",
                    "status": "REC UNREAD",
                    "timestamp": datetime.now(),
                }
                self.sms_messages.append(new_sms)
                self.logger.info("Generated system notification SMS")

    def run(self):
        """Main emulation loop with enhanced robustness."""
        self.logger.info(f"SIM ready (PIN: {self.pin})")
        self.logger.info(f"IMSI: {self.imsi} | ICCID: {self.iccid}")
        try:
            while True:
                try:
                    if self._data_available():
                        data = os.read(self.master_fd, 1024).decode(errors="ignore")
                        if data:
                            self._command_buffer += data
                            if "\r" in self._command_buffer:
                                commands = self._command_buffer.split("\r")
                                self._command_buffer = (
                                    commands.pop()
                                )  # Save incomplete command
                                for cmd in commands:
                                    response = self._process_command(cmd)
                                    os.write(self.master_fd, response.encode())
                except KeyboardInterrupt:
                    break
                except Exception as e:
                    self.logger.error(f"Processing error: {str(e)}")
                    time.sleep(0.1)
        finally:
            os.close(self.master_fd)
            self.logger.info("SIM stopped")

    def _data_available(self, timeout: float = 0.1) -> bool:
        """Check for incoming data with timeout."""
        import select

        return bool(select.select([self.master_fd], [], [], timeout)[0])


if __name__ == "__main__":
    emulator = AdvancedSIMEmulator(pin="1234")
    print(f"Connect your SIM reader to: {emulator.slave_name}")
    print('Supported commands: AT, AT+CPIN?, AT+CPIN="<PIN>", AT+CMGL="ALL",')
    print("AT+CMGR=<id>, AT+CMGD=<id>, AT+CPBS?, AT+CPBR=<start>[,<end>],")
    print("AT+CIMI, AT+CCID, AT+COPS?, AT+CSQ, AT+CBC")
    emulator.run()
