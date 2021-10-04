//new 3
//imports
#include <ctype.h>
#include <dirent.h>
#include <errno.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>
#include <wait.h>

extern int errno;

//helpers definitions
void get_tokens(char buff[], int len, int l);
void shallow_cpy(char in[], char out[], int len);
void process();
void flags();
int cpcat();

// globals
char raw_line[300];
int token_count = 0;
char token_arr[50][500];
char shell_name[100] = "mysh";
int exit_status = 0;

char out_override[100] = "";
char in_ovverride[100] = "";
int bg = 0;

//main
int main(int argc, char const* argv[])
{
    setbuf(stdout, NULL);
    setbuf(stdin, NULL);

    //ignore children and kill zombies
    //signal(SIGCHLD, NULL);

    int char_ind = 0;
    int default_stdout = dup(STDOUT_FILENO);
    int default_stdin = dup(STDIN_FILENO);

    int fd;
    int fdi;

    // main loop
    while (1) {
        if (isatty(STDIN_FILENO)) {
            printf("%s> ", shell_name);
        }

        if (fgets(raw_line, 500, stdin) == NULL) {
            break;
        }
        strcpy(token_arr[0], "#");
        get_tokens(raw_line, strlen(raw_line), 0);
        //flags();

        if (token_count > 0) {
            // OVERRIDE OUTPUT
            if (strlen(out_override) > 0) {
                if ((fd = open(out_override, O_CREAT | O_TRUNC | O_WRONLY, S_IRUSR | S_IWUSR)) < 0) {
                    perror("out override open");
                }
                if (dup2(fd, STDOUT_FILENO) < 0) {
                    perror("out override dup2");
                }
                strcpy(out_override, "");
            } else {
                // a JE TREBA TO CLOSAT MOGOČE BURAZ? NEVEM.... AAAAA
                //close(fd);
            }

            // OVERRIDE INPUT
            if (strlen(in_ovverride) > 0) {
                printf("jebeno");
                if ((fdi = open(in_ovverride, O_RDONLY)) < 0) {
                    perror("in override open");
                }

                if (dup2(fdi, STDIN_FILENO) < 0) {
                    perror("in override dup2");
                }

                // reset override
                strcpy(in_ovverride, "");
            } else {
                //close(fdi);
            }

            if (bg) {
                // running in ng
                int pid = 0;
                if ((pid = fork()) < 0) {
                    perror("fork");
                }

                if (pid == 0) {
                    //child
                    process();
                    exit(0);
                } else {
                    //parent
                }
            } else {
                //dont run in bg
                process();
            }
        }
        bg = 0;
        strcpy(raw_line, "");
        //dup2(default_stdout, STDOUT_FILENO);
        //dup2(default_stdin, STDIN_FILENO);
    }
    return exit_status;
}

// helpers implementation
int name()
{
    if (token_count == 1) {
        printf("%s\n", shell_name);
    } else {
        strcpy(shell_name, token_arr[1]);
    }
    return 0;
}

int help()
{
    printf("Ukazi: \n -name\n -help\n -status\n");
    return 0;
}

int status()
{
    printf("%d\n", exit_status);
    return 0;
}

int print()
{
    for (int i = 1; i < token_count; i++) {
        printf("%s", token_arr[i]);
        if (i != token_count - 1) {
            printf(" ");
        }
    }
    return (0);
}

int echo_m()
{
    for (int i = 1; i < token_count; i++) {
        printf("%s", token_arr[i]);
        if (i != token_count - 1) {
            printf(" ");
        }
    }
    printf("\n");
    return (0);
}

int pid_m()
{
    printf("%d\n", getpid());
    return 0;
}

int ppid_m()
{
    printf("%d\n", getppid());
    return 0;
}

int dir_change(char new_dir[])
{
    if (chdir(new_dir) < 0) {
        exit_status = errno;
        perror("dirchange");
        return exit_status;
    }
    return 0;
}

int dir_where()
{
    char cwdir[500] = "";
    getcwd(cwdir, 500);
    printf("%s\n", cwdir);
    return 0;
}

int dir_make(char new_dir[])
{

    if (mkdir(new_dir, 0777) < 0) {
        exit_status = errno;
        perror("dirmake");
        return exit_status;
    }
    return 0;
}

int dir_remove(char new_dir[])
{
    if (rmdir(new_dir) < 0) {
        exit_status = errno;
        perror("dirremove");
        return exit_status;
    }
    return 0;
}

