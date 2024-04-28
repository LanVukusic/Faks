#!/bin/bash

a () {
    input="${1:-tocke.txt}"
    IFS=$'\n'
    for i in $(cat $input);do
        st=$(echo $i | cut -d " " -f1)
        v1=$(echo $i | cut -d " " -f2)
        v2=$(echo $i | cut -d " " -f3)
        v3=$(echo $i | cut -d " " -f4)

        if [ "$v2" == "R" ]
        then
            v2=$(( ( RANDOM % 5 )  + 1 )) # random number from 1 to 5
        fi

        echo $st $((v1 + v2 + v3))
    done
}

a $1 | sort 

