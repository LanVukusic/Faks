# First disk - AAAA

## disk integrity check

after running the command `sha512sum disk_0.iso` we get the following output:

- **d40731a995f6c21ced096c8cd44699fb0c37dbc3938cfe0f9d1ab993a0f973f51900e8f6cd74e2ea0ed3c6b8a74aef4931e23ad03eced4f89af0442a0be34567**

## mounting the image and checking the file system

To mount the disk image and inspect its partitions, the following steps were performed:

1. Attached the ISO to a loop device with partition scanning:
   - Command: `sudo losetup --find --show --partscan disk_0.iso`
   - Output: `/dev/loop0`

2. Listed the filesystems and partition types:
   - Command: `lsblk -f /dev/loop0`
   - Output:
     - `/dev/loop0p1`: ntfs (System Reserved)
     - `/dev/loop0p2`: ntfs

3. Created mount points and mounted both partitions:
   - Commands:
     - `sudo mkdir -p /mnt/disk0p1 /mnt/disk0p2`
     - `sudo mount /dev/loop0p1 /mnt/disk0p1`
     - `sudo mount /dev/loop0p2 /mnt/disk0p2`

4. Verified contents of the partitions:
   - `/mnt/disk0p1` contained boot files and system information.
   - `/mnt/disk0p2` contained Windows system folders and user data.

## findings

Upon manual inspection of the mounted system, the following findings were made:

- In `/mnt/disk0p2/Users/user/Documents`, two documents were identified containing the string "XXXX":
  - `document.odt`
  - `eko_cert.odt`

- In `/mnt/disk0p2/Users/user/Documents/My Pictures` (a symlink to `/mnt/disk0p2/Users/user/Pictures`), the following files were present:
  - `lose_a_file.odt`
  - `confidential-01.jpg`
  - `confidential-04.jpg`

It is notable that `confidential-03.jpg` is missing from the sequence, while `confidential-01.jpg`, `confidential-02.jpg`, and `confidential-04.jpg` exist. A search for deleted files did not yield a recoverable `confidential-03.jpg`, supporting the hypothesis that this file was deleted and is no longer recoverable.

Additionally, the deleted file `weeee_43.gif` was successfully recovered using the `ntfsundelete` tool. The following commands were used for recovery:

```fish
sudo ntfsundelete /dev/loop2p2 | head -n 40
```

```fish
sudo ntfsundelete /dev/loop2p2 -u -m weeee_43.gif
```

Upon analysis, the recovered file was found to contain the target string "XXXX".

- The file `/mnt/disk0p2/Users/user/test.mp4` is a video file that contains the string "XXXX". This was confirmed by searching the file's data.

- The file `/mnt/disk0p2/Users/user/Lie_Detector.mp4` was also examined. No occurrences of the string "XXXX" or any unusual content were found; it appears to be a regular video file with standard metadata.

### Hidden file and data analysis

To determine if any files were intentionally hidden, several forensic techniques were applied:

- Searched for hidden files (dotfiles) in user directories using:

```fish
find /mnt/disk0p2/Users -type f -name '.*'
```

Only a single Firefox metadata file was found; no suspicious hidden files were detected.

- Listed all symbolic links in user directories to check for hidden or redirected locations:

```fish
find /mnt/disk0p2/Users -type l
```

All found symlinks were standard Windows user profile links.

- Checked for extended attributes and alternate data streams (ADS) in Documents and Pictures:

```fish
sudo getfattr -d -m . /mnt/disk0p2/Users/user/Documents/* 2>/dev/null
```

```fish
sudo getfattr -d -m . /mnt/disk0p2/Users/user/Pictures/* 2>/dev/null
```

No extended attributes or ADS were found.

- Used `steghide` to check for steganography in the confidential images:

```fish
steghide info confidential-01.jpg
```

```fish
steghide info confidential-02.jpg
```

```fish
steghide info confidential-04.jpg
```

No hidden data was detected in these images.

Based on these checks, there is no evidence of intentionally hidden files or data using standard filesystem or steganography techniques.

## User activity context

Based on the recovered and analyzed files, it appears the user was engaged in research or activities related to eco-friendly vehicles. This is supported by the presence of a document named `eko_cert.odt` and several confidential images of cars. The context and naming suggest a focus on environmentally conscious or certified vehicles, possibly for documentation or reporting purposes.

### Browser history analysis

The user's browser history was examined for evidence of visited pages and online activity:

- Only Mozilla Firefox user data was present. The Firefox profile contained a history database (`places.sqlite`).
- Querying this database revealed only the default Firefox first-run page and several Mozilla-related URLs, all visited on 2014-02-13. No other browsing activity was recorded.
- No Google Chrome or Microsoft Edge user data or history files were found in the user's AppData directories.

**Conclusion:**
There is no evidence of significant browser activity for this user. The only recorded visits are to Mozilla's own pages during the initial Firefox setup. No other browser logs or history were found.