int dir_list(char new_dir[])
{

    struct dirent* entry;
    DIR* dp;

    if (token_count == 1) {
        char cwdir[500] = "";
        getcwd(cwdir, 500);
        dp = opendir(cwdir);
    } else {
        dp = opendir(new_dir);
    }

    if (dp == NULL) {
        exit_status = errno;
        perror("dirlist");
        return exit_status;
    }

    while ((entry = readdir(dp))) {
        printf("%s  ", entry->d_name);
    }

    printf("\n");

    closedir(dp);
    return 0;
}

int rename_m(char new_dir[], char second_dir[])
{
    if (rename(new_dir, second_dir) < 0) {
        exit_status = errno;
        perror("rename");
        return exit_status;
    }
    return 0;
}

int linkhard_m(char new_dir[], char second_dir[])
{
    if (link(new_dir, second_dir) < 0) {
        exit_status = errno;
        perror("linkhard");
        return exit_status;
    }
    return 0;
}

int linksoft_m(char new_dir[], char second_dir[])
{
    if (symlink(new_dir, second_dir) < 0) {
        exit_status = errno;
        perror("linksoft");
        return exit_status;
    }
    return 0;
}

int linkread_m(char new_dir[])
{
    char buf[1024];
    ssize_t len;
    if ((len = readlink(new_dir, buf, sizeof(buf) - 1)) != -1) {
        buf[len] = '\0';
    } else {
        exit_status = errno;
        perror("linkread");
        return exit_status;
    }
    printf("%s\n", buf);
    return 0;
}

int linklist_m(char new_dir[])
{
    struct dirent* entry;
    DIR* dp;
    int result;
    struct stat str_stat;

    long inode_original;
    if (stat(new_dir, &str_stat) < 0) {
        exit_status = errno;
        perror("linklist");
        return exit_status;
    }
    inode_original = str_stat.st_ino;

    dp = opendir(".");
    if (dp == NULL) {
        exit_status = errno;
        perror("linklist");
        return exit_status;
    }

    while ((entry = readdir(dp))) {
        if (entry->d_ino == inode_original) {
            printf("%s  ", entry->d_name);
        }
    }

    printf("\n");
    closedir(dp);
    return 0;
}

int unlink_m(char new_dir[])
{
    if (unlink(new_dir) < 0) {
        exit_status = errno;
        perror("unlink");
        return exit_status;
    }
    return 0;
}

// aghhh
int open_f(char* name, int type)
{
    int file = open(name, type, 777);

    if (file < 0) {
        exit_status = errno;
        perror(name);
    }
    return file;
}

int cpcat()
{
    int buffs = 10;
    char buff[buffs];
    int readBits;

    int ind = STDIN_FILENO, outd = STDOUT_FILENO;

    if (token_count > 1) {
        if (token_count < 3) {
            ind = open_f(token_arr[1], O_RDONLY);
        } else {
            if (token_arr[2][0] == '-') {
                outd = open_f(token_arr[2], O_CREAT | O_TRUNC | O_WRONLY);
            } else {
                ind = open_f(token_arr[1], O_RDONLY);
                outd = open_f(token_arr[2], O_CREAT | O_TRUNC | O_WRONLY);
            }
        }
    }

    while ((readBits = read(ind, buff, buffs)) > 0) {
        //buff[readBits] = '\0';
        int w = write(outd, buff, readBits);
        if (w < 0) {
            exit_status = errno;
            perror("cpcat");
            return exit_status;
        }
    }

    if (ind != STDIN_FILENO && close(ind) < 0) {
        exit_status = errno;
        perror("cpcat");
        return exit_status;
    }

    if (outd != STDOUT_FILENO && close(outd) < 0) {
        exit_status = errno;
        perror("cpcat");
        return exit_status;
    }
    return 0;
}

int other_calls()
{

    pid_t pid = 0;
    int status;

    pid = fork();
    if (pid == 0) {
        // TO JE CHILD

        char* toks[50];
        int i = 0;
        for (i = 0; i < token_count; i++) {
            toks[i] = token_arr[i];
        }
        toks[i] = NULL;

        execvp(token_arr[0], toks);
        perror("EXEC");
        exit(1);
    }

    if (pid > 0) {
        // TO JE PARENT
        int now_status;
        waitpid(pid, &now_status, 0);
        exit_status = WEXITSTATUS(now_status);
    }

    if (pid < 0) {
        perror("fork");
    }
}

