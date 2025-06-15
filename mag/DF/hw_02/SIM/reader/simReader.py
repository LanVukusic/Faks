import serial
import time
import logging
from typing import List, Dict, Optional, Tuple


class SIMError(Exception):
    """Custom exception for SIM communication errors"""

    pass


class SIMReader:
    """
    Professional-grade SIM card reader with:
    - Automatic PIN management
    - Comprehensive error handling
    - Full AT command support
    - Cross-platform compatibility
    """

    def __init__(
        self,
        port: str,
        baudrate: int = 9600,
        timeout: int = 2,
        pin: Optional[str] = None,
        retries: int = 3,
    ):
        """
        Initialize SIM card reader

        Args:
            port: Serial port path (e.g. '/dev/pts/3')
            baudrate: Communication speed (default 9600)
            timeout: Command timeout in seconds (default 2)
            pin: Optional PIN code
            retries: PIN attempt retries (default 3)
        """
        self.port = port
        self.baudrate = baudrate
        self.timeout = timeout
        self.pin = pin
        self.retries = retries
        self.requires_pin = True  # Assume locked until verified
        self.logger = self._setup_logging()

        try:
            self.serial_conn = serial.Serial(
                port=port,
                baudrate=baudrate,
                timeout=timeout,
                bytesize=serial.EIGHTBITS,
                parity=serial.PARITY_NONE,
                stopbits=serial.STOPBITS_ONE,
            )
            self.logger.info(f"Connected to {port} at {baudrate} baud")
            self._initialize_sim()
        except serial.SerialException as e:
            self.logger.error(f"Connection failed: {e}")
            raise SIMError(f"Could not connect to {port}") from e

    def _setup_logging(self) -> logging.Logger:
        """Configure module-level logging"""
        logger = logging.getLogger("SIMReader")
        logger.setLevel(logging.INFO)

        # Prevent duplicate handlers
        if not logger.hasHandlers():
            handler = logging.StreamHandler()
            handler.setFormatter(
                logging.Formatter(
                    "%(asctime)s - %(name)s - %(levelname)s - %(message)s"
                )
            )
            logger.addHandler(handler)
        return logger

    def _initialize_sim(self):
        """Initialize SIM card communication"""
        try:
            # Send initial AT command to check connectivity
            response = self._send_command("AT")
            if "OK" not in response:
                raise SIMError("No response from SIM")

            # Check initial PIN status
            self.requires_pin = self._check_pin_required()

            # Auto-unlock if PIN provided
            if self.requires_pin and self.pin:
                self.enter_pin(self.pin)
                # Verify PIN status after unlocking
                self.requires_pin = self._check_pin_required()
        except Exception as e:
            self.logger.error(f"Initialization failed: {e}")
            raise SIMError("SIM initialization failed") from e

    def _send_command(self, cmd: str, timeout: Optional[float] = None) -> List[str]:
        """
        Send AT command and return response lines

        Args:
            cmd: AT command (without \r\n)
            timeout: Optional override of default timeout

        Returns:
            List of response lines
        """
        if not self.serial_conn.is_open:
            raise SIMError("Serial connection not open")

        # Clear buffers
        self.serial_conn.reset_input_buffer()
        self.serial_conn.reset_output_buffer()

        # Set temporary timeout if specified
        original_timeout = self.serial_conn.timeout
        if timeout is not None:
            self.serial_conn.timeout = timeout

        try:
            # Send command with proper line endings
            self.serial_conn.write(f"{cmd}\r\n".encode())
            self.logger.debug(f"Sent: {cmd}")

            # Read response
            response = []
            start_time = time.time()

            while time.time() - start_time < self.serial_conn.timeout:
                line = self.serial_conn.readline().decode(errors="ignore").strip()
                if line:
                    response.append(line)
                    self.logger.debug(f"Received: {line}")

                    # Stop on final result codes
                    if line in (
                        "OK",
                        "ERROR",
                        "NO CARRIER",
                        "NO ANSWER",
                        "NO DIALTONE",
                    ):
                        break
                    # Handle unsolicited responses
                    if (
                        line.startswith("+CMTI:")
                        or line.startswith("+CUSD:")
                        or line.startswith("+CDS:")
                    ):
                        continue

            return response
        finally:
            # Restore original timeout
            if timeout is not None:
                self.serial_conn.timeout = original_timeout

    def _check_pin_required(self) -> bool:
        """Check if SIM requires PIN"""
        response = self._send_command("AT+CPIN?")
        return any("SIM PIN" in line for line in response)

    def enter_pin(self, pin: str) -> bool:
        """
        Enter PIN code to unlock SIM

        Args:
            pin: PIN code to attempt

        Returns:
            True if PIN accepted, False otherwise
        """
        for attempt in range(1, self.retries + 1):
            response = self._send_command(f'AT+CPIN="{pin}"')

            if any("OK" in line for line in response):
                self.requires_pin = False
                self.logger.info(f"PIN accepted on attempt {attempt}")
                return True

            self.logger.warning(f"Invalid PIN (attempt {attempt}/{self.retries})")
            time.sleep(1)  # Delay between attempts

        self.logger.error("PIN entry failed after all attempts")
        return False

    def check_pin_status(self) -> str:
        """
        Check current PIN status

        Returns:
            Status string ("READY", "SIM PIN", etc.)

        Raises:
            SIMError: If status cannot be determined
        """
        response = self._send_command("AT+CPIN?")

        for line in response:
            if line.startswith("+CPIN:"):
                return line.split(":", 1)[1].strip()

        raise SIMError("Could not determine PIN status")

    def read_sms(self, mode: str = "ALL") -> List[Dict[str, str]]:
        """
        Read SMS messages from SIM

        Args:
            mode: "ALL", "REC UNREAD", "REC READ"

        Returns:
            List of SMS messages as dictionaries

        Raises:
            SIMError: If SIM is locked or command fails
        """
        if self.requires_pin:
            raise SIMError("SIM requires PIN before reading SMS")

        response = self._send_command(f'AT+CMGL="{mode}"', timeout=5)

        messages = []
        current_msg = {}
        body_lines = []

        for line in response:
            if line.startswith("+CMGL:"):
                # Save previous message if exists
                if current_msg:
                    current_msg["body"] = "\n".join(body_lines).strip()
                    messages.append(current_msg)
                    body_lines = []

                # Parse header
                parts = [p.strip('"') for p in line[7:].split(",", 4)]
                if len(parts) >= 5:
                    current_msg = {
                        "index": parts[0],
                        "status": parts[1],
                        "sender": parts[2],
                        "alpha": parts[3],
                        "timestamp": parts[4],
                    }
            elif current_msg:
                # Collect body lines
                if line not in ("OK", "ERROR"):
                    body_lines.append(line)

        # Save last message
        if current_msg:
            current_msg["body"] = "\n".join(body_lines).strip()
            messages.append(current_msg)

        return messages

    def __enter__(self):
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        if self.serial_conn.is_open:
            self.serial_conn.close()
            self.logger.info("Connection closed")


# Example usage
if __name__ == "__main__":
    # Configure logging
    logging.basicConfig(level=logging.INFO)

    try:
        with SIMReader("/dev/pts/3", pin="1234") as sim:
            print(f"PIN required: {sim.requires_pin}")

            if sim.requires_pin:
                if sim.enter_pin("1234"):
                    print("Successfully unlocked SIM")
                else:
                    print("Failed to unlock SIM")
                    exit(1)

            print("\nReading SMS messages:")
            for msg in sim.read_sms():
                print(f"ID: {msg['index']} | From: {msg['sender']}")
                print(f"Time: {msg['timestamp']}")
                print(f"Body: {msg['body']}\n")

    except SIMError as e:
        print(f"SIM Error: {e}")
    except KeyboardInterrupt:
        print("\nOperation cancelled")
