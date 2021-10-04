#!/bin/bash

sum (){
    IFS=" "
    num=$(cat "$1" | cut -d " " -f2)
    all=0
    for v in $num; do
        # echo $v
        (( all = all + "$v" ))
    done
    echo $all
}

sum "$1"

# num=$(cat "$1" | cut -d " " -f2,3) # 2 in 3 element
# num=$(cat "$1" | cut -d " " -f2:5) # od druggega do petega
# num=$(cat "$1" | cut -d " " -f2-) # od drugega naprej