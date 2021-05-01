#!/bin/bash

pomoc() {
    echo "Uporaba: $0 akcija parametri"

    return 0
}

hehho() {
    for((i = 1; i <= "$#"; i++)); do
        word="${!i}"
        word=${word//a/ha}
        word=${word//e/he}
        word=${word//i/hi}
        word=${word//o/ho}
        word=${word//u/hu}
        echo "$((i-1)): $word"
    done

    return 0
}

status() {
    if (($1 % $2 == 0)); then
        echo "$2"
        return $(($2 % 256))
    else
        status $2 $(($1 % $2))
    fi
}

leto() {
    for leto in "$@"
    do
        echo -n "Leto $leto "
        [[ $((leto % 400)) == 0 ]] && echo -n "je " || (
        [[ $((leto % 100)) == 0 ]] && echo -n "ni " || (
        [[ $((leto % 4)) == 0 ]] && echo -n "je " || ( echo -n "ni " )))
		
        echo "prestopno."
    done

    return 0
}

stej() {
    cat "$@" | sed -e '/^[[:blank:]]*$/d;/^[[:blank:]]*#/d' | cut -d":" -f2 | sed -e 's/^[[:blank:]]*//' | sort | uniq -c | sort -bnr | nl
    return $?
}

fib() {
    for ((ar = $#; ar > 0; ar--))
    do
        a=0
        b=1

        if ((${!ar} == 0)); then
            echo "${!ar}: 0"
        else if ((${!ar} == 1)); then
                echo "${!ar}: 1"
            else
                for ((i = 0; i < $((${!ar} - 1)); i++))
                do
                    fn=$((a + b))
                    a=$b
                    b=$fn
                done
                echo "${!ar}: $fn"
            fi
        fi
        
    done

    return 0
}

upori() {
    for user in "$@"
    do
        echo -n "$user: "
        uid=$(id -u $user 2>/dev/null)

        if (($? == 1)); then
            echo "err"
        else
            gid=$(id -g $user)
            gr=$(getent group $gid | cut -d: -f1)
            #echo "$gr"

            if [[ $uid == $gid ]] ; then
                echo -n "enaka "
            else
                echo -n "razlicna "
            fi

            hd=$(cat '/etc/passwd' | grep $user | cut -d":" -f6)
            none="/nonexistent"

            if [ "$hd" = "$none" ]; then
                echo -n "ne-obstaja "
            else
                echo -n "obstaja "
            fi

            gs=$(id -G $user | wc -w)
            echo -n "$gs "

            proc=$(ps -u $user --no-header | sed -e '/^[[:blank:]]*$/d') 
            if [[ "$proc" == "" ]] ; then
                echo "0"
            else
                cnt=$(echo "$proc" | wc -l)
                echo "$cnt"
            fi
        fi

        #echo "id: $(id $user)"
        #echo -n "pswd: " 
        #cat '/etc/passwd' | grep -G "^$user:"
        #echo "proc: "
        #echo "$proc"
        #echo "--------------------------------------------------------"

        
    done

    return $?
}

pusher() {
    for ((i = 0; i < $1; i++));
    do
        echo -n "----"
    done
    return 0
}

drevo() { # drevo file curr_depth max_depth
    #echo  "$2 $3"
    if (($2 > $3)); then
        return 0
    fi

    cd "$1" 2>/dev/null

    if (($? != 0)); then
        pusher $2
        echo "Probably no permission to view this directory"
        return 1
    fi

    for file in *
    do
        #echo "$file"

        if [[ $file == '*' ]] ; then
            continue
        fi

        typ=$(stat "$file" --format="%F" 2>/dev/null)
        err=$?

        if (($err != 0)); then
            pusher $2
            echo "Error on geting file stat: could be permissions: stat exit code: $err | typ: $typ: file: $file"
            continue
        fi


        #stats=$(stat "$file" --format="%B %b %s" 2>/dev/null) #DELETE

        pusher $2

        case "$typ" in
            "block special file")
                echo "BLOCK $file"
                ;;
            "character special file")
                echo "CHAR  $file"
                ;;
            "directory")
                echo "DIR   $file"
                drevo "$file" $(($2 + 1)) $3
                ;;
            "fifo")
                echo "PIPE  $file"
                ;;
            "regular empty file")
                echo "FILE  $file"
                ;;
            "regular file")
                echo "FILE  $file"
                ;;
            "socket")
                echo "SOCK  $file"
                ;;
            "symbolic link")
                echo "LINK  $file"
                ;;
            *)
                echo "Provided filetype not expected: type provided: $typ"
                ;;
        esac
    done

    cd ..

    return 0
}

