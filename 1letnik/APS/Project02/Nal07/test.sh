for((i = 0; i < 11; i++));
do
printf "\n%d:" $i;
time java Naloga7 test/I2_$i.txt out_$i.txt;
cmp test/O2_$i.txt out_$i.txt;
done