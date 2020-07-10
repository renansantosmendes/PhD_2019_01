#!/bin/bash 
#SBATCH --qos=qos-30d
#SBATCH --partition=lamho-1
module load jdk8_32
java -jar r250n12tw10k4s.jar
