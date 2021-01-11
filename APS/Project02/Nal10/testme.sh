for((i = 1; i < 11; i++));
do
printf "\n%d:" $i;
time java Naloga10 test/I_$i.txt out_$i.txt;
cmp test/O_$i.txt out_$i.txt;
done