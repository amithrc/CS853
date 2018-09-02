#!/bin/bash
#@Author- Amith RC


function usage()
{
	echo "Please pass the CBOR file to the install script"
	echo ""
	exit
}

function call_maven()
{
echo "Executing the Maven for the dependency"
mvn compile
mvn package
sleep 2
}

function change_path_target()
{
	pwdCurrent=$(pwd)
	pwdCurrent=$pwdCurrent/target/LuceneIndex-1.0-SNAPSHOT-jar-with-dependencies.jar
	java -jar $pwdCurrent $1	
}

if [ $# -ne 1 ]
then 
	echo "Please pass the file name as the argument "
else
	echo "File passed is $1"
	echo " "
	call_maven
	change_path_target $1
fi




