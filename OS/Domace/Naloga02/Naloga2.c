#include <dirent.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/resource.h>
#include <sys/time.h>
#include <sys/utsname.h>
#include <unistd.h>
#include <ctype.h>

// helpers

// swaps 2 items
void swap(int* xp, int* yp)
{
    int temp = *xp;
    *xp = *yp;
    *yp = temp;
}
// checks if string is of digits only
int digits_only(const char *s)
{
    while (*s) {
        if (isdigit(*s++) == 0) return 0;
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
    printf("CPU limit: %d\n", 0);
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
            if(digits_only(entry->d_name)){
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

    // SYSEXT
    if (!strcmp("me", argv[1])) {
        return me(argc, argv);
    }

    // SYSEXT
    if (!strcmp("pids", argv[1])) {
        return pids(argc, argv);
    }
    return 0;
}
