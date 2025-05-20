# Second disk - BBBB

## disk integrity check

After running the command `sha512sum disk_0.vmdk` we get the following output:

- **d68b6f9a2dfe2ab74bf93830fae1334ab5fbd649c30f031dfc273969aa24408fab6beec6dc751fba7f84c43ec9d6eb9697baf140d914540cebfcc550778a4a43**

## mounting the image and checking the file system

To mount the disk image and inspect its partitions, the following steps should be performed:

1. Attach the VMDK to a loop device with partition scanning:

   - Command: `sudo losetup --find --show --partscan disk_0.vmdk`
   - Output: `/dev/loopX`

2. List the filesystems and partition types:

   - Command: `lsblk -f /dev/loopX`
   - Output:
     - `/dev/loopXp1`: ext4
     - `/dev/loopXp2`: (no filesystem detected)
     - `/dev/loopXp5`: swap

3. Create mount points and mount the partitions:

   - Commands:
     - `sudo mkdir -p /mnt/disk1p1 /mnt/disk1p2 ...`
     - `sudo mount /dev/loopXp1 /mnt/disk1p1`
     - `sudo mount /dev/loopXp2 /mnt/disk1p2`

4. Verify contents of the partitions:

   - `/mnt/disk1p1` ...
   - `/mnt/disk1p2` ...

## partition analysis

The disk image contains three partitions as detected by `lsblk`:

- `/dev/loopXp1`: ext4 filesystem (Linux data partition)
- `/dev/loopXp2`: no filesystem detected (likely an extended partition, used as a container for logical partitions)
- `/dev/loopXp5`: swap partition (Linux swap space)

### Analysis of `/dev/loopXp2`

- This partition is of type 'extended', which means it does not contain a filesystem or user data directly. Instead, it serves as a container for logical partitions (in this case, `/dev/loopXp5`).
- Hexdump and file analysis of `/dev/loopXp2` show only partition table/MBR data, with no evidence of a filesystem or user data.
- No readable data or files were found in `/dev/loopXp2` using `strings` or file carving tools.

### extundelete recovery results

- extundelete was run after unmounting the partition and running fsck to ensure filesystem integrity.
- The tool recovered a number of system and configuration files (mainly in bin, boot, etc, lib, and related subdirectories), but **no user files or documents were recovered** from /home/user or any other user data location.
- The recovered files are mostly system scripts, configuration files, and a kernel image, with no evidence of user-created content or personal data.
- This suggests that either no user files ever existed, or they were securely deleted/overwritten before imaging.

### Search for user documents, images, and videos

- A recursive search was performed for common user file types, including images (jpg, png, gif, bmp), OpenDocument files (.odt), and video files (mp4, avi, mov, mkv), in both the BBBB directory and the RECOVERED_FILES directory.
- **No such files were found** in the live filesystem or among the recovered files.
- This confirms that there are no user-created images, documents, or videos present on the disk image or recoverable from deleted space.

### Search for deleted or hidden data in unallocated space

- A search for the string "XXXX" and other user data remnants was performed directly on the raw ext4 partition device using `strings` and `grep`.
- The results consisted only of generic or random matches, with no evidence of user documents, images, or videos containing "XXXX" or any meaningful user content.
- No user data was found in unallocated space, supporting the conclusion that either no user files ever existed or all user data was securely deleted/overwritten before imaging.

### Browser history and user activity analysis

- No browser history or user browsing data was found on the disk image.
- The `/home/user` directory, where browser profiles and history databases (such as `.mozilla/`, `.config/google-chrome/`, or similar) would normally reside, is empty in both the mounted partition and the RECOVERED_FILES directory from extundelete.
- No browser profile folders or history databases (e.g., `places.sqlite` for Firefox or `History` for Chrome) were found among the recovered files.
- Only system and configuration files were recovered; no user-level application data, including browser data, was present.
- This suggests that either no browsers were used, or all user data (including browser history) was securely deleted or never created.

### System identification

- The system is based on **Ubuntu 14.04.4 LTS (Trusty Tahr)**, as indicated by the contents of `/etc/apt/sources.list` and repository comments.

### Conclusion

- All user data is expected to reside on `/dev/loopXp1` (ext4). The swap partition (`/dev/loopXp5`) is not expected to contain persistent user files.
- No hidden or encrypted filesystems were detected in `/dev/loopXp2`.

## findings

(Proceed with the same forensic methodology as for the first disk: search for files containing 'XXXX', check for deleted/hidden files, analyze user activity, and summarize browser history and user context.)

### Overall Summary for Disk BBBB

Based on the forensic analysis of the disk BBBB image, the following conclusions were reached:

- The disk image integrity was verified using SHA512.
- The partition analysis identified an ext4 data partition, an extended partition, and a swap partition. The extended partition contained no user data.
- A thorough examination of the mounted ext4 data partition revealed no user files or documents in standard user directories (e.g., /home/user).
- Checks for hidden files, unusual file attributes, and alternate data streams yielded no evidence of concealed data.
- Attempts to recover deleted files using extundelete only yielded system and configuration files; no user files were recovered.
- Recursive searches for common user file types (images, .odt documents, videos) found no such files on the disk image or among recovered files.
- Analysis for browser history and user activity found no evidence of browsing data or user-specific application activity. There was one zippeddocument found in the home/documents directory, but it was just a document about printing.
- I assume that the user did not use the system for any personal activities, as no user data was found in the filesystem or among recovered files.

**Conclusion:** The forensic analysis of disk BBBB found no evidence of user activity or user-created data. The filesystem appears to contain only a minimal operating system installation with no personal files or browsing history.
