#!/bin/bash

#cd /home/matlab/Viki

#comsol PHYNDI (myPHYNDI) + mySUSE + PHYNDI2
COMSOL=/opt/comsol43/bin/comsol


#comsol kissrv
#COMSOL=/d/inst/viki/comsol/comsol_install/comsol43/bin/comsol


####MODIFY

c_kcl_base=0.06
c_kcl_acid=0
U0=10;

L=0.7    #modify
c_fix=0.004   #modify
#ezt meg a forrasfajlban is at kell irni... hacsak nem irom at
t_peak=0.5

c_kcl_base_t=0.06
#c_kcl_acid_t=0.02
U0_t=10;

#c_kcl_acid_t[0]=0.001
#c_kcl_acid_t[1]=0.002
#c_kcl_acid_t[2]=0.005
#c_kcl_acid_t[3]=0.01
#c_kcl_acid_t[4]=0.015
#c_kcl_acid_t[5]=0.02
#c_kcl_acid_t[6]=0.025
#c_kcl_acid_t[7]=0.03
#c_kcl_acid_t[8]=0.035
#c_kcl_acid_t[9]=0.04
#c_kcl_acid_t[10]=0.045
#c_kcl_acid_t[11]=0.05
#c_kcl_acid_t[12]=0.055
#c_kcl_acid_t[13]=0.06
#c_kcl_acid_t[14]=0.065
#c_kcl_acid_t[15]=0.07
#c_kcl_acid_t[16]=0.075
#c_kcl_acid_t[17]=0.08

c_kcl_acid_t[0]=0.001
c_kcl_acid_t[1]=0.002
c_kcl_acid_t[2]=0.005
c_kcl_acid_t[3]=0.01
c_kcl_acid_t[4]=0.015
c_kcl_acid_t[5]=0.02
c_kcl_acid_t[6]=0.025
c_kcl_acid_t[7]=0.03
c_kcl_acid_t[8]=0.04
c_kcl_acid_t[9]=0.05
c_kcl_acid_t[10]=0.06
c_kcl_acid_t[11]=0.07
c_kcl_acid_t[12]=0.08




#for kissrv
#DIR=/d/inst/viki/acidbase_transient_data/impulzus/mph_bin/

#for phyndi (myPHYNDI)
#DIR=/home/matlab/acidbase_transient_data/impulzus_new/mph_bin/

#for phyndi2 (under GIBBS)
DIR=/home/comsol/acidbase_transient_data/impulzus/mph_bin/


#ez a ciklus a koncentraciok kiszamolasara szolgal; egyszerre jo sok szamolast el lehessen inditani...
#modify 10..17
for i in {8..12}
do

#itt ellenorizni kell, hogy az mph_bin-en belul letre vannak-e hozva az alkonyvtarak, ahova ezt menteni kell...
#ugyanolyan hierarchianak megfeleloen, mint a current es a profile
#attol fuggoen hogyan megy majd a bontas, lehet majd [i] indexekkel jatszani...    

DIRECTORY=${DIR}L_${L}mm
echo $DIRECTORY

if [ ! -d "$DIRECTORY" ] 
then
  	echo "DIR DOES NOT EXIST!"
	#DIR must be done.
	mkdir ${DIRECTORY}
else
	echo "DIR EXISTS"
	#Don't do anything with the dir, just check for SUBDIR.	
fi


DIRECTORY=${DIRECTORY}/c_fix_${c_fix}
echo ${DIRECTORY}

if [ ! -d "$DIRECTORY" ] 
then
	echo "SUBDIR DOES NOT EXIST!"
	#DIR must be done.
	mkdir ${DIRECTORY}
else
	echo "SUBDIR EXISTS"
fi



DIRECTORY=${DIRECTORY}/${U0}_V
echo $DIRECTORY

if [ ! -d "$DIRECTORY" ] 
then
  	echo "SUBDIR DOES NOT EXIST!"
	#DIR must be done.
	mkdir ${DIRECTORY}
else
	echo "SUBDIR EXISTS"
	#Don't do anything with the dir, just check for SUBDIR.	
fi


DIRECTORY=${DIRECTORY}/t_${t_peak}
echo $DIRECTORY

if [ ! -d "$DIRECTORY" ] 
then
  	echo "SUBDIR DOES NOT EXIST!"
	#DIR must be done.
	mkdir ${DIRECTORY}
else
	echo "SUBDIR EXISTS"
	#Don't do anything with the dir, just check for SUBDIR.	
fi



DIRECTORY=${DIRECTORY}/c_kcl_b_${c_kcl_base}/
echo ${DIRECTORY}

if [ ! -d "$DIRECTORY" ] 
then
	echo "SUBDIR DOES NOT EXIST!"
	#DIR must be done.
	mkdir ${DIRECTORY}
else
	echo "SUBDIR EXISTS"
fi


c_kcl_base=0.06
c_kcl_base_t=0.06


MPH=imp_c_kcl_${c_kcl_base}_${c_kcl_acid}_c_kcl_t_${c_kcl_base_t}_${c_kcl_acid_t[i]}_U_${U0}_L_${L}mm_c_fix_${c_fix}_t_${t_peak}_


NAME=${DIRECTORY}${MPH}$(date +%Y%m%d_%T).mph


#for kissrv
#TMP=/d/inst/viki/tmp/

#for phyndi (myPHYNDI)
#TMP=/home/matlab/

#for phyndi2 (under GIBBS)
TMP=/home/comsol/comsol_trash/


#$1 a leforditott .class fajlt adja oda, amit futtatni kell

${COMSOL} batch -graphics -3drend sw -inputfile $1 ${c_kcl_base} ${c_kcl_acid} ${U0} ${c_kcl_base_t} ${c_kcl_acid_t[i]} ${U0_t} ${L} ${c_fix} -outputfile ${NAME} -tmpdir ${TMP}

done


