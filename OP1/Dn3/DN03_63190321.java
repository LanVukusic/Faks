import java.util.Scanner;

/**
 * DN03_63190321
 * https://ucilnica.fri.uni-lj.si/pluginfile.php/130234/mod_resource/content/1/stevila.pdf
 * input h,w,aÑ,bÑ,cÑ,aÓ,bÓ,cÓint.
 */
public class DN03_63190321 {
        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            // brace yourselves ...lots andlots of inputs are comming
            int h = sc.nextInt();
            int w = sc.nextInt();
            int ax = sc.nextInt();
            int bx = sc.nextInt();
            int cx = sc.nextInt();
            int ay = sc.nextInt();
            int by = sc.nextInt();
            int cy = sc.nextInt();
            int t = sc.nextInt();

            int time = 0;
            
            while (h != 1 || w !=1){
                
                if (isFree(ax, bx, cx, time) && w != 1){  // check if he can go right
                    // System.out.println("time: "+time+" go right"); 
                    w -= 1;
                    // System.out.println(w);
                    time += t;
                }else{
                    if(isFree(ay, by, cy, time) && h != 1){  // can he mybe go down than?
                       //  System.out.println("time: "+time+" go down");
                        h -= 1;
                        // System.out.println(h);
                        time += t;
                    }else{  // no? i guess he will just wait there for 1 "time"
                        // System.out.println(time);
                        time ++;
                        
                    }
                }
                
                
            }

            System.out.println(time);
            
        }

        public static boolean isFree(int a, int b, int c, int time){
            if (time < a){  // if time is within the firs <a> seconds
               return false;  // than we can say it is RED
            }
            time = (time - a) % (b + c);
            if (time < b){
                return true;
            }else{
                return false;
            }
        }
}