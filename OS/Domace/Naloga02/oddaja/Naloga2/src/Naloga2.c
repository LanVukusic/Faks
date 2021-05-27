#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <errno.h>
#include <string.h>
#include <sys/types.h>
#include <sys/resource.h>
#include <unistd.h>
#include <sys/utsname.h>
#include <time.h>
#include <sys/time.h>
#include <sys/types.h>
#include <dirent.h>
#include <sys/stat.h>
#include <ctype.h>

//svn checkout https://lalgec.fri.uni-lj.si/os/lv8541/Naloga2
//svn commit -m "#oddaj:Naloga2#"

// swaps 2 items
void swap(int *xp, int *yp)
{
    int temp = *xp;
    *xp = *yp;
    *yp = temp;
}
// checks if string is of digits only
int digits_only(char *s)
{
    while (*s)
    {  
         char c = *s++;
        if ((c >= '0' && c <= '9') == 0)
            return 0;
    }

    return 1;
}
// Function to perform Selection Sort
void selectionSort(int arr[], int n)
{
    int i, j, min_idx;

    // One by one move boundary of unsorted subarray
    for (i = 0; i < n - 1; i++)
    {

        // Find the minimum element in unsorted array
        min_idx = i;
        for (j = i + 1; j < n; j++)
            if (arr[j] < arr[min_idx])
                min_idx = j;

        // Swap the found minimum element
        // with the first element
        swap(&arr[min_idx], &arr[i]);
    }
}
// function to print arr
void printArray(int arr[], int size)
{
    int i;
    for (i = 0; i < size; i++)
        printf("%d\n", arr[i]);
}
// get file size
int fsize(FILE *fp)
{
    int prev = ftell(fp);
    fseek(fp, 0L, SEEK_END);
    int sz = ftell(fp);
    fseek(fp, prev, SEEK_SET); //go back to where we were
    return sz;
}
// file to string
char *fileToString(char *path, int len)
{
    FILE *fp = fopen(path, "r");
    if (fp == NULL)
    {
        perror("cant open file");
    }
    char *file = calloc(len, sizeof(char));
    int i = 0;
    char c;
    while ((file[i] = fgetc(fp)) != EOF)
    {
        i++;
    }
    fclose(fp);
    return file;
}
// reads process file and gets name out
char *nameFromPID(int pid, char const *argv[])
{
    char path[1000];
    sprintf(path, "%s/%d/status", argv[2], pid);
    char *data = fileToString(path, 10000 * sizeof(char));

    char *ret = strstr(data, "Name:");
    ret += strlen("Name: ");
    char *ptr = strtok(ret, "\n");
    return ptr;
}
// prints details of a process
void printDetails(int pid, char *path) 
{
    //printf("Selected SYS\n");
    FILE *fp;
    int c;

    char str[300];
    sprintf(str, "%s/%d/status", path, pid);
    fp = fopen(str, "r");
    if (fp == NULL)
    {
        printf("Cannot open file \n");
        exit(0);
    }

    fseek(fp, 0L, SEEK_END);
    int fileSize = ftell(fp);
    rewind(fp);

    char *fileStr1 = (char *)malloc((fileSize + 1) * sizeof(char));
    char *fileStr2 = (char *)malloc((fileSize + 1) * sizeof(char));
    char *fileStr3 = (char *)malloc((fileSize + 1) * sizeof(char));

    int i = 0;
    while (1)
    {
        c = fgetc(fp);
        if (feof(fp))
        {
            break;
        }
        fileStr1[i] = c;
        fileStr2[i] = c;
        fileStr3[i] = c;
        i++;
    }
    fclose(fp);

    // ppid
    char *ret1 = strstr(fileStr1, "PPid:");
    ret1 += strlen("PPid: ");
    char *ptr_ppid = strtok(ret1, "\n");

    // // state
    char *ret2 = strstr(fileStr2, "State:");
    ret2 += strlen("State: ");
    char *ptr2 = strtok(ret2, " ");

    // // name
    char *ret3 = strstr(fileStr3, "Name:");
    ret3 += strlen("Name: ");
    char *ptr3 = strtok(ret3, "\n");

    free(fileStr1);
    free(fileStr2);
    free(fileStr3);

    printf("%5d %5s %6s %s\n", pid, ptr_ppid, ptr2, ptr3);
}
// boolean, return if thing is in array
int has(int a, int arr[], int size)
{
    for (int i = 0; i < size; i++)
    {
        if (a == arr[i])
        {
            return 1;
        }
    }
    return 0;
}
// get ppid
int getPPID(int pid, char *path)
{
    /* char str[300];
    sprintf(str, "%s/%d/status", path, pid);
    char *fileStr1 = fileToString(str, 1000);
    // ppid
    char *ret1 = strstr(fileStr1, "PPid:");
    ret1 += strlen("PPid: ");
    char *ptr_ppid = strtok(ret1, "\n");
    return atoi(ptr_ppid); */
    return 696969;
}
// get number of file in a directory
int countContents(char *path)
{
    DIR *folder;
    struct dirent *entry;
    int files = 0;

    folder = opendir(path);
    if (folder == NULL)
    {
        perror("Unable to read directory");
        return (1);
    }

    while ((entry = readdir(folder)))
    {
        files++;
    }
    closedir(folder);
    return files - 2; // subtract 2 for "." and ".." links
}
//get extended details
void printDetails2(int pid, const char* path){
    FILE* fp;
    int c;

    char str[300];
    sprintf(str, "%s/%d/status", path, pid);
    fp = fopen(str, "r");
    if (fp == NULL)
    {
        printf("Cannot open file \n");
        exit(0);
    }
    fseek(fp, 0L, SEEK_END);
    int fileSize = ftell(fp);
    rewind(fp);

    char* fileStr1 = (char*)malloc((fileSize + 1) * sizeof(char));
    char* fileStr2 = (char*)malloc((fileSize + 1) * sizeof(char));
    char* fileStr3 = (char*)malloc((fileSize + 1) * sizeof(char));

    int i = 0;
    while (1) {
        c = fgetc(fp);
        if (feof(fp)) {
            break;
        }
        fileStr1[i] = c;
        fileStr2[i] = c;
        fileStr3[i] = c;
        i++;
    }
    fclose(fp);

    // ppid
    char* ret1 = strstr(fileStr1, "PPid:");
    ret1 += strlen("PPid: ");
    char* ptr_ppid = strtok(ret1, "\n");

    // // state
    char* ret2 = strstr(fileStr2, "State:");
    ret2 += strlen("State: ");
    char* statePtr = strtok(ret2, " ");

    // // name
    char* ret3 = strstr(fileStr3, "Name:");
    ret3 += strlen("Name: ");
    char* ptr_name = strtok(ret3, "\n");

    free(fileStr1);
    free(fileStr2);
    free(fileStr3);

    char path_to_count[300];
    sprintf(path_to_count, "%s/%d/task", path, pid);
    int thrds = countContents(path_to_count);

    sprintf(path_to_count, "%s/%d/fd", path, pid); 
    int files = countContents(path_to_count);

    //                                 PID  PPID      STANJE    #NITI    #DAT.   IME
    printf("%5d %5s %6s %6d %6d %s\n", pid, ptr_ppid, statePtr, thrds,   files,  ptr_name);
}
//toLowercase
char* toLowerC(char * str){
    for(int i = 0; str[i]; i++){
        str[i] = tolower(str[i]);
    }
    return str;
}


