#!/bin/bash 

for i in 50 100 150 200 250
do
	cd .//DR_50//$i
	nohup java -jar *.jar &
	cd ..
	cd ..

	cd .//DR_100//$i
	nohup java -jar *.jar &
	cd ..
	cd ..

	cd .//VAR_50//$i
	nohup java -jar *.jar &
	cd ..
	cd ..

	cd .//VAR_100//$i
	nohup java -jar *.jar &
	cd ..
	cd ..

	cd .//PEARSON_50//$i
	nohup java -jar *.jar &
	cd ..
	cd ..

	cd .//PEARSON_100//$i
	nohup java -jar *.jar &
	cd ..
	cd ..
done