import java.io.*;
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

class InstrList {
    public int[] db_from;
    public int[] db_to;
    public int length;

    public InstrList(int len) {
        this.db_from = new int[len];
        this.db_to = new int[len];
        this.length = 0;
    }

    public boolean append(int from, int to) {
        if (this.length == this.db_from.length) {
            System.out.println("nemorm apendat razbu"+this.length + "-"+this.db_from.length);
            return false;
        }
        this.length++;
        this.db_from[this.length - 1] = from;
        this.db_to[this.length - 1] = to;
        return true;
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
    public InstrList instructions;

    public Stanje(int sirina, int visina) {
        this.stolpci = new WannabeList[sirina];
        this.instructions = new InstrList(100);
        for (int i = 0; i < stolpci.length; i++) {
            stolpci[i] = new WannabeList(sirina);
        }
    }

    public void AddInstruction(int from, int to) {
        this.instructions.append(from, to);
    }

    public void move(int from, int to){
        this.stolpci[to].append(this.stolpci[from].pop());
        this.AddInstruction(from+1, to+1);
    }

    public Stanje copy() {
        Stanje temp = new Stanje(this.stolpci.length, this.stolpci[0].length);
        for (int i = 0; i < this.stolpci.length; i++) {
            temp.stolpci[i] = this.stolpci[i].copy();
        }

        // copy instructions
        InstrList temp_i = this.instructions;
        InstrList temp_aggr = new InstrList(temp_i.db_from.length);
        System.arraycopy(temp_i.db_from, 0, temp_aggr.db_from, 0, temp_i.length);
        System.arraycopy(temp_i.db_to, 0, temp_aggr.db_to, 0, temp_i.length);
        temp_aggr.length = temp_i.length;
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

        InstrList a = this.instructions;;
        for (int i = 0; i < a.length; i++) {
            out += (String.format("%d -> %d\n", a.db_from[i], a.db_to[i]));
        }

        // LLNode t = this.instructions;
        // while (t != null) {
        //     out += String.format("%d -> %d\n", t.field_from, t.field_to);
        //     t = t.next;
        // }
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
    public static void main(String[] args) throws IOException {
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
                            temp.move(stolpci_iz, stolpci_v);
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
                            temp.move(stolpci_iz, stolpci_v );
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
                InstrList temp = intersect[1].instructions;
                for (int i = 0; i < temp.length; i++) {
                    System.out.println(String.format("VZEMI %d\nIZPUSTI %d", temp.db_from[i], temp.db_to[i]));
                }

                //System.out.println();
                temp = intersect[0].instructions;
                for (int i = 0; i < temp.length; i++) {
                    System.out.println(String.format("VZEMI %d\nIZPUSTI %d", temp.db_from[temp.length -1 -i], temp.db_to[temp.length -1 -i]));
                }
               
                System.out.println(intersect[0]); // iz zadnjega konca
                System.out.println(intersect[1]); // iz sprednjega i  guess

                return;
            }
        }

        // print to file
    }

    // unit testing
    public static void main_(String[] args) throws IOException{

        Stanje a = new Stanje(10, 10);
        a.stolpci[0].append(10);
        a.stolpci[0].append(12);
        a.stolpci[0].append(13);
        a.stolpci[0].append(14);
        System.out.println(a);
        a.move(0, 1);
        a.move(0, 1);
        a.move(0, 2);
        System.out.println(a);
        //System.out.println(b);


    }
}
