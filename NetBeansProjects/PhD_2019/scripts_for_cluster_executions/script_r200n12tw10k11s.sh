#!/bin/bash 
#SBATCH --qos=part2d
#SBATCH --partition=medium
module load jdk8_32
java -jar r200n12tw10k11s.jar