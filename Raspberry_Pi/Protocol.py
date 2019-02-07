#!/usr/bin/python
import RPi.GPIO as GPIO
import time
import os
import sys

GPIO.setmode(GPIO.BCM)
PIR1=7
PIR2=17
GPIO.setup(PIR1,GPIO.IN)
GPIO.setup(PIR2,GPIO.IN)
Cnt=sys.argv[1]
Count=int(Cnt)
Shared=1
in_out=0
Success=0
try:
        while True:
		print Shared
		if Shared==1:
			in_out=0
			print "Entered Into The Count!!!"
			while True:
				print "Waiting For The Event!!!"
                		if GPIO.input(PIR1):
					print "Sensor 1"
					in_out=1
					Shared=0
					break;
				elif GPIO.input(PIR2):
					print "Sensor 2"
					in_out=2
					Shared=0
					break;
				time.sleep(5)
			if in_out==1:
				print "Protocol 1"
				Wait=0
				Success=0
				time.sleep(5)
				while Wait<=5:
					print Wait
					if GPIO.input(PIR2):
						print "Second Input!!!"
						Success=1
						Count+=1
						break;
					elif GPIO.input(PIR1):
						Success=0
						break;
					Wait=Wait+1
					time.sleep(1)
				if Success==0:
					os.system("./GlowLED.sh &")
			elif in_out==2:
				print "Protocol 2"
				Wait=0
				Success=0
				time.sleep(5)
				while Wait<=5:
					print Wait
                			if GPIO.input(PIR1):
						print "Second Input!!!"
						Success=1
						Count-=1
						break;
					elif GPIO.input(PIR2):
						Success=0
						break;
					Wait=Wait+1
					time.sleep(1)
				if Success==0:
					os.system("./GlowLED.sh &")
			Cnt=str(Count)
			print "Current Human Count:"  + Cnt
			if Count<0:
				os.system("./GlowLED.sh")
		Shared=1
		time.sleep(10)
except KeyboardInterrupt:

       print "Quit"

       GPIO.cleanup()

