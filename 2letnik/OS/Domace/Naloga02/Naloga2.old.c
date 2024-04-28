#include <ctype.h>
#include <dirent.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/resource.h>
#include <sys/time.h>
#include <sys/utsname.h>
#include <unistd.h>

//svn
//svn checkout https://lalgec.fri.uni-lj.si/os/lv8541/Naloga2


// helpers

// swaps 2 items
void swap(int* xp, int* yp)
{
    int temp = *xp;
    *xp = *yp;
    *yp = temp;
}
// checks if string is of digits only
int digits_only(const char* s)
{
    while (*s) {
        if (isdigit(*s++) == 0)
            return 0;
    }

    return 1;
}
// Function to perform Selection Sort
void selectionSort(int arr[], int n)
{
    int i, j, min_idx;

    // One by one move boundary of unsorted subarray
    for (i = 0; i < n - 1; i++) {

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
// file to string
const char* fileToString(const char* path)
{
    FILE* fp;
    int c;

    fp = fopen(path, "r");
    fseek(fp, 0L, SEEK_END);
    int fileSize = ftell(fp);
    rewind(fp);

    char* fileStr = (char*)malloc((fileSize + 1) * sizeof(char));
    int i = 0;
    while (1) {
        c = fgetc(fp);
        if (feof(fp)) {
            break;
        }
        fileStr[i] = c;
        i++;
    }
    fclose(fp);

    return fileStr;
}
// reads process file and gets name out
const char* nameFromPID(int pid, char const* argv[])
{
    char path[200];
    sprintf(path, "%s/%d/status", argv[2], pid);
    const char* data = fileToString(path);
    //printf("TEST %s\n", data);
    char* ret = strstr(data, "Name:");
    ret += strlen("Name: ");
    char* ptr = strtok(ret, "\n");
    return ptr;
}
// prints details of a process
void printDetails(int pid, const char* path){
    //printf("Selected SYS\n");
    FILE* fp;
    int c;

    char str[300];
    sprintf(str, "%s%d/status", path, pid);
    fp = fopen(str, "r");
    if (fp == NULL)
    {
        printf("Cannot open file \n");
        exit(0);
    }
    //  // Read contents from file
    // c = fgetc(fp);
    // while (c != EOF)
    // {
    //     printf ("%c", c);
    //     c = fgetc(fp);
    // }
  
    // fclose(fp);
    fseek(fp, 0L, SEEK_END);
    int fileSize = ftell(fp);
    rewind(fp);
    // c = fgetc(fp);
    // while (c != EOF)
    // {
    //     printf ("%c", c);
    //     c = fgetc(fp);
    // }

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
    char* ptr2 = strtok(ret2, " ");

    // // name
    char* ret3 = strstr(fileStr3, "Name:");
    ret3 += strlen("Name: ");
    char* ptr3 = strtok(ret3, "\n");

    free(fileStr1);
    free(fileStr2);
    free(fileStr3);

    printf("%5d %5s %6s %s\n", pid, ptr_ppid, ptr2, ptr3);

}
// boolean, return if thing is in array
int has (int a, int arr[], int size)
{
    for (int i = 0; i < size; i++)
    {
        if(a == arr[i]){
            return 1;
        }
    }
    return 0;
}
// get ppid
int getPPID(int pid, const char* path){
    //printf("Selected SYS\n");
    FILE* fp;
    int c;

    char str[300];
    sprintf(str, "%s%d/status", path, pid);
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

    int i = 0;
    while (1) {
        c = fgetc(fp);
        if (feof(fp)) {
            break;
        }
        fileStr1[i] = c;
        i++;
    }
    fclose(fp);

    // ppid
    char* ret1 = strstr(fileStr1, "PPid:");
    ret1 += strlen("PPid: ");
    char* ptr_ppid = strtok(ret1, "\n");
    free(fileStr1);
    return atoi(ptr_ppid);
}
// get number of file in a directory
int countContents(const char* path){
    DIR* folder;
    struct dirent* entry;
    int files = 0;


    folder = opendir(path);
    if (folder == NULL) {
        perror("Unable to read directory");
        return (1);
    }

    while ((entry = readdir(folder))) {
        files++;
    }
    closedir(folder);
    return files-2; // subtract 2 for "." and ".." links
}
//get extended details
void printDetails2(int pid, const char* path){
    FILE* fp;
    int c;

    char str[300];
    sprintf(str, "%s%d/status", path, pid);
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
    sprintf(path_to_count, "%s%d/task", path, pid);
    int thrds = countContents(path_to_count);

    sprintf(path_to_count, "%s%d/fd", path, pid);
    int files = countContents(path_to_count);

    //                                 PID  PPID      STANJE    #NITI    #DAT.   IME
    printf("%5d %5s %6s %6d %6d %s\n", pid, ptr_ppid, statePtr, thrds,   files,  ptr_name);
}


// input handlers
void sys(int argc, char const* argv[])
{
    //printf("Selected SYS\n");
    FILE* fp;
    int c;

    char str[100];
    sprintf(str, "%s/version", argv[2]);
    fp = fopen(str, "r");
    fseek(fp, 0L, SEEK_END);
    int fileSize = ftell(fp);
    rewind(fp);

    char* fileStr = (char*)malloc((fileSize + 1) * sizeof(char));
    char* fileStr2 = (char*)malloc((fileSize + 1) * sizeof(char));

    int i = 0;
    while (1) {
        c = fgetc(fp);
        if (feof(fp)) {
            break;
        }
        fileStr[i] = c;
        fileStr2[i] = c;
        i++;
    }
    fclose(fp);

    char* ret = strstr(fileStr, "Linux version ");
    ret += strlen("Linux version ");
    char* ptr = strtok(ret, " ");
    printf("Linux: %s\n", ptr);

    char* ret2 = strstr(fileStr2, "gcc version ");
    ret2 += strlen("gcc version ");
    char* ptr2 = strtok(ret2, " ");
    printf("gcc: %s\n", ptr2);
}

int sysext_modules(int argc, char const* argv[])
{
    FILE* fp;
    int c;

    char str[100];
    sprintf(str, "%s/modules", argv[2]);
    fp = fopen(str, "r");
    fseek(fp, 0L, SEEK_END);
    int fileSize = ftell(fp);
    rewind(fp);

    char* fileStr = (char*)malloc((fileSize + 1) * sizeof(char));

    int i = 0;
    while (1) {
        c = fgetc(fp);
        if (feof(fp)) {
            break;
        }
        fileStr[i] = c;
        i++;
    }
    fclose(fp);

    /* get the first token */
    char* token = strtok(fileStr, "\n");
    int a = 0;

    /* walk through other tokens */
    while (token != NULL) {
        token = strtok(NULL, "\n");
        a++;
    }
    return a;
}

int sysext(int argc, char const* argv[])
{
    //printf("Selected SYS\n");
    FILE* fp;
    int c;

    char str[100];
    sprintf(str, "%s/swaps", argv[2]);
    fp = fopen(str, "r");
    fseek(fp, 0L, SEEK_END);
    int fileSize = ftell(fp);
    rewind(fp);

    char* fileStr = (char*)malloc((fileSize + 1) * sizeof(char));

    int i = 0;
    while (1) {
        c = fgetc(fp);
        if (feof(fp)) {
            break;
        }
        fileStr[i] = c;
        i++;
    }
    fclose(fp);

    // swap
    char* ret = strstr(fileStr, "\n");
    ret += strlen("\n");
    char* ptr = strtok(ret, " ");
    printf("Swap: %s\n", ptr);

    // modules
    int modules = sysext_modules(argc, argv);
    printf("Modules: %d\n", modules);
    return modules;
}

int me(int argc, char const* argv[])
{
    int pid = getpid();

    char proc_path[100];
    sprintf(proc_path, "/proc/%d", pid);

    printf("Uid: %d\n", getuid());
    printf("EUid: %d\n", geteuid());
    printf("Gid: %d\n", getgid());
    printf("EGid: %d\n", getegid());

    char* buf;
    buf = (char*)malloc(200 * sizeof(char));
    getcwd(buf, 200);
    printf("Cwd: %s\n", buf);

    printf("Priority: %d\n", getpriority(PRIO_PROCESS, pid));
    printf("Process proc path: %s\n", proc_path);
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
    const char* file_o = fileToString(proc_path2);

    char* ret = strstr(file_o, "Cpus_allowed:");
    ret += strlen("Cpus_allowed: ");
    char* ptr = strtok(ret, "\n");

    printf("CPU limit: %d\n", (int)strtol(ptr, NULL, 16));
}

int pids(int argc, char const* argv[])
{
    DIR* folder;
    struct dirent* entry;
    int files = 0;

    char str[100];
    sprintf(str, "%s", argv[2]);

    folder = opendir(str);
    if (folder == NULL) {
        perror("Unable to read directory");
        return (1);
    }

    int arr[500];
    int j = 0;
    while ((entry = readdir(folder))) {
        files++;
        if (entry->d_type == DT_DIR) {
            if (digits_only(entry->d_name)) {
                //printf("--%s--", entry->d_name);
                int i = atoi(entry->d_name);
                //printf("%d--\n", i);

                arr[j] = i;
                j++;
            }
        }
    }

    selectionSort(arr, j);
    printArray(arr, j);

    closedir(folder);
    return 0;
}

int names(int argc, char const* argv[])
{
    DIR* folder;
    struct dirent* entry;
    int files = 0;

    char str[500];
    sprintf(str, "%s", argv[2]);

    folder = opendir(str);
    if (folder == NULL) {
        perror("Unable to read directory");
        return (1);
    }
    int arr[10000];
    const char* names[10000];
    int indexes[10000];

    int j = 0;
    while ((entry = readdir(folder))) {
        files++;
        if (entry->d_type == DT_DIR) {
            if (digits_only(entry->d_name)) {
                int i = atoi(entry->d_name);
                const char* s = nameFromPID(i, argv);
                names[j] = s;
                //printf("%s\n", s);
                arr[j] = i;
                indexes[j] = j;
                j++;
            }
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
            sprintf(a, "%s%d", names[inda], arr[inda]);

            char* b = malloc(10000*sizeof(char));
            sprintf(b, "%s%d", names[indb], arr[indb]);

            if(strcmp(a,b) > 0){
                indexes[i] = indb;
                indexes[k] = inda;
            }
            free(a);
            free(b);
        }
    }

    int indext = 0;

    for (int i = j-1; i >= 0; i--) {
        printf("%d %s\n", arr[indexes[i]], names[indexes[i]]);
    }



    return 0;
}

int ps(int argc, char const* argv[]){
    int parenPID = 0;
    if(argc == 4){
        parenPID = atoi(argv[3]);
    }else{
        parenPID = -1;
    }

    DIR* folder;
    struct dirent* entry;
    int files = 0;

    char str[500];
    sprintf(str, "%s", argv[2]);

    folder = opendir(str);
    if (folder == NULL) {
        perror("Unable to read directory");
        return (1);
    }
    int PIDs[10000];
    int PPIDs[10000];

    const char* names[10000];
    const char* states[10000];

    int j = 0;
    while ((entry = readdir(folder))) {
        files++;
        if (entry->d_type == DT_DIR) {
            if (digits_only(entry->d_name)) {
                int i = atoi(entry->d_name);
                const char* s = nameFromPID(i, argv);

                names[j] = s;
                PIDs[j] = i;
                //printf(".");
                PPIDs[j] = getPPID(i, argv[2]);
                j++;
            }
        }
    }
    closedir(folder);

    //validate
    printf("%5s %5s %6s %s\n", "PID", "PPID", "STANJE", "IME");
    if(parenPID == -1){
        // we need no parent validation. just print
        selectionSort(PIDs, j);

        for (int indx = 0; indx < j; indx++){
            printDetails(PIDs[indx], argv[2]);
        }
        
    }else{
        // check parents
        if (has(parenPID, PIDs, j)){
            int kInd = 1;
            int out[j+1];
            out[0] = parenPID;
            int brk = 0;

            while (1)
            {
                brk = 1;
                for (int i = 0; i < j; i++)
                {   
                    // check if PPID is in output array
                    if(has(PPIDs[i], out, kInd)){
                        out[kInd] = PIDs[i];
                        kInd++;
                        brk = 0;
                        PIDs[i] = -1;
                        PPIDs[i] = -1;
                    }
                }
                if(brk){
                    break;
                }
            }

            selectionSort(out, kInd);
            for (int indx = 0; indx < kInd; indx++){
                //printf(",");
                printDetails(out[indx], argv[2]);
            }
        }
    }

}

int psext(int argc, char const* argv[]){
    int parenPID = 0;
    if(argc == 4){
        parenPID = atoi(argv[3]);
    }else{
        parenPID = -1;
    }

    DIR* folder;
    struct dirent* entry;
    int files = 0;

    char str[500];
    sprintf(str, "%s", argv[2]);

    folder = opendir(str);
    if (folder == NULL) {
        perror("Unable to read directory");
        return (1);
    }
    int PIDs[10000];
    int PPIDs[10000];

    const char* names[10000];
    const char* states[10000];

    int j = 0;
    while ((entry = readdir(folder))) {
        files++;
        if (entry->d_type == DT_DIR) {
            if (digits_only(entry->d_name)) {
                int i = atoi(entry->d_name);
                const char* s = nameFromPID(i, argv);

                names[j] = s;
                PIDs[j] = i;
                //printf(".");
                PPIDs[j] = getPPID(i, argv[2]);
                j++;
            }
        }
    }
    closedir(folder);

    //validate
    printf("%5s %5s %6s %6s %6s %s\n", "PID", "PPID", "STANJE", "#NITI", "#DAT.", "IME");
    if(parenPID == -1){
        // we need no parent validation. just print
        selectionSort(PIDs, j);

        for (int indx = 0; indx < j; indx++){
            printDetails2(PIDs[indx], argv[2]);
        }
        
    }else{
        // check parents
        if (has(parenPID, PIDs, j)){
            int kInd = 1;
            int out[j+1];
            out[0] = parenPID;
            int brk = 0;

            while (1)
            {
                brk = 1;
                for (int i = 0; i < j; i++)
                {   
                    // check if PPID is in output array
                    if(has(PPIDs[i], out, kInd)){
                        out[kInd] = PIDs[i];
                        kInd++;
                        brk = 0;
                        PIDs[i] = -1;
                        PPIDs[i] = -1;
                    }
                }
                if(brk){
                    break;
                }
            }

            selectionSort(out, kInd);
            for (int indx = 0; indx < kInd; indx++){
                //printf(",");
                printDetails2(out[indx], argv[2]);
            }
        }
    }

}


//no main no gain
int main(int argc, char const* argv[])
{
    // SYS
    if (!strcmp("sys", argv[1])) {
        sys(argc, argv);
    }

    // SYSEXT
    if (!strcmp("sysext", argv[1])) {
        sys(argc, argv);
        return sysext(argc, argv);
    }

    // ME
    if (!strcmp("me", argv[1])) {
        return me(argc, argv);
    }

    // PIDS
    if (!strcmp("pids", argv[1])) {
        return pids(argc, argv);
    }

    // NAMES
    if (!strcmp("names", argv[1])) {
        return names(argc, argv);
    }

    // PS
    if (!strcmp("ps", argv[1])) {
        return ps(argc, argv);
    }

    // PSEXT
    if (!strcmp("psext", argv[1])) {
        return psext(argc, argv);
    }

    return 0;
}
