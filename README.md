# CS853
This repository contains all the source code for the **CS853** 

# Team Members (Team 3)

* Amith Ramanagar chandrashekar
* Pooja oza
* Sepideh Koohfar
* Vaughan coder


# Java

version **1.8**

# Lucene

Version **7.4.0**

# Build Tool

maven

Linux Distros Debian based can get maven from the repo.
```
apt-get update && apt-get install maven
```

Windows users, please use the below link to get maven up and running

https://www.mkyong.com/maven/how-to-install-maven-in-windows/


# How to Run it? (Contributor Note)

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
This is what what I would add to ~/.bashrc

```
JAVA_HOME="/usr/lib/jvm/java-1.8.0-openjdk-amd64"  
export JAVA_HOME  
export PATH=$PATH:$JAVA_HOME/bin  
```

Windows you need to set the JAVA_HOME appropriately 

# Executing program

mvn package created the executable jar with dependencies in the target directory.  File name does says it is jar with dependencies. You can pass the filename to the jar as an argument, this should create the index and call the search methods.

```
mvn package
```

# Build Script

Run the ***./install.sh*** with absolute path to the paragraph_CBOR[0],Outline_CBOR[1],Qrel[2].Program creates the indexed_file directory within the CS853 directory.

Please run git clone https://github.com/amithrc/CS853.git
cd CS853
Run the below Installtion script.
```
./install.sh <absolute_path_to_CBOR> <absolute_path_to_OUTLINE>
```

