import java.io.*;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;

class LLNode {
    public int id;
    public int index;
    public LLNode next;
    public LLNode prev;

    public  LLNode(int id) {
        this.id = id;
    }
}

class Disk {
    public int length;
    public LLNode head;
    public LLNode ids;

    public Disk() {
    }

    public void Init(int fieldLength) {
        this.length = fieldLength;
        this.AddID(-1);
        // create fieldđ
        this.head = new LLNode(-1);
        LLNode temp = this.head;
        for (int i = 0; i < fieldLength - 1; i++) {
            temp.next = new LLNode(-1);
            temp.next.prev = temp;
            temp = temp.next;
            temp.index = i + 1;
        }
    }

    public boolean alloc(int size, int id) {
        // if id is not in already
        if (!this.HasID(id)) {
            // find first open slot
            LLNode temp = this.head;
            int s = 0;
            do {
                if (temp.id == -1) { // we have a free spot
                    s++;
                    if (s == size) {
                        // alocate
                        for (int i = 0; i < size; i++) {
                            temp.id = id;
                            temp = temp.prev;
                        }
                        this.AddID(id);
                        return true;
                    }
                } else {
                    s = 0;
                }
                temp = temp.next;
            } while (temp != null);
            return false; // we have reached the end with no slot
        } else { // id is in
            return false;
        }
    }

    public int Free(int id) {
        if (!this.HasID(id)) {
            //System.out.println("nemorm frijat");
            return 0;
        }
        int releasedBytes = 0;

        LLNode temp = this.head;
        do {
            if (temp.id == id) {
                do {
                    temp.id = -1;
                    releasedBytes++;
                    temp = temp.next;
                } while (temp != null && temp.id == id);
                this.RemoveID(id);
                return releasedBytes;
            }
            temp = temp.next;
        } while (temp != null);
        this.RemoveID(id);
        return releasedBytes;
    }

    public void AddID(int id) {
        if (this.ids == null) {
            this.ids = new LLNode(id);
        } else {
            LLNode temp = new LLNode(id);
            temp.next = this.ids;
            this.ids = temp;
        }
    }

    public void RemoveID(int id) {
        if(this.ids == null){
            return;
        }
        LLNode temp = this.ids;
        do {
            if (temp.id == id) {
                temp.id = -1;
            }
            temp = temp.next;
        } while (temp != null);
    }

    public boolean HasID(int id) {
        LLNode temp = this.ids;
        do {
            if (temp.id == id) {
                return true;
            }
            temp = temp.next;
        } while (temp != null);
        return false;
    }

    public void PrintDisk() {
        LLNode temp = this.head;
        do {
            // System.out.print(""+temp.id+":"+temp.index+":"+(temp.next == null ||
            // temp.prev == null )+" ");
            System.out.print(" " + temp.id + " |");
            temp = temp.next;
        } while (temp != null);
        System.out.println();
    }

    public void printDiskReverse() {
        LLNode temp = this.head;
        while (temp.next != null) {
            temp = temp.next;
        }
        String out = "";
        while (temp.prev != null) {
            // System.out.println(temp.id);
            out = " " + temp.id + " |" + out;
            temp = temp.prev;
        }
        out = (" " + temp.id + " |") + out;

        System.out.println(out + " <- rev");
    }

    public void Defrag(int steps) {
        LLNode searchStart = this.head;
        LLNode temp = null;
        for (int i = 0; i < steps; i++) { // do [i] many steps of defragmentation
            temp = searchStart;
            LLNode firstEmpty = null;
            LLNode lastEmpty = null;
            // find first empty space
            while (temp != null) {
                // check if current field is empty
                if (temp.id == -1) { // field is empty
                    firstEmpty = temp;
                    break;
                }
                temp = temp.next;
            }

            // find next full field
            while (temp != null) {
                // check if current field is empty
                if (temp.id != -1) { // field is not empty
                    lastEmpty = temp.prev;
                    if (firstEmpty.prev != null) {
                        firstEmpty.prev.next = temp;
                        temp.prev = firstEmpty.prev;

                    } else {
                        this.head = temp;
                        temp.prev = null;
                    }

                    break;
                }
                temp = temp.next;
            }

            // find the end of full fileds
            while (temp != null) {
                // check if current field is empty
                if (temp.next == null || temp.next.id != temp.id) { // this is the last of the field
                    if (temp.next != null) {
                        temp.next.prev = lastEmpty;
                    }
                    lastEmpty.next = temp.next;
                    temp.next = firstEmpty;
                    firstEmpty.prev = temp;

                    break;
                }
                temp = temp.next;
            }
            searchStart = firstEmpty;
        }
    }

    public void DumpDisk(String filename) throws IOException {
        // print file
        FileOutputStream out_f = new FileOutputStream(filename);
        OutputStreamWriter ssreamWriter = new OutputStreamWriter(out_f, StandardCharsets.UTF_8);
        BufferedWriter bufferedWriter = new BufferedWriter(ssreamWriter);

        LLNode temp = this.head;
        int i = 0;
        int num = -1, low=0;
        do {
            if(temp.id != -1){
                if(temp.id != num && num != -1){
                    bufferedWriter.append(String.format("%d,%d,%d", num,low,i-1));
                    bufferedWriter.append("\n");
                    low = i;
                }
                if(num == -1){
                    // we found first one of the field
                    low = i;
                }
            }else{ // we found empty field
                if(num != -1){
                    bufferedWriter.append(String.format("%d,%d,%d", num,low,i-1));
                    bufferedWriter.append("\n");
                }
            }
            num = temp.id;

            temp = temp.next;
            i++;
        } while (temp != null);

        if(num != -1){
            bufferedWriter.append(String.format("%d,%d,%d\n", num,low,i-1));
        }
        bufferedWriter.close();
    }
}

public class Naloga4 {
    public static void main(String[] args) throws IOException {
        // read the input data
        FileReader fileIn = new FileReader(args[0]);
        //FileReader fileIn = new FileReader("in.txt");
        BufferedReader reader = new BufferedReader(fileIn);
        // parse number of instructions
        String line = reader.readLine();

        Disk mojJakiDisk = new Disk();
        // read instructions
        
        for (int instruction = 0; instruction < Integer.parseInt(line); instruction++) {
            String ljna = reader.readLine();
            String[] lineIn = ljna.split(",");
            switch (lineIn[0]) {
                case "i":
                    mojJakiDisk.Init(Integer.parseInt(lineIn[1]));
                    break;
                case "a":
                    mojJakiDisk.alloc(Integer.parseInt(lineIn[1]), Integer.parseInt(lineIn[2]));
                    break;
                case "f":
                    mojJakiDisk.Free(Integer.parseInt(lineIn[1]));
                    break;
                case "d":
                    mojJakiDisk.Defrag(Integer.parseInt(lineIn[1]));
                    break;
            }
            //mojJakiDisk.PrintDisk();
        }
        reader.close();
        //mojJakiDisk.PrintDisk();
        mojJakiDisk.DumpDisk(args[1]);
    }
}
