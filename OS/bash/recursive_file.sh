#!/bin/bash

rec (){
    for name in "$1"/*; do
        if [ -d "$name" ]; then
            rec "$name"
        else
            echo "$name"
        fi
    done
}

rec $1