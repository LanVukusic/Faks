# Forensic Report: disk_0.vmdk (Converted to ISO)

## Step 1: Disk Image Conversion

- **Command Used:**
  ```fish
  qemu-img convert -O raw disk_0.vmdk disk_0.iso
  ```
- **Result:**
  - `disk_0.vmdk` (4.6G) was successfully converted to `disk_0.iso` (40G) using `qemu-img`.
  - Both files are present in the `DDDD` directory.

## Step 2: Disk Integrity Check

- **Command Used:**
  ```fish
  sha512sum disk_0.iso
  ```
- **Result:**
  - SHA512: 31b61436c9841fc91b5a08aec9943a2d6049098d96c5d132ae1859dcda7808fffc8030bf3bc1652aa727ccf30c8de2788a1178351f620b2b821e59e6cb131919

## Step 3: Mounting the Image and Checking the File System

- **Command Used:**
  ```fish
  sudo losetup --find --show --partscan disk_0.iso
  ```
- **Result:**
  - Output: `/dev/loop1`

- **Command Used:**
  ```fish
  lsblk -f /dev/loop1
  ```
- **Result:**
  - `/dev/loop1p1`: ext4
  - `/dev/loop1p2`: (no filesystem detected)
  - `/dev/loop1p5`: swap

## Deleted files recovery (extundelete)

- The ext4 partition was scanned for deleted files using the following commands:
  ```fish
  sudo umount /mnt/disk3p1
  sudo extundelete /dev/loop1p1 --restore-all | tee DDDD_extundelete/extundelete.log
  ```
- Many deleted inodes were found, but most could not be recovered because their space had been reallocated.
- The majority of recoverable files were system, configuration, or package management files (e.g., in `/etc`, `/usr`, `/var`, and `lost+found`).
- No significant user documents, images, or files of interest were recovered from deleted space.
- This suggests that if any user files were deleted with the intent to hide them, they are no longer recoverable from the filesystem.

## Findings

Upon manual inspection of the mounted system, the following findings were made:

- In `/mnt/disk3p1/home/user`:
  - `test.mp4` — contains the string **XXXX**

- In `/mnt/disk3p2/home/user/documents`:
  - `document.odt` — contains **XXXX**
  - `eko_cert.odt` — contains **XXXX**
  - `jaz_nisem_nic_posebnega.odt` — contains descriptions on acquiring the confidential blueprints present in the report for disk AAAA

- In `/mnt/disk3p1/home/user/Pictures`:
  - `chat_systems_2x.png` — contains **xxxxKCD** (not exactly "XXXX" but reported for completeness)
  - `weeee_43.gif` — contains **XXXX**
  - `xxxx.jpg` — contains **XXXX**
  - `SabOnline10.GIF` — contains **XXXX**
  - `lorem.doc` — contains **XX XXX XXXX**

These files were identified as containing the target string or related content, or were otherwise relevant to the investigation.

## EXIF metadata analysis

- The following command was used to check for the presence of 'xxxx' or 'XXXX' in the EXIF metadata of all image files in the Pictures directory:
  ```fish
  exiftool /mnt/disk3p1/home/user/Pictures/*.jpg | grep -i xxxx
  exiftool /mnt/disk3p1/home/user/Pictures/*.png | grep -i xxxx
  exiftool /mnt/disk3p1/home/user/Pictures/*.gif | grep -i xxxx
  ```
- **Result:**
  - No occurrences of 'xxxx' or 'XXXX' were found in the EXIF metadata of any image files.
  - All references to 'xxxx' or 'XXXX' in these files are present in the file content itself, not in the metadata.

- The same EXIF/metadata search was performed on the video file `What If Wild Animals Ate Fast Food.flv`:
  ```fish
  exiftool /mnt/disk3p1/home/user/'What If Wild Animals Ate Fast Food.flv' | grep -i xxxx
  ```
- **Result:**
  - No occurrences of 'xxxx' or 'XXXX' were found in the metadata of this video file.

## SSH keys and keyring analysis

- The following directories were checked for SSH keys and keyring files:
  - `/mnt/disk3p1/home/user/.ssh`
  - `/mnt/disk3p1/home/user/.gnupg`
  - `/mnt/disk3p1/home/user/.local/share/keyrings`
- **Result:**
  - No SSH keys, GPG keys, or keyring files of interest were found in these locations.
  - There is no evidence of stored SSH private keys or exported keyring secrets for this user.

## Browser history analysis

- The user's home directory was checked for browser data and history files (e.g., Firefox, Chrome, Edge profiles).
- **Result:**
  - No browser history databases or user browser data were found for any browser.
  - There is no evidence of web browsing activity for this user on the system.
