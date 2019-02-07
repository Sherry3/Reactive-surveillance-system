#!/usr/bin/python
import os
import cv2
from PIL import Image
import time
import math

os.system("fswebcam -r 352x288 Test.jpg")
test_image=Image.open('/home/pi/Major/Test.jpg')
print 'Image1 Mode: %s' % test_image.mode
time.sleep(10)
#os.system("fswebcam -r 352x288 Test1.jpg")
test1_image=Image.open('/home/pi/Major/Black.jpg')
print 'Image2 Mode: %s' % test_image.mode

img1 = test_image.convert('L').resize((9,8),Image.ANTIALIAS,)
img2 = test1_image.convert('L').resize((9,8),Image.ANTIALIAS)

width1,height1 = img1.size
pixels = list(img1.getdata())
for col in xrange(height1):
	print pixels[col:col+width1]

width2,height2 = img2.size
pixels1 = list(img2.getdata())
for col in xrange(height2):
        print pixels1[col:col+width2]

Sum_Image1 = 0
Sum_Image2 = 0

Difference = []
for row in xrange(8):
	for col in xrange(9):
		if pixels[col + row]>= 0 and pixels[row + col]<=50:
			Difference.append(0)
		elif pixels[col + row]>=50 and pixels[col + row]<=190:
			Difference.append(1)
			Sum_Image1 = Sum_Image1 + 1
		else:
			Difference.append(0)
for col in xrange(width1):
	print Difference[col:col + (width1)]

Difference1 = []
for row in xrange(8):
        for col in xrange(9):
		if pixels1[col + row]>= 0 and pixels1[row + col]<=50:
                        Difference1.append(0)
                else:
                        Difference1.append(1)
			Sum_Image2 = Sum_Image2 + 1
for col in xrange(width1):
        print Difference1[col:col + (width1)]


Dot_Product = 0
Mag1 = 0
Mag2 = 0
for row in xrange(8):
	col=0
	for col in xrange(9):
		pixel_img1 = pixels[row + col]
		pixel_img2 = pixels1[row + col]
		Dot_Product = Dot_Product + (pixel_img1 * pixel_img2)
		Mag1 = Mag1 + pixel_img1 * pixel_img1
		Mag2 = Mag2 + pixel_img2 * pixel_img2
Inter_Result = math.sqrt(Mag1) * math.sqrt(Mag2)
if Inter_Result == 0:
	print "There might be Tampering with the Webcam!!!"
        print "Check If Lens Cap is Open!!!"
        os.system("./GlowLED.sh")
else:
	Result = Dot_Product/Inter_Result
	print Result

	if Result > 0.85:
		print "There might be Tampering with the Webcam!!!"
		print "Check If Lens Cap is Open!!!"
		os.system("./GlowLED.sh")
print Sum_Image1
print Sum_Image2
if Sum_Image1 < 20 or Sum_Image1 > 60:
	print "There might be Tampering with the Webcam!!!"
        print "Check If Lens Cap is Open!!!"
        os.system("./GlowLED.sh")
os.system("rm /home/pi/Major/Test.jpg")
os.system("rm /home/pi/Major/Test1.jpg")


	
