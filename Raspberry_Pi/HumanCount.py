#!/usr/bin/python
import RPi.GPIO as GPIO
import time
import os
import sys
import subprocess

GPIO.setmode(GPIO.BCM)
PIR1=7
PIR2=17
GPIO.setup(PIR1,GPIO.IN)
GPIO.setup(PIR2,GPIO.IN)
Cnt=sys.argv[1]
Cnt=int(Cnt)
in_out=0
try:
	while True:
		print Cnt
		Count=0
		if GPIO.input(PIR1):
			Count+=1
		if GPIO.input(PIR2):
			Count+=2
		print Count
		in_out=Count
		if Count==3:
			os.system("./GlowLED.sh")
			in_out=0
		while True:
			if in_out==1:
				time.sleep(1)
				Count1=0
                		if GPIO.input(PIR1):
                        		Count1+=1
                		if GPIO.input(PIR2):
                        		Count1+=2
				if Count1==3:
					time.sleep(1)
				else:
					os.system("./GlowLED.sh")
					break;
				
				Count2=0
                       		if GPIO.input(PIR1):
                                	Count2+=1
                        	if GPIO.input(PIR2):
                                	Count2+=2
                        	if Count2==2:
                                	time.sleep(1)
				else:
					os.system("./GlowLED.sh")
					break;

				Count3=0
                        	if GPIO.input(PIR1):
                                	Count3+=1
                        	if GPIO.input(PIR2):
                                	Count3+=2
                        	if Count3==0:
                                	time.sleep(1)
					Cnt+=1
				else:
					os.system("./GlowLED.sh")
			elif in_out==2:
				time.sleep(1)
                        	Count4=0
                        	if GPIO.input(PIR1):
                                	Count4+=1
                        	if GPIO.input(PIR2):
                                	Count4+=2
                        	if Count4==3:
                                	time.sleep(1)
                        	else:
					os.system("./GlowLED.sh")
					break;
                        	Count5=0
                        	if GPIO.input(PIR1):
                                	Count5+=1
                        	if GPIO.input(PIR2):
                                	Count5+=2
                        	if Count5==1:
                                	time.sleep(1)
                        	else:
					os.system("./GlowLED.sh")
					break;
                        	Count6=0
                        	if GPIO.input(PIR1):
                                	Count6+=1
                        	if GPIO.input(PIR2):
                                	Count6+=2
                        	if Count6==0:
                                	time.sleep(1)
                                	Cnt-=1
                        	else:
					os.system("./GlowLED.sh")
					break;

except KeyboardInterrupt:

       print "Quit"

       GPIO.cleanup()

