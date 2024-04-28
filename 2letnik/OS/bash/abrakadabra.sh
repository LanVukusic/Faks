#!/bin/bash

rec () {
    for i in $1/*; do
        if [ -d "$i" ]
        then
            rec $i
        else
            v=$(cat $i | grep "abrakadabra")
            if [ ${#v} -ge 2 ]
            then
                echo $i
            fi
        fi
    done
}

rec ${1:-"~/"}