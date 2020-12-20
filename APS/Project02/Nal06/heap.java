import java.util.PriorityQueue;

/**
 * heap
 */
public class heap {

    public static void main(String[] args) {
        PriorityQueue<Integer> priQ = new PriorityQueue<>();
        priQ.add(5);
        priQ.add(2);
        priQ.add(3);
        System.out.println(priQ.poll());
        System.out.println(priQ.poll());

    }
}