//processes theinput
void process()
{
    //printf("process: %d-%s\n", getpid(), token_arr[0]);

    // NAME
    if (!strcmp(token_arr[0], "name")) {
        exit_status = name();
        return;
    }

    // HELP
    if (!strcmp(token_arr[0], "help")) {
        exit_status = help();
        return;
    }

    // STATUS
    if (!strcmp(token_arr[0], "status")) {
        exit_status = status();
        return;
    }

    // EXIT
    if (!strcmp(token_arr[0], "exit")) {
        exit_status = atoi(token_arr[1]);
        exit(exit_status);
    }

    // PRINT
    if (!strcmp(token_arr[0], "print")) {
        exit_status = print();
        return;
    }

    // ECHO
    if (!strcmp(token_arr[0], "echo")) {
        exit_status = echo_m();
        return;
    }

    // PID
    if (!strcmp(token_arr[0], "pid")) {
        exit_status = pid_m();
        return;
    }

    // PPID
    if (!strcmp(token_arr[0], "ppid")) {
        exit_status = ppid_m();
        return;
    }

    // DIRCHANGE
    if (!strcmp(token_arr[0], "dirchange")) {
        exit_status = dir_change(token_arr[1]);
        return;
    }

    // DIRWHERE
    if (!strcmp(token_arr[0], "dirwhere")) {
        exit_status = dir_where();
        return;
    }

    // DIRMAKE
    if (!strcmp(token_arr[0], "dirmake")) {
        exit_status = dir_make(token_arr[1]);
        return;
    }

    // DIRREMOVE
    if (!strcmp(token_arr[0], "dirremove")) {
        exit_status = dir_remove(token_arr[1]);
        return;
    }

    // DIRLIST
    if (!strcmp(token_arr[0], "dirlist")) {
        exit_status = dir_list(token_arr[1]);
        return;
    }

    // LINKHARD
    if (!strcmp(token_arr[0], "linkhard")) {
        exit_status = linkhard_m(token_arr[1], token_arr[2]);
        return;
    }

    // LINKSOFT
    if (!strcmp(token_arr[0], "linksoft")) {
        exit_status = linksoft_m(token_arr[1], token_arr[2]);
        return;
    }

    // LINKREAD
    if (!strcmp(token_arr[0], "linkread")) {
        exit_status = linkread_m(token_arr[1]);
        return;
    }

    // LINKLIST
    if (!strcmp(token_arr[0], "linklist")) {
        exit_status = linklist_m(token_arr[1]);
        return;
    }

    // UNLINK
    if (!strcmp(token_arr[0], "unlink")) {
        exit_status = unlink_m(token_arr[1]);
        return;
    }

    // RENAME
    if (!strcmp(token_arr[0], "rename")) {
        exit_status = rename_m(token_arr[1], token_arr[2]);
        return;
    }

    // CPCAT
    if (!strcmp(token_arr[0], "cpcat")) {
        exit_status = cpcat();
        return;
    }

    // ALL OTHER ZUNANJI
    exit_status = other_calls();
    return;
}

// tokenizes string and stores result in global variable
void get_tokens(char buff[], int len, int l)
{
    //printf("%s\n", buff);
    int i = 0;
    for (; i < len; i++) {
        if (!isspace(buff[i]))
            break;
    }

    if (i == len || buff[i] == '#') {
        token_count = 0;
        return;
    }

    buff[len] = ' ';

    char curr_char = '\0';
    char prev_char = '\0';
    int space_override = 0;

    char token[200];
    int token_index = 0;

    token_arr[50][500];
    token_count = l;

    for (; i <= len; i++) {
        prev_char = curr_char;
        curr_char = buff[i];

        if ((isspace(curr_char) || curr_char == '\n' || i == len) && !space_override) {
            //we hit a delimiter
            if (isspace(prev_char) || prev_char == '\n') {
                continue;
            }

            //close token_arr token
            token[token_index++] = '\0';
            token_index = 0;

            //add it to array
            strcpy(token_arr[token_count++], token);
        } else {
            if (!space_override && isspace(prev_char) && curr_char == '&') {
                curr_char = ' ';
                bg = 1;
                continue;
            }
            if (!space_override && isspace(prev_char) && (curr_char == '<' || curr_char == '>')) {
                int start = i;
                char a = curr_char;
                for (; i <= len; i++) {
                    prev_char = curr_char;
                    curr_char = buff[i];
                    if (isspace(curr_char) || curr_char == '\n' || curr_char == '\0') {
                        break;
                    }
                    token[-1 + token_index++] = curr_char;
                }
                token[-1 + token_index++] = '\0';
                token_index = 0;
                strcpy(a == '>' ? out_override : in_ovverride, token);
                continue;
            }

            if (curr_char == '"' || curr_char == '\'') {
                space_override = !space_override;
            } else {
                token[token_index++] = curr_char;
            }
        }
    }
    return;
}
