#!/bin/bash
#ez az en verziom
#COMSOL=/opt/comsol40a/bin/comsol
#JDK=/usr/lib64/jvm/java-1.6.0-sun/
#cd /home/matlab/Viki

#torolni .classt, ellenorzom, hogy letrejott-e

#rm -f $1.class

#${COMSOL} compile -jdkroot ${JDK} $1.java

#echo
#if [ -f $1.class ]
#then
#	echo "OK, done"
#else
#	echo "ERROR"
#fi

#Laci verzioja
if [ $# -ne 1 ]
then
	echo "Error: The number of arguments does not equal to 1."
	echo "Usage: $0 file[.java]"
	exit 1
fi

FILENAME=${1%.*}
if [ ! -f ${FILENAME}.java ]
then
	echo "${FILENAME}.java: No such file"
	exit 1
fi


# JDK for my SUSE
#JDK=/usr/java/jdk1.6.0_45/

#JDK for phyndi (myPHYNDI)
#JDK=/usr/lib/jvm/java-6-openjdk-amd64

#JDK for kissrv
#JDK=/usr/lib64/jvm/java-1.6.0-openjdk

#JDK for PHYNDI2 (under GIBBS)
JDK=/usr/lib/jvm/java-6-oracle

#4.3 version for PHYNDI and MY_SUSE
COMSOL=/opt/comsol43/bin/comsol

#4.3 version for kissrv
#COMSOL=/d/inst/viki/comsol/comsol_install/comsol43/bin/comsol

rm -f ${FILENAME}.class

#${COMSOL} compile -jdkroot ${JDK} ${FILENAME}.java
${COMSOL} compile -jdkroot ${JDK} *.java

echo
if [ -f ${FILENAME}.class ]
then
	echo "OK, done"
else
	echo "ERROR"
fi
