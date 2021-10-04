import java.io.*;
import java.text.Normalizer.Form;
import java.util.NoSuchElementException;

import java.util.Iterator;

class LLNode {
    public int field_from;
    public int field_to;
    public LLNode next;
    public LLNode prev;

    public LLNode(int from, int to) {
        this.field_from = from;
        this.field_to = to;
    }
}

class WannabeList {
    public int[] db;
    public int length;

    public WannabeList(int len) {
        this.db = new int[len];
        this.length = 0;
    }

    public int get(int index) {
        return this.db[index];
    }

    public boolean append(int elem) {
        if (this.length == this.db.length) {
            System.out.println("nemorm apendat brt");
            return false;
        }
        this.length++;
        this.db[this.length - 1] = elem;
        return true;
    }

    public int pop() {
        return this.get(length-- - 1);
    }

    public WannabeList copy() {
        WannabeList temp = new WannabeList(this.db.length);
        temp.length = this.length;
        temp.db = this.db.clone();
        return temp;
    }

    @Override
    public int hashCode() {
        int aggr = 0;
        for (int i = 0; i < this.length; i++) {
            aggr += this.db[i] * (17 + i);
        }
        return aggr;
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof WannabeList){
            if(this.length == ((WannabeList)other).length){
                for (int i = 0; i < this.db.length; i++) {
                    if(this.db[i] != ((WannabeList)other).db[i]){
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}

class Stanje {
    public WannabeList[] stolpci;
    public LLNode instructions;

    public Stanje(int sirina, int visina) {
        this.stolpci = new WannabeList[sirina];
        for (int i = 0; i < stolpci.length; i++) {
            stolpci[i] = new WannabeList(sirina);
        }
    }

    public void AddInstruction(int from, int to) {
        if (this.instructions == null) {
            this.instructions = new LLNode(from, to);
            return;
        }
        LLNode temp = new LLNode(from, to);
        temp.next = this.instructions;
        this.instructions = temp;
    }

    public Stanje copy() {
        Stanje temp = new Stanje(this.stolpci.length, this.stolpci[0].length);
        for (int i = 0; i < this.stolpci.length; i++) {
            temp.stolpci[i] = this.stolpci[i].copy();
        }

        LLNode temp_i = this.instructions;
        LLNode temp_aggr = null;
        while (temp_i != null) {
            if (temp_aggr == null) {
                temp_aggr = new LLNode(temp_i.field_from, temp_i.field_to);
                continue;
            }
            temp_aggr.next = new LLNode(temp_i.field_from, temp_i.field_to);
            temp_aggr = temp_aggr.next;
            temp_i = temp_i.next;
        }
        temp.instructions = temp_aggr;

        return temp;
    }

    @Override
    public String toString(){
        String out = "";
        for (WannabeList wannabeList : stolpci) {
            out += "::";
            for (int i = 0; i < wannabeList.length; i++) {
                out += wannabeList.db[i] + " ";
            }
            out += "\n";
        }

        LLNode t = this.instructions;
        while (t != null) {
            out += String.format("%d -> %d\n", t.field_from, t.field_to);
            t = t.next;
        }
        return out;

    }

    @Override
    public int hashCode() {
        int aggr = 0;
        int mod = 0;
        for (WannabeList stolpec : this.stolpci) {
            mod++;
            aggr += stolpec.hashCode() * mod;
        }
        return aggr;
        // return firstname.hashCode()+lastname.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Stanje) {
            for (int i = 0; i < stolpci.length; i++) {
                if(!stolpci[i].equals(((Stanje)obj).stolpci[i])){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    // @Override
    // public String toString() {
    //     return
    // }

}

class HashSet implements Iterable {

    private static class Entry {
        Stanje key;
        Entry next;
    }

    private Entry[] buckets;

    private int size;
    public int length;

    public HashSet(int capacity) {
        buckets = new Entry[capacity];
        size = 0;
    }

    private int hashFunction(int hashCode) {
        int index = hashCode;
        if (index < 0) {
            index = -index;
        }
        return index % buckets.length;
    }

    public boolean contains(Stanje element) {
        int index = hashFunction(element.hashCode());
        Entry current = buckets[index];

        while (current != null) {
            // check if node contains element
            if (current.key.equals(element)) {
                return true;
            }
            // otherwise visit next node in the bucket
            current = current.next;
        }
        // no element found
        return false;
    }

    public Stanje getCopy(Stanje s){
        int index = hashFunction(s.hashCode());
        Entry current = buckets[index];

        while (current != null) {
            // check if node contains element
            if (current.key.equals(s)) {
                return current.key;
            }
            // otherwise visit next node in the bucket
            current = current.next;
        }
        // no element found
        return null;
    }

    public boolean add(Stanje element) {

        int index = hashFunction(element.hashCode());
        // log.info(element.toString() + " hashCode=" + element.hashCode() + " index=" +
        // index);
        Entry current = buckets[index];

        while (current != null) {
            // element is already in set
            if (current.key.equals(element)) {
                return false;
            }
            // otherwise visit next entry in the bucket
            current = current.next;
        }
        // no element found so add new entry
        Entry entry = new Entry();
        entry.key = element;
        // current Entry is null if bucket is empty
        // if it is not null it becomes next Entry
        entry.next = buckets[index];
        buckets[index] = entry;
        size++;
        this.length ++;
        return true;
    }

    public boolean remove(Stanje element) {

        int index = hashFunction(element.hashCode());
        Entry current = buckets[index];
        Entry previous = null;

        while (current != null) {
            // element found so remove it
            if (current.key.equals(element)) {
                if (previous == null) {
                    buckets[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
                this.length --;
                return true;
            }
            previous = current;
            current = current.next;
        }
        // no element found nothing to remove
        return false;
    }

    public Stanje[] intersect(HashSet other) {
        for (Object b : other) {
            if (this.contains((Stanje)b)) {
                return new Stanje[] {(Stanje)b, this.getCopy((Stanje)b)};
            }
        }
        return null;
    }

    @Override
    public String toString() {

        Entry currentEntry = null;
        StringBuffer sb = new StringBuffer();

        // loop through the array
        for (int index = 0; index < buckets.length; index++) {
            // we have an entry
            if (buckets[index] != null) {
                currentEntry = buckets[index];
                sb.append("[" + index + "]\n");
                sb.append(currentEntry.key.toString());
                while (currentEntry.next != null) {
                    currentEntry = currentEntry.next;
                    sb.append(" -> " + currentEntry.key.toString());
                }
                sb.append('\n');
            }
        }


        return sb.toString();
    }

    public Iterator iterator() {
        return new SimpleHashSetIterator();
    }

    class SimpleHashSetIterator implements Iterator {

        private int currentBucket;
        private int previousBucket;
        private Entry currentEntry;
        private Entry previousEntry;

        public SimpleHashSetIterator() {

            currentEntry = null;
            previousEntry = null;
            currentBucket = -1;
            previousBucket = -1;

        }

    
        public boolean hasNext() {

            // currentEntry node has next
            if (currentEntry != null && currentEntry.next != null) {
                return true;
            }

            // there are still nodes
            for (int index = currentBucket + 1; index < buckets.length; index++) {
                if (buckets[index] != null) {
                    return true;
                }
            }

            // nothing left
            return false;
        }

        public Stanje next() {

            previousEntry = currentEntry;
            previousBucket = currentBucket;

            // if either the current or next node are null
            if (currentEntry == null || currentEntry.next == null) {

                // go to next bucket
                currentBucket++;

                // keep going until you find a bucket with a node
                while (currentBucket < buckets.length && buckets[currentBucket] == null) {
                    // go to next bucket
                    currentBucket++;
                }

                // if bucket array index still in bounds
                // make it the current node
                if (currentBucket < buckets.length) {
                    currentEntry = buckets[currentBucket];
                }
                // otherwise there are no more elements
                else {
                    throw new NoSuchElementException();

                }
            }
            // go to the next element in bucket
            else {

                currentEntry = currentEntry.next;
            }

            // return the element in the current node
            return currentEntry.key;

        }

    }

}

public class Naloga5 {
    public static HashSet combos (HashSet in, int length, int height){
        HashSet tempNivo =  new HashSet(5000);
        for (Object h : in) {
            Stanje a = (Stanje) h;
            for (int stolpci_iz = 0; stolpci_iz < length; stolpci_iz++) {
                for (int stolpci_v = 0; stolpci_v < length; stolpci_v++) {
                    //vrstica ni prazna, destinacija pa ni polna
                    if (a.stolpci[stolpci_iz].length == 0 || a.stolpci[stolpci_v].length == height) {
                        continue;
                    }
                    //ne dajemo isto v isto kr pač why
                    if (stolpci_iz == stolpci_v) {
                        continue;
                    }

                    Stanje temp = a.copy();
                    temp.AddInstruction(stolpci_iz + 1, stolpci_v + 1);
                    temp.stolpci[stolpci_v].append(temp.stolpci[stolpci_iz].pop());
                    tempNivo.add(temp);
                }
            }
        }
        return tempNivo;
    }
    public static void main_(String[] args) throws IOException {
        // read the input data
        FileReader fileIn = new FileReader(args[0]);
        BufferedReader reader = new BufferedReader(fileIn);

        String[] line = reader.readLine().split(",");
        int length = Integer.parseInt(line[0]); // len
        int height = Integer.parseInt(line[1]); // h

        Stanje startImg = new Stanje(length, height);
        Stanje endImg = new Stanje(length, height);

        // zacetki
        for (int i = 0; i < length; i++) {
            String[] read = reader.readLine().split(":");
            if (read.length == 1) {
                continue;
            }
            for (String a : read[1].split(",")) {
                startImg.stolpci[i].append((a.hashCode()));
            }
        }

        // konci
        for (int i = 0; i < length; i++) {
            String[] read = reader.readLine().split(":");
            if (read.length == 1) {
                continue;
            }
            for (String a : read[1].split(",")) {
                endImg.stolpci[i].append((a.hashCode()));
            }
        }
        reader.close();

        // solve
        // create first entries
        HashSet zacetneSlike = new HashSet(10);
        zacetneSlike.add(startImg);
        HashSet koncneSlike = new HashSet(10);
        koncneSlike.add(endImg);

        boolean flipflop = true;
        HashSet tempNivo = null;
        while (true) {
            tempNivo =  new HashSet(5000);
            if (flipflop) {
                for (Object h : zacetneSlike) {
                    Stanje a = (Stanje) h;
                    for (int stolpci_iz = 0; stolpci_iz < length; stolpci_iz++) {
                        for (int stolpci_v = 0; stolpci_v < length; stolpci_v++) {
                            //vrstica ni prazna, destinacija pa ni polna
                            if (a.stolpci[stolpci_iz].length == 0 || a.stolpci[stolpci_v].length == height) {
                                continue;
                            }
                            //ne dajemo isto v isto kr pač why
                            if (stolpci_iz == stolpci_v) {
                                continue;
                            }

                            Stanje temp = a.copy();
                            temp.AddInstruction(stolpci_iz + 1, stolpci_v + 1);
                            temp.stolpci[stolpci_v].append(temp.stolpci[stolpci_iz].pop());
                            tempNivo.add(temp);
                        }
                    }
                }
            } else {
                for (Object h : koncneSlike) {
                    Stanje a = (Stanje) h;
                    for (int stolpci_iz = 0; stolpci_iz < length; stolpci_iz++) {
                        for (int stolpci_v = 0; stolpci_v < length; stolpci_v++) {
                            if (a.stolpci[stolpci_iz].length == 0 || a.stolpci[stolpci_v].length == height) {
                                continue;
                            }
                            if (stolpci_iz == stolpci_v) {
                                continue;
                            }

                            Stanje temp = a.copy();
                            temp.AddInstruction(stolpci_iz + 1, stolpci_v + 1);
                            temp.stolpci[stolpci_v].append(temp.stolpci[stolpci_iz].pop());
                            tempNivo.add(temp);
                            
                        }
                    }
                }
            }

            if (flipflop) {
                zacetneSlike = tempNivo;
            } else {
                koncneSlike = tempNivo;
            }
            System.out.printf("%d %d\n", koncneSlike.length, zacetneSlike.length);

            flipflop = !flipflop;
       
            Stanje[] intersect = zacetneSlike.intersect(koncneSlike);

            if (intersect != null) {
                System.out.println("start&end");
                System.out.println(startImg);
                System.out.println(endImg);
                System.out.println("result");

                //System.out.println();
                LLNode temp = intersect[1].instructions;
                while (temp != null){
                    System.out.println(String.format("VZEMI %d\nIZPUSTI %d", temp.field_from, temp.field_to));
                    temp = temp.next;
                }

                //System.out.println();
                temp = intersect[0].instructions;
                String out = "";
                while (temp != null){
                    out = String.format("VZEMI %d\nIZPUSTI %d\n", temp.field_to, temp.field_from) + out;
                    temp = temp.next;
                }
                System.out.print(out);

                System.out.println(intersect[0]); // iz zadnjega konca
                System.out.println(intersect[1]); // iz sprednjega i  guess
                System.out.println(intersect[0].equals(intersect[1]));

                return;
            }
        }

        // print to file
    }

    // unit testing
    public static void main(String[] args) throws IOException{
        // read the input data
        HashSet a = new HashSet(10);
        Stanje b = new Stanje(3, 3);
        b.stolpci[0].append(3);
        b.stolpci[0].append(2);
        b.stolpci[0].append(1);

        a.add(b);

        HashSet out = combos(a,3,3);
        System.out.println(out);

        HashSet out2 = combos(out,3,3);
        System.out.println(out2);   
    }
}
