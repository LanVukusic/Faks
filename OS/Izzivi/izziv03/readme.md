# Povezave  
## Symlinks 101  
```bash 
#ln -s source_file symbolic_link
ln -s realdeal/fajl.txt link

#izpiše cilj
readlink link
```
Če pobrišemo cilj povezave link ostane, nemoremo pa seveda nič z nim delat. "file not found error"  

## Hard links  
```bash
ln realdeal/neki.txt ln1
ln realdeal/neki.txt ln2
ln realdeal/neki.txt ln3
ls -l
# -rw-rw-r-- 4 ubuntu ubuntu    0 Apr  3 15:03 ln1
# prva številka -4 je število linkov
```  

## 5 linkov brez ln  
```bash
mkdir 5links
cd 5links
mkdir 1
mkdir 2
mkdir 3
cd ..
ls -l
# drwxrwxr-x 5 ubuntu ubuntu 4096 Apr  3 15:10 5links
```


# Uporabniki in skupine  
## Osnovni ukazi  
- ### spreminjanje lastništva  
    ```bash
    sudo chown -R mojuporabnik:mojagrupa moj.fajl
    ```  
    -R pove da rekurzivno owna tudi vse podfolderje  
    "mojuporabnik" je ime userja ki dobi lastnistvo 
    "mojagrupa"  je group ki dobi lastinstvo. Lahko napišemo tudi samo :groupname, ki spremeni samo group.  

- ### spreminjanje pravic  
    Komanda "chmod"  
    ```bash
    chmod ug+rwx moj.fajl
    chmod a-x moj.fajl
    ```
    Prva da vse pravice uporabniku "u" in grupi "g" za moj.fajl.  
    Druga umakne exectue pravico vsem.   

## Kateri uporabniki imajo default shell "bash"?  
- ### Kateri uporabniki?  
    ```bash
    cat /etc/passwd | grep /bin/bash | cut -d : -f1
    ```
    Teli uporabniki.  

- ### Kakšne so shelli in frekvence uporabe?
    ```bash
    cat /etc/passwd | cut -d : -f1,7
    ```
    Frekvence uporabe neznam. Sploh ne razumem vprašanja. Lahko bi dobil recimo idle times userjev da bi vidu kolko casa so gor al pa nihove logine da vidim kdaj so prijavljeni in to, sam se mi zdi da ni prav.  

- ## 100 uporabnikov  
    Najprej zazenes multipass virtualko, da ne smetiš prelepega Ubuntuja.  
    ```bash
    #!/bin/bash
    for i in `seq 1 10`;
    do
            useradd --create-home "dummy-$i"
            touch "/home/dummy-$i/secret.token"
            chown "dummy-$i" "/home/dummy-$i/secret.token"  
            echo "dummy-$i created"
    done 
    ```

# Shadow backdoor  
Recikliram .c fajl iz prejšnjega izziva.
Fajl "shadow.c"  

dodamo pravice  
```bash
sudo chown root a.out 
sudo chmod u+s ./a.out
sudo chmod g+s ./a.out
./a.out /etc/shadow
```

In to dela :D  

# mymkdir  