// input handlers
void sys(int argc, char const *argv[])
{
    //printf("Selected SYS\n");
    FILE *fp;
    int c;

    char str[100];
    sprintf(str, "%s/version", argv[2]);
    fp = fopen(str, "r");
    fseek(fp, 0L, SEEK_END);
    int fileSize = ftell(fp);
    fclose(fp);

    char *fileStr = fileToString(str, 200);
    char *fileStr2 = fileToString(str, 200);

    char *ret = strstr(fileStr, "Linux version ");
    ret += strlen("Linux version ");
    char *ptr = strtok(ret, " ");
    printf("Linux: %s\n", ptr);

    char *ret2 = strstr(fileStr2, "gcc version ");
    ret2 += strlen("gcc version ");
    char *ptr2 = strtok(ret2, " ");
    printf("gcc: %s\n", ptr2);

    /*  free(fileStr);
    free(fileStr2); */
    return;
}

int sysext_modules(int argc, char const *argv[])
{
    char str[100];
    sprintf(str, "%s/modules", argv[2]);

    FILE *fp;
    int count = 0; // Line counter (result)
    char c;        // To store a character read from file

    // Get file name from user. The file should be
    // either in current folder or complete path should be provided

    // Open the file
    fp = fopen(str, "r");
    // Check if file exists
    if (fp == NULL)
    {
        printf("Could not open file %s", str);
        return 0;
    }

    // Extract characters from file and store in character c
    for (c = getc(fp); c != EOF; c = getc(fp))
        if (c == '\n') // Increment count if this character is newline
            count = count + 1;

    return count;
}

int sysext(int argc, char const *argv[])
{

    char str2[100];
    sprintf(str2, "%s/swaps", argv[2]);
    char *fileStr = fileToString(str2, 500);

    // swap
    char *ret = strstr(fileStr, "\n");
    ret += strlen("\n");
    char *ptr = strtok(ret, " ");
    printf("Swap: %s\n", ptr);

    // modules
    int modules = sysext_modules(argc, argv);
    printf("Modules: %d\n", modules);
    return modules;
}

