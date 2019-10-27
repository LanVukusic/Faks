/**
 * Test
 */
public class Test {

    public static void main(String[] args) {
        System.out.println(decimalAtPlace(12345, 5));
    }

    public static int decimalAtPlace (int num, int place){
        num = num % (int)Math.pow(10, place); // strips leading numbers
        num = num - num % (int)Math.pow(10, place-1);
        return num / (int)Math.pow(10, place-1);
    }
}