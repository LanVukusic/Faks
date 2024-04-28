import java.util.Scanner;
// https://ucilnica.fri.uni-lj.si/pluginfile.php/129754/mod_resource/content/1/telesa.pdf
// Od nejca :P

public class Test1 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt(); //indint ex da ti pove kaj za en lik je to 
        if (a == 1 ) {
            //System.out.println("Kocka");
            int stranica = sc.nextInt(); // dejansko dolzino stranice
            int rezultat = stranica*stranica*stranica;
            System.out.println(rezultat);
        }if (a == 2 ){
            //System.out.println("Kvader");
            int stranica1 =sc.nextInt();
            int stranica2 =sc.nextInt();
            int stranica3 =sc.nextInt();
            System.out.println(stranica1*stranica2*stranica3);
        }if (a == 3 ){
            //System.out.println("Piramida");
            //opcija 1
            //int visina = sc.nextInt();
            // System.out.println(Piramida(visina));

            //opcija 2
            int visina = sc.nextInt();
            int skupniVolumen = 0;
            for (int stevec = 1; stevec <= visina; stevec ++ ){
                skupniVolumen += stevec*stevec;
            }
            System.out.println(skupniVolumen);
        }if (a == 4 ){
            //System.out.println("Prizma");
            int skupniVolumen = 0;
            int dolzina = sc.nextInt();
            int sirina = sc.nextInt();
            for (int dolzina1 = 1; dolzina1 <= dolzina; dolzina1 ++){
                skupniVolumen += dolzina1*sirina;
            }
            System.out.println(skupniVolumen);
        }if (a == 5){
            //System.out.println("Votla piramida");
            int h = sc.nextInt();
            System.out.println(Piramida(h) - Piramida(h-2));
        }if (a == 6) {
            //System.out.println("Votla prizma");
            int skupniVolumen = 0;
            int visina = sc.nextInt();
            int sirina = sc.nextInt();
            for (int visinaTrenutnaVelika = 1; visinaTrenutnaVelika <= visina; visinaTrenutnaVelika ++){
                skupniVolumen += visinaTrenutnaVelika*sirina;
            }
            for (int visinaTrenutnaMala = 1; visinaTrenutnaMala<= visina-2; visinaTrenutnaMala ++){
                skupniVolumen -= visinaTrenutnaMala*(sirina-2);
            }
            System.out.println(skupniVolumen);

        }

    }

    // to se nanasa na 5-ko
    public static int Piramida (int visina){
        int skupniVolumen = 0;
        for (int stevec = 1; stevec <= visina; stevec ++ ){
            skupniVolumen += stevec*stevec;
        }
        return skupniVolumen;
    }
}