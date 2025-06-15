# Flatpak File Access Tracer

This project contains a Python script (`main.py`) that uses Flatpak and `strace` to run an executable in a contained environment and trace its file access and modifications.

The script runs the provided executable inside a Flatpak sandbox based on `org.gnome.Sdk`. By default, the sandbox filesystem is read-only, except for directories explicitly exposed using the `--filesystem` flag (which the script handles for the executable's directory and any directories specified with `--expose-dir`). It uses `strace` to monitor system calls related to file operations (`file`, `openat`, `write`). The captured `strace` output is then parsed to identify processes and file interactions.

The parsed data is exported into two CSV files:

- `file_access_trace.csv`: Contains the full parsed trace data, including PID, event, details, file path, and modification type.
- `touched_files.csv`: Contains a list of absolute paths of all files that were opened or modified by the executable within the sandbox.

## Example Executables

This project includes two example Go programs:

- `main.go`: A simple program that creates a file named `out.txt` and writes "helo" to it.
- `hard.go`: A program that creates a file named `hard.txt` and then executes the `main` program.

These examples are useful for demonstrating the script's ability to trace file access and modifications, including scenarios involving nested process execution. By tracing `hard`, you can observe both `hard` creating `hard.txt` and `main` (executed by `hard`) creating `out.txt`. This capability is particularly valuable for forensic analysis, allowing you to understand the full chain of file system interactions initiated by a program and its subprocesses within a contained environment.

You can compile these Go programs to create executables to test the Python script.

## Requirements

- flatpak
- uv (as the package manager for Python dependencies)

## Setup

1. Ensure Flatpak is installed on your system.
2. Install the required Flatpak SDK:
  
   ```bash
   flatpak install flathub org.gnome.Sdk//48
   ```

   (Note: The exact branch might vary, `//48` was used in a previous successful installation attempt. If this fails, you may need to search for the correct branch using `flatpak search gnome sdk`).

3. Install Python dependencies using `uv`:

   ```bash
   uv sync
   ```

## Usage

Run the `main.py` script with the path to the executable you want to trace.

```bash
uv run main.py /path/to/your/executable
```

You can also expose additional directories on the host to the Flatpak sandbox using the `--expose-dir` flag. This is necessary if the executable needs to access files or other executables outside of its own directory. You can use this flag multiple times to expose multiple directories.

```bash
uv run main.py /path/to/your/executable --expose-dir /path/to/dir1 --expose-dir /path/to/dir2
```

For example, to trace the `hard` executable (compiled from `hard.go`) and expose the current directory :

```bash
uv run main.py .hard --expose-dir .
```

This will output the raw `strace` trace, a summary of the parsed data, and generate `file_access_trace.csv` and `touched_files.csv` in the directory where you run the command.
