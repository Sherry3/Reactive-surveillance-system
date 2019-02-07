#!/bin/bash 
echo $1
String=$1
Link=`echo "$String" | sed -r 's/[&]+/%26/g'`
echo $Link
curl --data "DeviceId=dv1&Link=$Link" http://sssdo.host56.com/phpFiles/loadGcmIdByDeviceId.php
chmod 777 Notification.sh
