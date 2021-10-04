#!/bin/bash
demon () {
    exec 0<&-
    exec 1>&-
    exec 2>&-
    trap "ustvari" SIGUSR1
    trap "quit" SIGTERM

    while true
    do
        sleep 1
    done
}

function ustvari {
    echo "ustvari"
}

function quit {
    exit(0)
}

demon &