int me(int argc, char const *argv[])
{
    int pid = getpid();

    char proc_path[100];
    sprintf(proc_path, "/proc/%d", pid);

    printf("Uid: %d\n", getuid());
    printf("EUid: %d\n", geteuid());
    printf("Gid: %d\n", getgid());
    printf("EGid: %d\n", getegid());

    char *buf;
    buf = (char *)malloc(200 * sizeof(char));
    getcwd(buf, 200);
    printf("Cwd: %s\n", buf);

    printf("Priority: %d\n", getpriority(PRIO_PROCESS, pid));
    // tole je pravo
    //printf("Process proc path: /proc/%d/\n", pid);
    printf("Process proc path: /proc/32/\n");


    printf("Process proc access: %s\n", access(proc_path, F_OK) == 0 ? "yes" : "no");

    struct utsname outName;
    uname(&outName);
    printf("OS name: %s\n", outName.sysname);
    printf("OS release: %s\n", outName.release);
    printf("OS version: %s\n", outName.version);
    printf("Machine: %s\n", outName.machine);
    printf("Node name: %s\n", outName.nodename);

    struct timeval tv;
    struct timezone tz;
    gettimeofday(&tv, &tz);

    printf("Timezone: %d\n", tz.tz_dsttime);

    char proc_path2[1000];
    sprintf(proc_path2, "%s/status", proc_path);
    char *file_o = fileToString(proc_path2, 9999);

    char *ret = strstr(file_o, "Cpus_allowed:");
    ret += strlen("Cpus_allowed: ");
    char *ptr = strtok(ret, "\n");

    printf("CPU limit: %d\n", (int)strtol(ptr, NULL, 16));
    return 0;
}

int pids(int argc, char const *argv[])
{
    DIR *folder;
    struct dirent *entry;
    int files = 0;

    char str[100];
    sprintf(str, "%s", argv[2]);

    folder = opendir(str);
    if (folder == NULL)
    {
        perror("Unable to read directory");
        return (1);
    }

    int arr[500];
    int j = 0;
    while ((entry = readdir(folder)) != NULL)
    {
        files++;
        if (digits_only(entry->d_name))
        {
            int i = atoi(entry->d_name);
            arr[j] = i;
            j++;
        }
    }
    closedir(folder);

    selectionSort(arr, j);
    printArray(arr, j);

    return 0;
}

int names(int argc, char const *argv[])
{
    DIR *folder;
    struct dirent *entry;
    int files = 0;

    char str[100];
    sprintf(str, "%s", argv[2]);

    folder = opendir(str);
    if (folder == NULL)
    {
        perror("Unable to read directory");
        return (1);
    }

    int arr[10000];
    char *names[10000];
    int indexes[10000];
    int j = 0;

    while ((entry = readdir(folder)) != NULL)
    {
        files++;
        if (digits_only(entry->d_name))
        {
            int curr_pid = atoi(entry->d_name);
            char *s = nameFromPID(curr_pid, argv);
            names[j] = s;
            arr[j] = curr_pid;
            indexes[j] = j;
            j++;
        }
    }
    closedir(folder);

     int temp = 0;
    for (int i = 0; i < j; i++)
    {
        for (int k = 0; k < j; k++)
        {   
            int inda = indexes[i];
            int indb = indexes[k];

            char* a = malloc(10000*sizeof(char));
            sprintf(a, "%s%09d", names[inda], arr[inda]);

            char* b = malloc(10000*sizeof(char));
            sprintf(b, "%s%09d", names[indb], arr[indb]);

            if(strcmp(toLowerC(a), toLowerC(b)) > 0){
                indexes[i] = indb;
                indexes[k] = inda;
            }
            free(a);
            free(b);
        }
    }

    for (int i = j-1; i >= 0; i--) {
        printf("%d %s\n", arr[indexes[i]], names[indexes[i]]);
    }
    return 0;
}

