#!/usr/bin/python
import RPi.GPIO as GPIO
import time
import os
import sys
import subprocess

Cnt=sys.argv[2]

GPIO.setmode(GPIO.BCM)
PIR_PIN=7
print "PIR Module Test (CTRL - C to Exit)"
GPIO.setup(PIR_PIN,GPIO.IN)
GPIO.setup(18,False)
try:
	print "PIR Module..."
	time.sleep(1)
	print "Ready"
	count=int(Cnt)
	while True:

                  if GPIO.input(PIR_PIN):

                       print "Motion Detected!!!"
		       print "Ready!!!"
		       if sys.argv[1]=="1":
				Command='curl --data "ZoneId=zn1" --url http://sssdo.host56.com/phpFiles/loadZoneLevelByZoneId.php'
				#os.system("curl --data ${Command} --url http://sssdo.host56.com/phpFiles/loadZoneLevelByZoneId.php > file.txt")
				p = subprocess.Popen(Command,stdout=subprocess.PIPE,shell=True)
				output,errors=p.communicate()
		       elif sys.argv[1]=="2":
				Command='curl --data "ZoneId=zn2" --url http://sssdo.host56.com/phpFiles/loadZoneLevelByZoneId.php'
                                p = subprocess.Popen(Command,stdout=subprocess.PIPE,shell=True)
                                output,errors=p.communicate()
		       elif sys.argv[1]=="3":
				Command='curl --data "ZoneId=zn3" --url http://sssdo.host56.com/phpFiles/loadZoneLevelByZoneId.php'
                                #os.system("curl --data ${Command} --url http://sssdo.host56.com/phpFiles/loadZoneLevelByZoneId.php > file.txt")
                                p = subprocess.Popen(Command,stdout=subprocess.PIPE,shell=True)
                                output,errors=p.communicate()
		       elif sys.argv[1]=="4":
				Command='curl --data "ZoneId=zn4" --url http://sssdo.host56.com/phpFiles/loadZoneLevelByZoneId.php'
                                #os.system("curl --data ${Command} --url http://sssdo.host56.com/phpFiles/loadZoneLevelByZoneId.php > file.txt")
                                p = subprocess.Popen(Command,stdout=subprocess.PIPE,shell=True)
                                output,errors=p.communicate()
		       print output
		       if output=="1":
				os.system("./GlowLED.sh")
				print "High Security Zone"
		       #os.system("./InternetDisconnect.sh")
		       os.system("./Notification.sh")
		       os.system("rm /home/pi/Major/home/pi/Camera/*.jpg")
		       os.system("rm /home/pi/Major/home/pi/Camera/*.avi")
		       os.system("./Capture.sh" + " " + Cnt + " " + sys.argv[1])
		       count=count+1
		       Cnt=str(count)
                  time.sleep(2)

except KeyboardInterrupt:

       print "Quit"

       GPIO.cleanup()
