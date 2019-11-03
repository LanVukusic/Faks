/**
 * Untitled-1
 */
public class Mrakovetezave1 {
    public static void main(String args[]){
        int a = 5;
        int Zadnjacifra = 1;
        String  mystr = "";
        for (int i = 0; i < a; i++) {

            for (int j = 0; j < a-i; j++) {
                mystr +=" ";
            }
            
            mystr += dejvn(Zadnjacifra, i*2+1);
            Zadnjacifra += i*2+1;

            System.out.println(mystr);
            mystr = "";
        }
        
    }

    public static String dejvn(int start, int dolz){
        String returner = "";
        for (int i = start; i < start+dolz; i++) {
            returner += i%10;  // ubistvu če je i recimo 33 dobis vn 3 - 
        }
        return returner;
    }
}