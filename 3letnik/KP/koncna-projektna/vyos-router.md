# nastavitve ipja na zunanji mrezni kartici

- ipv4 : 88.200.24.231/24
- ipv6 : 2001:1470:fffd:80::2/64

## ipv6 segmentacija

- 2001:1470:fffd:80::/64
  - povezava z lrk
- 2001:1470:fffd:81::/64
  - internal
- 2001:1470:fffd:82::/64
  - DMZ
  - SLAAC
- 2001:1470:fffd:83::/64
  - npt ULA fc00:dead:fffd:83::/64
  - ipv6 only

## interfaces

```
Interface        IP Address                        S/L  Description
---------        ----------                        ---  -----------
eth0             88.200.24.231/24                  u/u  ISP connection interface 
                 2001:1470:fffd:80::2/64
eth1             10.8.0.1/24                       u/u  internal interface 
                 2001:1470:fffd:81::1/64
eth2             192.168.1.1/24                    u/u  DMZ interface 
                 2001:1470:fffd:82::1/64
lo               127.0.0.1/8                       u/u  
                 ::1/128
wg01             10.1.1.1/24                       u/u  VPN 
```

## wireguard

- [vyos config](https://docs.vyos.io/en/crux/configuration/interfaces/wireguard.html)
- [wireguard info](https://www.wireguard.com/quickstart/)

## fw setup

set firewall name OUTSIDE-LOCAL rule 33 action 'accept'
set firewall name OUTSIDE-LOCAL rule 33 destination port '80'
set firewall name OUTSIDE-LOCAL rule 33 protocol 'tcp'
set firewall name OUTSIDE-LOCAL rule 33 state new 'enable'

## etcd conf



## komande vyos

```bash
set firewall name OUTSIDE-IN default-action 'drop'
set firewall name OUTSIDE-IN rule 10 action 'accept'
set firewall name OUTSIDE-IN rule 10 state established 'enable'
set firewall name OUTSIDE-IN rule 10 state related 'enable'
set firewall name OUTSIDE-LOCAL default-action 'drop'
set firewall name OUTSIDE-LOCAL rule 10 action 'accept'
set firewall name OUTSIDE-LOCAL rule 10 state established 'enable'
set firewall name OUTSIDE-LOCAL rule 10 state related 'enable'
set firewall name OUTSIDE-LOCAL rule 20 action 'accept'
set firewall name OUTSIDE-LOCAL rule 20 icmp type-name 'echo-request'
set firewall name OUTSIDE-LOCAL rule 20 protocol 'icmp'
set firewall name OUTSIDE-LOCAL rule 20 state new 'enable'
set firewall name OUTSIDE-LOCAL rule 31 action 'accept'
set firewall name OUTSIDE-LOCAL rule 31 destination port '22'
set firewall name OUTSIDE-LOCAL rule 31 protocol 'tcp'
set firewall name OUTSIDE-LOCAL rule 31 state new 'enable'
set firewall name OUTSIDE-LOCAL rule 32 action 'accept'
set firewall name OUTSIDE-LOCAL rule 32 destination port '51820'
set firewall name OUTSIDE-LOCAL rule 32 protocol 'tcp'
set firewall name OUTSIDE-LOCAL rule 32 state new 'enable'
set firewall name OUTSIDE-LOCAL rule 33 action 'accept'
set firewall name OUTSIDE-LOCAL rule 33 destination port '80'
set firewall name OUTSIDE-LOCAL rule 33 protocol 'tcp'
set firewall name OUTSIDE-LOCAL rule 33 state new 'enable'
set interfaces ethernet eth0 address '88.200.24.231/24'
set interfaces ethernet eth0 address '2001:1470:fffd:80::2/64'
set interfaces ethernet eth0 description 'ISP connection interface'
set interfaces ethernet eth0 duplex 'auto'
set interfaces ethernet eth0 hw-id '00:0c:29:72:23:9c'
set interfaces ethernet eth0 smp-affinity 'auto'
set interfaces ethernet eth0 speed 'auto'
set interfaces ethernet eth1 address '10.8.0.1/24'
set interfaces ethernet eth1 address '2001:1470:fffd:81::1/64'
set interfaces ethernet eth1 description 'internal interface'
set interfaces ethernet eth1 duplex 'auto'
set interfaces ethernet eth1 hw-id '00:0c:29:72:23:a6'
set interfaces ethernet eth1 ipv6 dup-addr-detect-transmits '1'
set interfaces ethernet eth1 ipv6 router-advert cur-hop-limit '64'
set interfaces ethernet eth1 ipv6 router-advert link-mtu '0'
set interfaces ethernet eth1 ipv6 router-advert managed-flag 'true'
set interfaces ethernet eth1 ipv6 router-advert max-interval '600'
set interfaces ethernet eth1 ipv6 router-advert other-config-flag 'false'
set interfaces ethernet eth1 ipv6 router-advert reachable-time '0'
set interfaces ethernet eth1 ipv6 router-advert retrans-timer '0'
set interfaces ethernet eth1 ipv6 router-advert send-advert 'true'
set interfaces ethernet eth1 smp-affinity 'auto'
set interfaces ethernet eth1 speed 'auto'
set interfaces ethernet eth2 address '192.168.1.1/24'
set interfaces ethernet eth2 address '2001:1470:fffd:82::1/64'
set interfaces ethernet eth2 description 'DMZ interface'
set interfaces ethernet eth2 duplex 'auto'
set interfaces ethernet eth2 hw-id '00:0c:29:72:23:b0'
set interfaces ethernet eth2 ipv6 dup-addr-detect-transmits '1'
set interfaces ethernet eth2 ipv6 router-advert cur-hop-limit '64'
set interfaces ethernet eth2 ipv6 router-advert link-mtu '0'
set interfaces ethernet eth2 ipv6 router-advert managed-flag 'false'
set interfaces ethernet eth2 ipv6 router-advert max-interval '600'
set interfaces ethernet eth2 ipv6 router-advert other-config-flag 'false'
set interfaces ethernet eth2 ipv6 router-advert prefix 2001:1470:fffd:82::/64 autonomous-flag 'true'
set interfaces ethernet eth2 ipv6 router-advert prefix 2001:1470:fffd:82::/64 on-link-flag 'true'
set interfaces ethernet eth2 ipv6 router-advert prefix 2001:1470:fffd:82::/64 valid-lifetime '2592000'
set interfaces ethernet eth2 ipv6 router-advert reachable-time '0'
set interfaces ethernet eth2 ipv6 router-advert retrans-timer '0'
set interfaces ethernet eth2 ipv6 router-advert send-advert 'true'
set interfaces ethernet eth2 smp-affinity 'auto'
set interfaces ethernet eth2 speed 'auto'
set interfaces ethernet eth3 address '2001:1470:fffd:83::1/64'
set interfaces ethernet eth3 address 'fc00:dead:fffd:83::1/64'
set interfaces ethernet eth3 description 'ipv6only'
set interfaces ethernet eth3 hw-id '00:0c:29:72:23:ba'
set interfaces ethernet eth3 ipv6 router-advert name-server '2001:4860:4860::8888'
set interfaces ethernet eth3 ipv6 router-advert prefix 2001:1470:fffd:83::/64
set interfaces ethernet eth3 ipv6 router-advert prefix fc00:dead:fffd:83::/64
set interfaces ethernet eth3 ipv6 router-advert send-advert 'true'
set interfaces loopback lo
set interfaces wireguard wg01 address '10.1.1.1/24'
set interfaces wireguard wg01 description 'VPN'
set interfaces wireguard wg01 peer mojpc allowed-ips '10.1.1.0/24'
set interfaces wireguard wg01 peer mojpc allowed-ips '10.1.1.2/32'
set interfaces wireguard wg01 peer mojpc pubkey '8QG74H0oMCJVP2zDYCtzi8cF4gjTo5lN8Vvyfag8imc='
set interfaces wireguard wg01 port '51820'
set nat destination rule 100 description 'SSH to APISERV'
set nat destination rule 100 destination address '88.200.24.231'
set nat destination rule 100 destination port '2299'
set nat destination rule 100 inbound-interface 'eth0'
set nat destination rule 100 protocol 'tcp_udp'
set nat destination rule 100 translation address '192.168.1.101'
set nat destination rule 100 translation port '22'
set nat destination rule 101 description 'http to apiserv'
set nat destination rule 101 destination address '88.200.24.231'
set nat destination rule 101 destination port '80'
set nat destination rule 101 inbound-interface 'eth0'
set nat destination rule 101 protocol 'tcp_udp'
set nat destination rule 101 translation address '192.168.1.101'
set nat destination rule 101 translation port '80'
set nat destination rule 102 description 'SSH to OTHR'
set nat destination rule 102 destination address '88.200.24.231'
set nat destination rule 102 destination port '2288'
set nat destination rule 102 inbound-interface 'eth0'
set nat destination rule 102 protocol 'tcp_udp'
set nat destination rule 102 translation address '192.168.1.102'
set nat destination rule 102 translation port '22'
set nat destination rule 103 description 'SSH to ETCD'
set nat destination rule 103 destination address '88.200.24.31'
set nat destination rule 103 destination port '2277'
set nat destination rule 103 inbound-interface 'eth0'
set nat destination rule 103 protocol 'tcp_udp'
set nat destination rule 103 translation address '192.168.1.103'
set nat destination rule 103 translation port '22'
set nat nptv6 rule 66 outbound-interface 'eth0'
set nat nptv6 rule 66 source prefix 'fc00:dead:fffd:83::/64'
set nat nptv6 rule 66 translation prefix '2001:1470:fffd:83::/64'
set nat source rule 100 outbound-interface 'eth0'
set nat source rule 100 source address '192.168.1.0/24'
set nat source rule 100 translation address 'masquerade'
set nat source rule 105 outbound-interface 'eth0'
set nat source rule 105 source address '10.8.0.0/24'
set nat source rule 105 translation address 'masquerade'
set protocols static route 0.0.0.0/0 next-hop 88.200.24.1
set protocols static route6 ::/0 next-hop 2001:1470:fffd:80::1
set service dhcp-server shared-network-name DMZ subnet 192.168.1.0/24 default-router '192.168.1.1'
set service dhcp-server shared-network-name DMZ subnet 192.168.1.0/24 dns-server '8.8.8.8'
set service dhcp-server shared-network-name DMZ subnet 192.168.1.0/24 domain-name 'sk01'
set service dhcp-server shared-network-name DMZ subnet 192.168.1.0/24 lease '86400'
set service dhcp-server shared-network-name DMZ subnet 192.168.1.0/24 range 0 start '192.168.1.100'
set service dhcp-server shared-network-name DMZ subnet 192.168.1.0/24 range 0 stop '192.168.1.200'
set service dhcp-server shared-network-name DMZ subnet 192.168.1.0/24 static-mapping APISERV ip-address '192.168.1.101'
set service dhcp-server shared-network-name DMZ subnet 192.168.1.0/24 static-mapping APISERV mac-address '00:0c:29:be:c3:98'
set service dhcp-server shared-network-name DMZ subnet 192.168.1.0/24 static-mapping ETCD ip-address '192.168.1.103'
set service dhcp-server shared-network-name DMZ subnet 192.168.1.0/24 static-mapping ETCD mac-address '00:0c:29:a6:68:00'
set service dhcp-server shared-network-name DMZ subnet 192.168.1.0/24 static-mapping OTHR ip-address '192.168.1.102'
set service dhcp-server shared-network-name DMZ subnet 192.168.1.0/24 static-mapping OTHR mac-address '00:0c:29:7a:ee:7f'
set service dhcp-server shared-network-name INTERNAL subnet 10.8.0.0/24 default-router '10.8.0.1'
set service dhcp-server shared-network-name INTERNAL subnet 10.8.0.0/24 dns-server '8.8.8.8'
set service dhcp-server shared-network-name INTERNAL subnet 10.8.0.0/24 dns-server '8.8.4.4'
set service dhcp-server shared-network-name INTERNAL subnet 10.8.0.0/24 domain-name 'internal'
set service dhcp-server shared-network-name INTERNAL subnet 10.8.0.0/24 lease '86400'
set service dhcp-server shared-network-name INTERNAL subnet 10.8.0.0/24 range 0 start '10.8.0.100'
set service dhcp-server shared-network-name INTERNAL subnet 10.8.0.0/24 range 0 stop '10.8.0.200'
set service dhcpv6-server preference '255'
set service dhcpv6-server shared-network-name internalv6 subnet 2001:1470:fffd:81::/64 address-range start 2001:1470:fffd:81::100 stop '2001:1470:fffd:81::200'
set service dhcpv6-server shared-network-name internalv6 subnet 2001:1470:fffd:81::/64 name-server '2606:4700:4700::111'
set service dns forwarding allow-from '0.0.0.0/0'
set service dns forwarding cache-size '0'
set service dns forwarding listen-address '127.0.0.1'
set service dns forwarding name-server '1.1.1.1'
set service dns forwarding name-server '8.8.8.8'
set service ssh listen-address '0.0.0.0'
set service ssh port '22'
set system config-management commit-revisions '100'
set system console device ttyS0 speed '9600'
set system host-name 'sk01'
set system login user vyos authentication encrypted-password '$6$3WxZPFIo7t0$h31NvT2aKXRG75p9U465TaalBFeYnrWLZKOx.o3vrsr6pEQ3Ylw/swc9YUqZ3HM0.f3aBHcXPh.aC95DVE4rB0'
set system login user vyos authentication plaintext-password ''
set system login user vyos level 'admin'
set system name-server '1.1.1.1'
set system ntp server ntp1.arnes.si
set system syslog global facility all level 'info'
set system syslog global facility protocols level 'debug'
set system time-zone 'UTC'
```