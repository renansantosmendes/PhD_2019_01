#!/bin/bash 

for i in 50
do
	cd .//DR_50//$i
	nohup java -jar *.jar &
	cd ..
	cd ..
done