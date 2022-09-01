#!/bin/bash

###使用sips命令 制作对应的尺寸的图片

##创建一个文件夹 用来存储图片 比如 我有个软件叫做hello 需要制作图片 我可以把文件件叫做"icon.iconset"
mkdir -p ../icon.iconset

#执行命令创建
sips -z 16 16     ../ori/icon.png --out ../icon.iconset/icon_16x16.png
sips -z 32 32     ../ori/icon.png --out ../icon.iconset/icon_16x16@2x.png
sips -z 32 32     ../ori/icon.png --out ../icon.iconset/icon_32x32.png
sips -z 64 64     ../ori/icon.png --out ../icon.iconset/icon_32x32@2x.png
sips -z 128 128   ../ori/icon.png --out ../icon.iconset/icon_128x128.png
iconutil -c icns ../icon.iconset

rm -rf ../icon.iconset

mv ../icon.icns ../../ant/resources
# ok 制作完成