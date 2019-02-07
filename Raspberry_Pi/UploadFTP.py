#!/usr/bin/python2
from ftplib import FTP
import sys
import os

#count=1
ftp = FTP('sssdo.host56.com')
ftp.login(user = 'a6879791', passwd = 'M@jor2K15')
ftp.cwd('/Motion/System1')
if sys.argv[3]=="1":
	ftp.cwd('/Motion/System1/Zone1')
	folder=('Zone1/')
elif sys.argv[3]=="2":
	ftp.cwd('/Motion/System1/Zone2')
	folder=('Zone2/')
elif sys.argv[3]=="3":
	ftp.cwd('/Motion/System1/Zone3')
	folder=('Zone3/')
else:
	ftp.cwd('/Motion/System1/Zone4')
	folder=('Zone4/')
fp = '/home/pi/Major/home/pi/Camera/'+sys.argv[1]+'.avi'
filename = open(fp,'rb')
count=int(sys.argv[2])
ftp.storbinary('STOR Motion(%d).avi' %count ,filename)
filename.close()
fd = '/home/pi/Major/temp/temp.jpg'
print fd
Imagefile = open(fd,'rb')
ftp.storbinary('STOR Motion(%d).jpg' %count, Imagefile)
Imagefile.close()
Link_Pass = '/Motion/System1/'+folder+'Motion('+sys.argv[2]+').avi'
HLink_Pass = 'http://sssdo.host56.com/Motion/System1/'+folder+'Motion('+sys.argv[2]+').avi'
link_pass = '"' + Link_Pass + '"'
print Link_Pass
hlink_pass = '"' + HLink_Pass + '"'
Command='"fileToUpload='+ fd + '&folder=' + folder + '"'
print Command
os.system("curl --data ${Command} --url http://sssdo.host56.com/phpFiles/upload1.php")
os.system("./Notification.sh " + link_pass)
#count=count+1
ftp.quit()

