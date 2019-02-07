#!/bin/bash

ls /home/pi/Major/home/pi/Camera/*.jpg > Stills.txt
Image_Path=`head -n 1 Stills.txt`
echo $Image_Path 
cp $Image_Path /home/pi/Major/temp/temp.jpg
mencoder -ovc lavc -o /home/pi/Major/home/pi/Camera/$1.avi -mf type=jpg:fps=1 mf://@Stills.txt
file=$1
python ./uploader.py ./uploader.cfg /home/pi/Major/home/pi/Camera/$1.avi &
python ./UploadFTP.py $file $2 $3 &

chmod 777 Core.sh


