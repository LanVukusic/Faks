# SIM Card Emulator

A realistic virtual SIM card with serial interface that responds to standard AT commands.
Quick Start
bash

python3 fakeSIM.py

The emulator will display the virtual serial port path to connect to.
Features

- Core SIM Functions:
  - PIN authentication (default: "1234")
  - 3 attempt limit before lockout
  - Basic AT command response (AT)

- SMS Handling:
  - List all SMS messages (AT+CMGL="ALL")
  - Preloaded test messages (3 examples)
  - Proper SMS status flags ("REC UNREAD"/"REC READ")

- SIM Identification:
  - IMSI number response (AT+CIMI)
  - ICCID response (AT+CCID)

- Network Simulation:
  - Network operator info (AT+COPS?)
  - Signal strength reporting (AT+CSQ)
  - Battery level reporting (AT+CBC)

- Phonebook:
  - Basic phonebook access (AT+CPBR)
  - 5 preloaded contacts

## Supported Commands

```
AT               - Basic handshake
AT+CPIN?         - Check PIN status
AT+CPIN="1234"   - Enter PIN
AT+CMGL="ALL"    - List all SMS
AT+CMGR=<id>     - Read specific SMS
AT+CPBR=<index>  - Read phonebook entry
AT+CIMI          - Get IMSI
AT+CCID          - Get ICCID
AT+COPS?         - Get network operator
AT+CSQ           - Signal quality
AT+CBC           - Battery charge
```