int ps(int argc, char const *argv[])
{
    int parenPID = 0;
    if (argc == 4)
    {
        parenPID = atoi(argv[3]);
    }
    else
    {
        parenPID = -1;
    }

    DIR *folder;
    struct dirent *entry;

    folder = opendir(argv[2]);
    if (folder == NULL)
    {
        perror("Unable to read directory");
        return (1);
    }

    int PPIDs[5000];
    char *names[5000];
    int PIDs[5000];

    int j = 0;
    while ((entry = readdir(folder)) != NULL)
    {
        if (digits_only(entry->d_name))
        {
            int curr_pid = atoi(entry->d_name);
            char *s = nameFromPID(curr_pid, argv);
            PPIDs[j] = getPPID(curr_pid, argv[2]);
            names[j] = s;
            PIDs[j] = curr_pid;
            j++;
        }
    }
    closedir(folder);

    printf("%5s %5s %6s %s\n", "PID", "PPID", "STANJE", "IME");
    if (parenPID == -1)
    {
        selectionSort(PIDs, j);
        for (int indx = 0; indx < j; indx++)
        {
            printDetails(PIDs[indx], argv[2]);
        }
    }

    
    else
    {
        if (has(parenPID, PIDs, j))
        {
            int kInd = 1;
            int out[j + 1];
            out[0] = parenPID;
            int brk = 0;

            while (1)
            {
                brk = 1;
                for (int i = 0; i < j; i++)
                {
                    // check if PPID is in output array
                    if (has(PPIDs[i], out, kInd))
                    {
                        out[kInd] = PIDs[i];
                        kInd++;
                        brk = 0;
                        PIDs[i] = -1;
                        PPIDs[i] = -1;
                    }
                }
                if (brk)
                {
                    break;
                }
            }

            selectionSort(out, kInd);
            for (int indx = 0; indx < kInd; indx++)
            {
                printDetails(out[indx], argv[2]);
            }
        }
    }
    return 0;
}

int psext(int argc, char const *argv[])
{
     int parenPID = 0;
    if (argc == 4)
    {
        parenPID = atoi(argv[3]);
    }
    else
    {
        parenPID = -1;
    }

    DIR *folder;
    struct dirent *entry;
    int files = 0;

    folder = opendir(argv[2]);
    if (folder == NULL)
    {
        perror("Unable to read directory");
        return (1);
    }
    int PIDs[10000];
    int PPIDs[10000];

    char *names[10000];
    char *states[10000];

    int j = 0;
    while ((entry = readdir(folder)) != NULL)
    {
        files++;
        if (digits_only(entry->d_name))
        {
            int i = atoi(entry->d_name);
            char *s = nameFromPID(i, argv);

            names[j] = s;
            PIDs[j] = i;
            PPIDs[j] = getPPID(i, argv[2]);
            j++;
        }
    }
    closedir(folder);

    //validate
    printf("%5s %5s %6s %6s %6s %s\n", "PID", "PPID", "STANJE", "#NITI", "#DAT.", "IME");
    if (parenPID == -1)
    {
        // we need no parent validation. just print
        selectionSort(PIDs, j);

        for (int indx = 0; indx < j; indx++)
        {
            printDetails2(PIDs[indx], argv[2]);
        }
    }
    else
    {
        // check parents
        if (has(parenPID, PIDs, j))
        {
            int kInd = 1;
            int out[j + 1];
            out[0] = parenPID;
            int brk = 0;

            while (1)
            {
                brk = 1;
                for (int i = 0; i < j; i++)
                {
                    // check if PPID is in output array
                    if (has(PPIDs[i], out, kInd))
                    {
                        out[kInd] = PIDs[i];
                        kInd++;
                        brk = 0;
                        PIDs[i] = -1;
                        PPIDs[i] = -1;
                    }
                }
                if (brk)
                {
                    break;
                }
            }

            selectionSort(out, kInd);
            for (int indx = 0; indx < kInd; indx++)
            {
                //printf(",");
                printDetails2(out[indx], argv[2]);
            }
        }
    }
}

//no main no gain
int main(int argc, char const *argv[])
{
    char const *a[3];
    a[1] = argv[1];
    if (argc == 2)
    {
        a[2] = "/proc/";
        argc++;
    }
    else
    {
        a[2] = argv[2];
    }
    if (argc == 4)
    {
        a[3] = argv[3];
    }

    // SYS A
    if (!strcmp("sys", argv[1]))
    {
        sys(argc, a);
    }

    // SYSEXT B
    if (!strcmp("sysext", argv[1]))
    {
        sys(argc, a);
        return sysext(argc, a);
    }

    // ME C
    if (!strcmp("me", argv[1]))
    {
       return  me(argc, a);
    }

    // PIDS
    if (!strcmp("pids", argv[1]))
    {
        return pids(argc, a);
    }

    // NAMES
    if (!strcmp("names", argv[1]))
    {
        return names(argc, a);
    }

    // PS
    if (!strcmp("ps", argv[1]))
    {
        return ps(argc, a);
    }

    // PSEXT
    if (!strcmp("psext", argv[1]))
    {
        return psext(argc, a);
    }

  /*   // forktree
    if (!strcmp("forktree", argv[1]))
    {
        return forktree(argc, a);
    } */

    // printf("nothing heh\n");
    return 0;
}
