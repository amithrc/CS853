# CS853
This repository contains all the source code for the **CS853** 


# Java

version **1.8**

# Lucene

Version **7.4.0**

# Build Tool

maven

Linux Distros Debian based can get from the repo.

apt-get update && apt-get install maven

Windows users, please use the below link to get maven up and running

https://www.mkyong.com/maven/how-to-install-maven-in-windows/


# How to Run it?

* git clone https://github.com/amithrc/CS853.git  
* mvn compile --> Compiles the code  
* mvn package --> Compiles and create the jar file in the target Directory.  
* java -jar <jar_name> should run the program.   
* mvn clean --> Removes all the existing build.  

# General Instructions

If you do not like to create the Maven project, feel free to use the Lucene Library (7.4.0) and then you can commit the code
into proper directory structure.


# Issues running Maven

If you see the compile error saying the JAVA_HOME is not set, most probably, JAVA_HOME is not set. You need to modify the .bashrc.
'''
JAVA_HOME="/usr/lib/jvm/java-1.8.0-openjdk-amd64"  
export JAVA_HOME  
export PATH=$PATH:$JAVA_HOME/bin  
'''