prostor() { # prostor file curr_depth max_depth
    #echo  "$2 $3"
    if (($2 > $3)); then
        return 0
    fi

    cd "$1" 2>/dev/null

    if (($? != 0)); then
        echo "Probably no permission to view this directory: $1"
        return 1
    fi

    for file in *
    do
        #echo "$file"
        if [[ "$file" == '*' ]] ;
        then
            continue
        fi

        stats=$(stat "$file" --format="%B %b %s" 2>/dev/null)

        err=$?

        if (($err != 0)); then
            echo "Error on geting file stat: could be permissions: err: $err: stat: $stat"
            continue
        fi

        stats=$(stat "$file" --format="%B %b %s" 2>/dev/null)
        b_size_r=$(echo "$stats" | cut -d" " -f1)
        blokov_r=$(echo "$stats" | cut -d" " -f2)
        prostor_r=$(($b_size * $blokov_r))
        velikost_r=$(echo "$stats" | cut -d" " -f3)

        velikost=$(($velikost + $velikost_r))
        blokov=$(($blokov + $blokov_r))
        prostor=$(($prostor + $prostor_r))

        #pusher $2
        #echo "vel: $velikost blok: $blokov pros: $prostor | stats: $stats"

        if [ -d "$file" ]; then
            prostor "$file" $(($2 + 1)) $3
        fi
    done

    cd ..

    return 0

}

testing() {
    echo "root: enaka obstaja 1 2"
    echo "daemon: enaka obstaja 1 0"
    echo "bin: enaka obstaja 1 0"
    echo "sync: razlicna obstaja 1 0"
    echo "games: razlicna obstaja 1 0"
    echo "nobody: enaka ne-obstaja 1 0"
    echo "test: razlicna obstaja 1 3"
}

case "$1" in
    "pomoc")
        pomoc
        exit $?
        ;;
    "hehho")
        shift
        hehho "$@"
        exit $?
        ;;
    "status")
        shift
        status "$@"
        exit $?
        ;;
    "leto")
        shift
        leto "$@"
        ;;
    "stej")
        shift
        stej "$@"
        exit $?
        ;;
    "frik")
        stej $2
        exit $?
        ;;
    "fib")
        shift
        fib "$@"
        exit $?
        ;;
    "upori")
        #testing
        #exit 0

        shift
        upori "$@"
        exit $?
        ;;
    "userinfo")
        shift
        upori "$@"
        exit $?
        ;;
    "tocke")
        RANDOM=42
        sum=0
        cnt=0
        while read -r;
        do
            if ! [[ "$REPLY" =~ (^[[:blank:]]*\#|^[[:blank:]]*$) ]] ; then
                l="$REPLY"

                l_sum=0 

                v=$(echo "$l" | cut -d" " -f1)
                a=$(echo "$l" | cut -d" " -f2)
                b=$(echo "$l" | cut -d" " -f3)
                c=$(echo "$l" | cut -d" " -f4)
                t=$(echo "$l" | cut -d" " -f5)

                l_sum=$(($a + $b + $c))

                if [[ "$t" == "P" || "$t" == "p" ]] ; then
                    l_sum=$(($l_sum / 2))
                else
                    if [[ $(echo $v | cut -c3-4) == "14" ]] ; then
                        l_sum=$(($l_sum + $((1 + $RANDOM % 5))))
                    fi
                fi

                if (("$l_sum" > 50)); then
                    l_sum=50
                fi

                cnt=$(($cnt + 1))
                sum=$(($sum + $l_sum))

                echo "$v: $l_sum"
            fi
        done

        echo "St. studentov: $cnt"
        echo "Povprecne tocke: $(($sum / $cnt))"

        exit 0
        ;;
    "drevo")
        shift
        echo "DIR   $1"
        drevo "$1" "1" ${2:-3} 
        exit $?
        ;;
    "prostor")
        shift
        stats=$(stat "$1" --format="%B %b %s" 2>/dev/null)
        b_size=$(echo "$stats" | cut -d" " -f1)
        blokov=$(echo "$stats" | cut -d" " -f2)
        prostor=$(($b_size * $blokov))
        velikost=$(echo "$stats" | cut -d" " -f3)

        #echo "vel: $velikost blok: $blokov pros: $prostor | stats: $stats"

        prostor "$1" "1" ${2:-3}

        echo "Velikost: $velikost"
        echo "Blokov: $blokov"
        echo "Prostor: $prostor"

        exit $?
        ;;
    *)
        echo "Napacna uporaba skripte!"
        pomoc
        exit 42;
        ;;
esac

exit 0;



