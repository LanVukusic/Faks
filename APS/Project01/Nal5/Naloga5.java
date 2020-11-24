
class LLNode {
    public int field_from;
    public int field_to;
    public LLNode next;
    public LLNode prev;

    public  LLNode(int from, int to) {
        this.field_from = from;
        this.field_to = to;
    }
}

class Stanje { 
    public char[][] stolpci;
    public LLNode instructions;

    public Stanje(int sirina, int visina){
        this.stolpci = new char[sirina][visina];
    }

    public void AddInstruction(int from, int to){
        if(this.instructions == null){
            this.instructions = new LLNode(from, to);
            return;
        }
        LLNode temp = new LLNode(from, to);
        temp.next = this.instructions;
        this.instructions.next = temp;
    }

    @Override
    public int hashCode() {
        int aggr = 0;
        int mod = 0;
        for (char[] stolpec: this.stolpci ) {
            mod++;
            for (char vrednost : stolpec) {
                aggr += vrednost*(mod*17);
            }
        }   
        return aggr;
        //return firstname.hashCode()+lastname.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Stanje){
            return this.stolpci.equals(((Stanje)obj).stolpci);
        }
        return false;
    }
}



public class Naloga5 {
    public static void main(String[] args) {
        // read data
        

        // solve


        //print to file
    }
}
