
direct_parent () {
    i=$1
    while [ $i != 1 ]
    do
        echo -n $i " "
        i=$(cat /proc/"$i"/stat | cut -d " " -f4)
    done
    echo $i
}

IFS=$'\n'
for i in $(cat $1)
do
    direct_parent $i
done
