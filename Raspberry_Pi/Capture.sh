#!/bin/bash
service motion stop
echo $1
i=0
NAME=$(date +"%Y-%m-%d_%H%M%S")
while [ $i -lt 30 ]
do
DATE=$(date +"%Y-%m-%d_%H%M%S")
fswebcam -S 5 --jpeg 95 --shadow  --title "SMART SECURITY SYSTEM"  --subtitle "Home" --info "Monitor 1: Active" /home/pi/Major/home/pi/Camera/$DATE.jpg 
i=$[$i+1]
done
service motion start
sh ./Core.sh $NAME $1 $2 &

chmod 777 Capture.sh
