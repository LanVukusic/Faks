# useful commands  
## Bash  
cut by delimiter  
```bash
cut -d " " -f2,3 # 2 in 3 element
cut -d " " -f2:5 # od druggega do petega
cut -d " " -f2- # od drugega naprej
```

replace single character  

```bash
# set variable a with
# replace character : with ; in var
a=$( echo "$var" | tr -s ":" ";")
```  
tocke-1$!-12.txt

replacing substrings  
```bash
var="Kako se mate /neki"
rem="Kako "
echo ${var#"$rem"}  # "se mate /neki"
echo ${var%"/neki"}  # "Kako se mate"
echo ${var//"neki"/"nekidrugga"}  # "Kako se mate /nekidrugga"
```

default variable

```bash
#input = $1 if $1 is set and "tocke.txt" otherwise
input="${1:-tocke.txt}"
``` 

random randint  
```bash
v2=$(( ( RANDOM % 5 )  + 1 )) # random number from 1 to 5
```

some useful comparisons

```bash
    if [ -d "$file" ]
    then
        #file is a direcotry
    else
    fi

    [ -f "$file" ]  # file exists
```

read file line by line  
```bash
a () {
    input="${1:-tocke.txt}"
    IFS=$'\n'
    for i in $(cat $input)
    do
        st=$(echo $i | cut -d " " -f1)
        v1=$(echo $i | cut -d " " -f2)
        if [ "$v2" == "R" ]
        then
            v2=$(( ( RANDOM % 5 )  + 1 )) # random number from 1 to 5
        fi
    done
}
a $1
```


## C
cleaned  
```c
int main(int argc, char* argv[]) {
    int fd[2];
    pipe(fd); // fd[0] read; df[1] write

    // child 1
    int pid1 = fork();
    if (pid1 == 0) {
        // Child process 1 (ping)
        dup2(fd[1], STDOUT_FILENO);
        close(fd[0]);
        close(fd[1]);
        execlp("ping", "ping", "-c", "5", "google.com", NULL);
    }
    
    // child 2
    int pid2 = fork();
    if (pid2 == 0) {
        // Child process 2 (grep)
        dup2(fd[0], STDIN_FILENO);
        close(fd[0]);
        close(fd[1]);
        execlp("grep", "grep", "rtt", NULL);
    }
    
    // wait for both
    waitpid(pid1, NULL, 0);
    waitpid(pid2, NULL, 0);
    close(fd[0]);  // we need to close both so the app will terminate
    close(fd[1]);
    return 0;
}
```