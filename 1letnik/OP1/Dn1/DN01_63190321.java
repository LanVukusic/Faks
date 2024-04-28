// https://ucilnica.fri.uni-lj.si/pluginfile.php/129754/mod_resource/content/1/telesa.pdf

import java.util.Scanner;

public class DN01_63190321 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int oblika = sc.nextInt();

        int a;
        int b;
        int c;
        switch (oblika) {
            case 1:
                //kocka
                a = sc.nextInt();
                System.out.println(a*a);
                break;
        
            case 2:
                //kvader
                a = sc.nextInt();
                b = sc.nextInt();
                c = sc.nextInt();
                System.out.println(a*b*c);
                break;
            case 3:
                //piramida
                a = sc.nextInt();
                int sumZidakov = 0;

                for(int i = 1; i<=a; i++){
                    sumZidakov += i*i;
                }

                System.out.println(sumZidakov);
                break;
                
            case 4:
                //prizma
                a = sc.nextInt();
                b = sc.nextInt();
                int firstFace = 0;

                for(int i = 1; i <= a; i++){
                    firstFace += i;
                }
                System.out.println(firstFace*b);
                break;

            case 5:
                //piramida votla
                
                a = sc.nextInt();
                int sumZidakov2 = 0;

                for(int i = 1; i<=a; i++){
                    sumZidakov2 += i*i;
                    if(i <= 3){
                        sumZidakov2 -= (i-2)*(i-2);
                    }
                }
                System.out.println(sumZidakov2);
                break;

            case 6:
                //prizma
                a = sc.nextInt();
                b = sc.nextInt();
                int firstFace2 = 0;

                for(int j = 1; j <= b; j++){
                    for(int i = 1; i <= a; i++){
                        firstFace2 += i;
                        if(j > 2 && i > 2){// if the length is above 2, we can make it hollow;
                        
                                firstFace2 -= i-2;
                        }
                    }    
                }
                System.out.println(firstFace2);
                break;
        }
        sc.close();
    }
}