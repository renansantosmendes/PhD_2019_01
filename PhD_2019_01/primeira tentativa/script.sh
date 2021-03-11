#!/bin/bash 

for i in 50 100 150 200 250
do
	cd .//DR_1//$i
	nohup java -jar *.jar &
	cd ..
	cd ..

	cd .//DR_25//$i
	nohup java -jar *.jar &
	cd ..
	cd ..

	cd .//VAR_1//$i
	nohup java -jar *.jar &
	cd ..
	cd ..

	cd .//VAR_25//$i
	nohup java -jar *.jar &
	cd ..
	cd ..
done