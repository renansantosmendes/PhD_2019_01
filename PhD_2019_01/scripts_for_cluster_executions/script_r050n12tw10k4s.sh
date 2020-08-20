#!/bin/bash 
#SBATCH --qos=qos-7d
#SBATCH --partition=lamho-0
module load jdk8_32
java -jar r050n12tw10k4s.jar
