#!/bin/sh
#gcc FetchIP.c -o FetchIP.o
#./FetchIP.o
IPAddr1="$(ifconfig wlan0 | grep 'inet addr' | cut -d: -f2 | awk '{print $1}')"
IPAddr2="$(ifconfig eth0 | grep 'inet addr' | cut -d: -f2 | awk '{print $1}')"
curl --data "IpAddress1=$IPAddr1&IpAddress2=$IPAddr2" http://sssdo.host56.com/phpFiles/setIp.php
chmod 777 InetAddrFetch.sh
