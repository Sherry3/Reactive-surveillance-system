#!/usr/bin/python
import RPi.GPIO as GPIO
import time

GPIO.setmode(GPIO.BCM)

GPIO.setup(18,GPIO.OUT)
print "LED Test (CTRL+C to exit)"
time.sleep(2)
GPIO.output(18,True)
time.sleep(10)
GPIO.output(18,False)
