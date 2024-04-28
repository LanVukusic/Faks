#!/bin/bash

gcd (){
    dividend=$1
    # Assign values ​​at will.
    divisor=$2
    #+ Here, it doesn't matter which value is given.
    # Why is it okay?
    remainder=1
    # If uninitialized variables are used in the loop,
    #+ Then in the first loop,
    #+ It will generate an error message.
    until [ "$remainder" -eq 0 ]
    do
    let "remainder = $dividend % $divisor"
    dividend=$divisor
    # Now use the two smallest numbers to repeat.
    divisor=$remainder
    done
    # Euclid's algorithm
}

leapy () {
    # tole tehnicno nima IF-a notr tko da...
    while [[ $(( $1 % 400 )) == 0 || $(( $1 % 100 )) != 0 && $(( $1 % 4 )) == 0 ]]
    do
        echo "Leto $1 je prestopno."
        return
    done
    echo "Leto $1 ni prestopno."
}

fib(){
    ind=$1

    if [[ "$ind" == 0 ]] ; then
        echo "0"
        return
    fi
    if [[ "$ind" == 1 ]] ; then
        echo "1"
        return
    fi

    a="1"
    b="1"

    for((j=2; j < $ind; j++)) do
        c=$b
        b=$(($a + $b))
        a=$c
    done

    echo "$b"
    return
 }

recurse_drev() {
    if [ "$2" == 0 ];then
        return
    fi
    cd "$1"
    for i in *;do

        if [[ "$i" == "*" ]]; then
            cd ..
            return
        fi

        for((j=0; j<$(($3 + 1)); j++));do
            echo -n "----"
        done

        file=$(stat "$i" --format="%F")

        case "$file" in
            "regular empty file")
                echo "FILE  $i"
                ;;
            "regular file")
                echo "FILE  $i"
                ;;
            "fifo")
                echo "PIPE  $i"
                ;;
            "block special file")
                echo "BLOCK $i"
                ;;
            "character special file")
                echo "CHAR  $i"
                ;;
            "socket")
                echo "SOCK  $i"
                ;;
            "symbolic link")
                echo "LINK  $i"
                ;;
            "directory")
                echo "DIR   $i"
                recurse_drev "$i" $(($2 - 1)) $(($3 + 1))
                ;;
        esac
    done
    cd ..
}

recurse_prost() {

    #echo "$1"
    
    fileSize=$(stat --format=%s "$1")
    blockCount=$(stat --format=%b "$1")
    blockSize=$(stat --format=%B "$1")
    (( maxSize = maxSize + fileSize ))
    (( maxBlock = maxBlock + blockCount ))
    prostor=$(( blockCount * blockSize))
    (( maxProstor = maxProstor + prostor))

    if [ "$2" == 0 ] ; then
        return
    fi

    cd "$1"

    #ls -la

    for i in * ; do
        if [[ "$i" == "*" ]]; then
            cd ..
            return
        fi
        if [ -d "$i" ];then
            recurse_prost "$i" $(( $2 - 1 ))
        else
            
            fileSize=$(stat --format=%s "$i")
            blockCount=$(stat --format=%b "$i")
            blockSize=$(stat --format=%B "$i")
            (( maxSize = maxSize + fileSize ))
            (( maxBlock = maxBlock + blockCount ))
            prostor=$(( blockCount * blockSize))
            (( maxProstor = maxProstor + prostor))
        fi
    done
    cd ..
}

case $1 in

    "asd")
        echo "gay"
        ;;
    
    "hehho")
        #replaca use samoglasnike X z hX
        shift
        ((var=0))
        while test $# -gt 0
        do
            ret=$( echo $1 | sed -r 's/a/ha/g' | sed -r 's/e/he/g' | sed -r 's/i/hi/g'| sed -r 's/o/ho/g' | sed -r 's/u/hu/g' )
            echo "$var: $ret"
            ((var=var+1))
            shift
        done
        ;;

    "status") 
        #gcd prvih dveh argumentov
        gcd $2 $3
        echo "$dividend"
        exit $(($dividend % 256))
        ;;
    
    "leto")
        #racuna prestopno leto
        
        shift
        while test $# -gt 0
        do
            leapy $1
            shift
        done
        ;;

    "stej")
        shift
        cat "$@" | cut -d":" -f2 | sed -e 's/^[[:blank:]]*//;/^[[:blank:]]*$/d;/^[[:blank:]]*#/d' | sort | uniq -c | sort -bnr | nl
    ;;

    "fib")
        for ((i = $#; i > 1; i--));
        do
            echo -n "${!i}: "
            fib "${!i}"
        done
    ;;

    "upori")
        for i in $(echo $@ | cut -d' ' -f2-)
        do
            A=$(grep -E "^$i:" /etc/passwd)
            if [ ${#A} -ge 1 ]; then
                uid=$(grep -E "^$i:" /etc/passwd | cut -d: -f3)
                gid=$(grep -E "^$i:" /etc/passwd | cut -d: -f4)
                home=$(grep -E "^$i:" /etc/passwd | cut -d: -f6)

                enaka=$([[ $gid == $uid ]] && echo "enaka" || echo "razlicna")
                obstaja=$([[ $home == "/nonexistent" ]] && echo "ne-obstaja" || echo "obstaja")

                grp=$(groups $i | cut -d':' -f2 | wc -w)
                prc=$(ps -u $i --no-header | wc -l)
                echo "$i: $enaka $obstaja $grp $prc"
            else
                echo "$i: err"
            fi
        done
    ;;

    "tocke")
        RANDOM=42
        shift
        CUMULATIVE=0
        USERC=0

        while read line; do
            RANDAD=0
            DIVIDE=1

            if [[ "$line" =~ (^\#) || "$line" =~ (^[[:blank:]]*$) ]] ; then
                continue
            fi

            a=$(echo "$line" | cut -d" " -f1 | cut -c3-4)
            if [ "$a" == "14" ] ; then
                RANDAD=$((1 + $RANDOM % 5))
            fi

            a=$(echo "$line" | cut -d" " -f5)
            if [[ "$a"  == "P" || "$a"  == "p" ]] ; then
                DIVIDE=2
                RANDAD=0
            fi

            a=$(echo "$line" | cut -d" " -f2)
            b=$(echo "$line" | cut -d" " -f3)
            c=$(echo "$line" | cut -d" " -f4)

            STUD=$((($a+$b+$c+$RANDAD) / $DIVIDE))

            if (("$STUD" > 50)) ; then
                STUD=50
            fi

            USERC=$(($USERC + 1))
            CUMULATIVE=$(($CUMULATIVE + $STUD))
            
            echo "$(echo "$line" | cut -d" " -f1): $STUD"
        done

        echo "St. studentov: $USERC"
        echo "Povprecne tocke: $(($CUMULATIVE / $USERC))"
    ;;

    "prostor")
        maxSize=0
        maxBlock=0
        maxProstor=0
        
        recurse_prost "$2" "${3:-3}"
        
        echo "Velikost: $maxSize"
        echo "Blokov: $maxBlock"
        echo "Prostor: $maxProstor"
    ;;

    "drevo")
        echo "DIR   $2"      
        recurse_drev "$2" "${3:-3}" 0
    ;;

    "pomoc")
        echo "Uporaba: $0 akcija parametri"
        exit 0
        ;;

    *)
        #usekakor ne pomoc
        echo "Napacna uporaba skripte!"
        echo "Uporaba: $0 akcija parametri"
        exit 42
        ;;
esac

exit 0;







