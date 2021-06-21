public class eggs {
    public static int[][] memo;

    public static void main(String[] args) {
        int n = 4200; // number of floors
        int k = 300; // number of eggs
        memo = new int[n + 1][k + 1];

        System.out.println(eggDrop(n, k));
    }

    public static int eggDrop(int n, int k) {
        if (n == 1 || n == 0 || k == 1) {
            return n;
        }

        if (memo[n][k] != 0) {
            return memo[n][k];
        }

        int minOverX = Integer.MAX_VALUE;
        for (int x = 1; x <= n; x++) {
            minOverX = Math.min(minOverX, Math.max(eggDrop(x - 1, k - 1), eggDrop(n - x, k)));
        }
        memo[n][k] = 1 + minOverX;
        return 1 + minOverX;
    }
}
