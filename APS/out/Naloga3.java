import java.io.*;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;

class LineNode {
    public LineNode next;
    public LineNode prev;
    public int val;

    public LineNode(int val) {
        this.val = val;
    }
}

class Line {
    LineNode head;
    LineNode tail;
    public int length;

    public void pushString(String in) {
        this.push(Integer.parseInt(in));
    }

    public void debugme(String j) {
        LineNode a = this.head;
        System.out.print(j + " len:" + this.length + ": ");
        while (a != null) {
            System.out.print(" " + a.val);
            a = a.next;
        }
        System.out.println();
    }

    public void push(int in) {
        this.length++;
        LineNode newHead = new LineNode(in);

        newHead.next = this.head;
        if (this.head != null) {
            this.head.prev = newHead;
        } else {
            this.tail = newHead;
        }
        this.head = newHead;
        // this.debugme("push");
    }

    public int pop() {
        this.length--;
        int ret = this.tail.val;
        this.tail = this.tail.prev;
        if (this.tail != null) {
            this.tail.next = null;
        }
        // this.debugme("pop");
        return ret;

    }

    public int popFirst() {
        this.length--;
        int ret = this.head.val;
        this.head = this.head.next;
        if (this.head != null) {
            this.head.prev = null;
        }
        return ret;

    }
}

public class Naloga3 {
    static Line[] buffer;
    static int N;
    static int V;
    static int K;
    static int P;

    public static void main(String[] args) throws IOException {

        // read the input data
        FileReader fileIn = new FileReader(args[0]);
        // FileReader fileIn = new FileReader("in.txt");
        BufferedReader reader = new BufferedReader(fileIn);
        String[] line = reader.readLine().split(",");
        N = Integer.parseInt(line[0]);
        V = Integer.parseInt(line[1]);
        K = Integer.parseInt(line[2]);
        P = Integer.parseInt(line[3]);

        buffer = new Line[N];
        for (int y = 0; y < N; y++) {
            String[] lineIn = reader.readLine().split(",");
            Line l = new Line();
            if(!lineIn[0].equals("")){
                for (int i = 0; i < lineIn.length; i++) {
                    l.pushString(lineIn[lineIn.length - 1 - i]);
                }
            }
            buffer[y] = l;
        }
        reader.close();

        // solve
        solve3(K, V);
        String[] a = solve1(solve2());

        // print file
        FileOutputStream out_f = new FileOutputStream(args[1]);
        OutputStreamWriter ssreamWriter = new OutputStreamWriter(out_f, StandardCharsets.UTF_8);
        BufferedWriter bufferedWriter = new BufferedWriter(ssreamWriter);
        for (int i = 0; i < a.length; i++) {
            bufferedWriter.append((a[i]));
        }
        bufferedWriter.append("\n");
        bufferedWriter.close();
    }

    public static int[] solve2() {
        int sum = 0;
        for (int i = 0; i < buffer.length; i++) {
            sum += buffer[i].length;
        }
        int[] out = new int[sum];

        for (int i = 0; i < sum; i++) {
            // System.out.println(buffer[i%N].length);
            // buffer[i%N].debugme("LINE "+i%N);
            out[i] = buffer[i % N].popFirst();

        }
        // System.out.println();
        return out;
    }

    public static void solve3(int k, int currLine) {
        for (int i = 0; i < k; i++) {
            // poberi element iz trenutne vrstice
            int el = buffer[currLine].pop();

            // dobiš pravo vrstico
            int ljna = 0;
            while (((ljna + el) % N) != currLine) {
                ljna++;
            }

            // daš element v to vrstico
            buffer[ljna].push(el);
            // System.out.printf("Iz vrstice %d v vrstico %d\n",currLine, ljna);

            // se premakneš v naslednjo vrstico
            int j = 0;
            while ((N + (j + P % N)) % N != ljna) {
                j++;
            }

            // System.out.printf("Premaknem se iz %d v vrstico %d\n",currLine, j);
            currLine = j;
        }
    }

    public static String[] solve1(int[] in) {
        String[] a = new String[] { "A", "B", "C", "Č", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
                "R", "S", "Š", "T", "U", "V", "Z", "Ž", "a", "b", "c", "č", "d", "e", "f", "g", "h", "i", "j", "k", "l",
                "m", "n", "o", "p", "r", "s", "š", "t", "u", "v", "z", "ž", " " };
        String[] out = new String[in.length];
        for (int i = 0; i < in.length; i++) {
            out[i] = a[in[i]];
        }
        return out;
    }
}
