# SIM Card Reader Tool Documentation

## Introduction

This Python tool provides an interface for communicating with SIM cards via a serial connection. It allows you to check SIM status, unlock PIN-protected SIMs, and read SMS messages directly from the card. I developed this for my embedded systems course project to understand how mobile networks interact with SIM cards at a low level.
Getting Started

## Connect to SIM card on COM3 port with PIN 1234

```python
from sim_reader import SIMReader, SIMError

with SIMReader(port="COM3", pin="1234") as sim:
    print(f"SIM status: {'Locked' if sim.requires_pin else 'Unlocked'}")

```

## Reading SMS Messages

```python

try:
    with SIMReader("/dev/ttyUSB0", pin="0000") as sim:
        if sim.requires_pin:
            if sim.enter_pin("0000"):
                print("SIM unlocked successfully!")
            else:
                print("Wrong PIN!")
                exit()
        
        # Read unread messages
        messages = sim.read_sms(mode="REC UNREAD")
        for msg in messages:
            print(f"From: {msg['sender']}")
            print(f"Received: {msg['timestamp']}")
            print(f"Content: {msg['body']}\n")
            
except SIMError as e:
    print(f"Error occurred: {e}")

```

## Checking PIN Status

```python
with SIMReader("COM4") as sim:
    status = sim.check_pin_status()
    print(f"Current PIN status: {status}")
    
    # Example outputs:
    # - "READY" (SIM unlocked)
    # - "SIM PIN" (PIN required)
    # - "SIM PUK" (PUK required)

```

## Class Reference

### SIMReader Class

The main class for SIM card communication

Initialization Parameters:

  port (str): Serial port name (e.g., 'COM3' or '/dev/ttyUSB0')

  baudrate (int): Communication speed (default 9600)

  timeout (int): Command timeout in seconds (default 2)

  pin (str): Optional PIN code for automatic unlocking

  retries (int): PIN attempt retries (default 3)

Key Methods

  enter_pin(pin: str) -> bool
  Unlocks the SIM card with provided PIN. Returns True if successful.

  check_pin_status() -> str
  Returns current PIN status as a string.

  read_sms(mode: str = "ALL") -> List[Dict]
  Reads SMS messages from SIM. Modes:

      "ALL": All messages

      "REC UNREAD": Unread messages

      "REC READ": Read messages

  Returns list of dictionaries with message details.

## Advanced Examples

### Handling Multiple SIM Cards

```python

ports = ["COM3", "COM4", "/dev/ttyUSB1"]

for port in ports:
    try:
        with SIMReader(port) as sim:
            print(f"\nReading from {port}:")
            if sim.requires_pin:
                print("Skipping locked SIM")
                continue
                
            messages = sim.read_sms()
            print(f"Found {len(messages)} messages")
            
    except SIMError:
        print(f"No SIM detected on {port}")

```

## Logging Operations

```python

import logging

logging.basicConfig(
    level=logging.DEBUG,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    filename='sim_operations.log'
)

with SIMReader("/dev/pts/5", pin="5678") as sim:
    # All operations will be logged to file
    if sim.requires_pin:
        sim.enter_pin("5678")
    messages = sim.read_sms()

```

Troubleshooting
Common Issues

    Connection Errors:

        Verify correct port name

        Check device manager for active ports

        Ensure no other programs are using the port

    AT Command Failures:

        Try different baud rates (115200, 57600, 38400)

        Add delay between commands with time.sleep(0.5)

    SMS Parsing Issues:

        Some carriers use non-standard formats

        Enable debug logging to see raw responses

## Example Error Handling

```python


try:
    with SIMReader("COM5", pin="1234") as sim:
        # Your operations here
        
except SIMError as e:
    print(f"SIM communication error: {e}")
    
except serial.SerialException as e:
    print(f"Serial port error: {e}")
    
except KeyboardInterrupt:
    print("Operation cancelled by user")

```
