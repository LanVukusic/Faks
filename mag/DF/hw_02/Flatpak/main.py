import subprocess
import sys
import argparse
import tempfile
import os
import re
import pandas as pd

def parse_strace_output(strace_output):
    """
    Parses the strace output to extract process and file access information.

    Args:
        strace_output (str): The raw strace output string.

    Returns:
        pd.DataFrame: A DataFrame containing the parsed information.
    """
    lines = strace_output.strip().split('\n')
    data = []
    pid = None

    for line in lines:
        # Extract PID if present at the start of the line
        pid_match = re.match(r'^\[pid\s*(\d+)\]', line)
        if pid_match:
            pid = int(pid_match.group(1))
            # Remove PID part from the line for easier parsing of the call
            call_line = line[pid_match.end():].strip()
        else:
            # If no PID at the start, assume it's the same as the previous line's PID
            call_line = line.strip()

        # Parse execve calls
        execve_match = re.match(r'execve\("(.*?)",\s*\[(.*?)\]', call_line)
        if execve_match:
            executable = execve_match.group(1)
            args = [arg.strip('"') for arg in execve_match.group(2).split(', ')]
            data.append({'PID': pid, 'Event': 'execve', 'Details': f'Executable: {executable}, Args: {args}', 'File': None, 'Modification': None})
            continue

        # Parse openat calls
        openat_match = re.match(r'openat\(.*?"(.*?)"', call_line)
        if openat_match:
            file_path = openat_match.group(1)
            data.append({'PID': pid, 'Event': 'openat', 'Details': call_line, 'File': file_path, 'Modification': 'Open'})
            continue

        # Parse write calls
        write_match = re.match(r'write\(.*?, "(.*?)", \d+\)', call_line)
        if write_match:
            content_preview = write_match.group(1)
            data.append({'PID': pid, 'Event': 'write', 'Details': call_line, 'File': None, 'Modification': f'Write (content preview: "{content_preview}...")'})
            continue
            
        # Parse exit calls
        exit_match = re.match(r'\+\+\+ exited with (\d+) \+\+\+', call_line)
        if exit_match:
            exit_code = exit_match.group(1)
            data.append({'PID': pid, 'Event': 'exit', 'Details': f'Exited with code {exit_code}', 'File': None, 'Modification': None})
            continue


    df = pd.DataFrame(data)
    return df


def run_in_flatpak_with_trace(executable_path, extra_exposed_dirs=None):
    """
    Runs the given executable inside a Flatpak sandbox (org.gnome.Sdk)
    with strace tracing file access and modifications.

    Args:
        executable_path (str): Path to the executable on the host.
        extra_exposed_dirs (list of str): Additional directories to expose inside sandbox.

    Returns:
        str: Captured strace output showing file accesses.
    """
    if extra_exposed_dirs is None:
        extra_exposed_dirs = []

    # Get the directory of the executable
    executable_dir = os.path.dirname(os.path.abspath(executable_path))
    executable_basename = os.path.basename(executable_path)

    # Prepare flatpak run command with filesystem exposes
    # Expose the executable's directory and any extra exposed directories
    fs_args = [f'--filesystem={executable_dir}']
    for d in extra_exposed_dirs:
        fs_args += [f'--filesystem={os.path.abspath(d)}']

    # strace command to trace file open/write calls
    # Run the executable using its path relative to the exposed directory
    strace_cmd = [
        'strace', '-f', '-e', 'trace=file,openat,write',
        f'./{executable_basename}'
    ]

    # Full flatpak run command
    flatpak_cmd = [
        'flatpak', 'run', '--devel'
    ] + fs_args + ['--command=bash', 'org.gnome.Sdk']

    # Create a bash script to run strace on executable inside sandbox
    # Change directory to the executable's directory within the sandbox
    bash_script = f"""
cd {executable_dir}
{ ' '.join(strace_cmd) }
"""

    # Run flatpak with bash -c and capture output
    proc = subprocess.run(flatpak_cmd + ['-c', bash_script],
                          capture_output=True, text=True)

    return proc.stdout + proc.stderr

if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Run an executable in a Flatpak sandbox with strace file access tracing.')
    parser.add_argument('executable_path', help='Path to the executable on the host.')
    parser.add_argument('--expose-dir', action='append', default=[],
                        help='Directory on the host to expose inside the sandbox. Can be used multiple times.')

    args = parser.parse_args()

    output = run_in_flatpak_with_trace(args.executable_path, args.expose_dir)

    print("=== File access trace output ===")
    print(output)

    # Parse the strace output
    parsed_data = parse_strace_output(output)

    # Print parsed data summary
    print("\n=== Parsed Data Summary ===")
    print("Processes:")
    for pid in parsed_data['PID'].unique():
        print(f"- PID: {pid}")

    print("\nFile Accesses and Modifications:")
    for index, row in parsed_data[parsed_data['File'].notna() | parsed_data['Modification'].notna()].iterrows():
         print(f"- PID: {row['PID']}, Event: {row['Event']}, File: {row['File']}, Modification: {row['Modification']}")


    # Export full trace data to CSV
    csv_output_path = "file_access_trace.csv"
    parsed_data.to_csv(csv_output_path, index=False)
    print(f"\nFull trace data exported to {csv_output_path}")

    # Filter for touched files and export absolute paths to a new CSV
    touched_files_df = parsed_data[parsed_data['File'].notna()].copy()
    # Convert relative paths to absolute paths if necessary (assuming paths in strace are relative to the sandbox's current dir)
    # This might need adjustment based on how strace reports paths and how the sandbox is configured.
    # For now, assuming they are relative to the sandbox's working directory which is the executable's directory.
    # A more robust solution might involve resolving paths against the exposed directories.
    touched_files_df['Absolute File'] = touched_files_df['File'].apply(lambda x: os.path.abspath(os.path.join(os.path.dirname(os.path.abspath(args.executable_path)), x)))


    touched_files_csv_path = "touched_files.csv"
    touched_files_df['Absolute File'].dropna().to_csv(touched_files_csv_path, index=False, header=['Absolute File Path'])
    print(f"Touched file paths exported to {touched_files_csv_path}")
