rename () {
    echo $(echo $1 | sed --expression='s/\//_/g' )
}

remove_basedir () {
    echo ${2#"$1"}
}

bckpdir=${2:-"/var/bkp"}

rec () {
    for i in $1/*
    do
        if [ -d $i ]
        then
            # if i is a folder
            rec $i $2 $3
        else
            if [ -f $(remove_basedir $3 $i ) ]
            then
                dr=rename $(remove_basedir $3 $i )
                cp $i $dr
            else
                cp $i "$2/$(remove_basedir $3 $i )"
            fi
        fi
    done
}

rec $1 $bckpdir $1
#rename "neki/neki/gay/fegi"
#remove_basedir "neki/gay" "neki/gay/haha" 