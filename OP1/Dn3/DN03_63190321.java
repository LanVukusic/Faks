import java.util.Scanner;
import java.lang.Math;

/**
 * DN03_63190321
 * https://ucilnica.fri.uni-lj.si/pluginfile.php/130234/mod_resource/content/1/stevila.pdf
 * input h,w,ax,bx,cx,ay,by,cy,t.
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

            int time = Math.min(ax, ay);

            int nextFreeX = ax;
            int nextFreeY = ay;
            
            while (true){ // loop while player has not yet reached (1,1) break on line 44
                if (isFree(ax, bx, cx, time) && w != 1){  // check if he can go right 
                    w -= 1; // move him right
                    nextFreeX = getTimeIncrement(ax, bx, cx, t, time); // when is the next green light?
                    nextFreeY = getTimeIncrement(ay, by, cy, t, time);
                    time += t;  // pass the time
                }else{
                    if(isFree(ay, by, cy, time) && h != 1){  // can he mybe go down than?
                        h -= 1;
                        nextFreeX = getTimeIncrement(ax, bx, cx, t, time); // when is the next green light?
                        nextFreeY = getTimeIncrement(ay, by, cy, t, time);                        
                        time += t;  // pass the time
                    }
                }
                if(h == 1 && w ==1){  // if we are at school... sweet!!
                    System.out.println(time);
                    break;
                }
                time = getTime(w, h, nextFreeX, nextFreeY, time); // skip time to next time frame, when we can move
            }
        }

        public static boolean isFree(int a, int b, int c, int time){
            if (time < a){  // if time is within the firs <a> seconds
               return false;  // than we can say it is RED
            }
            time = (time - a) % (b + c);  // if its later in time, we can MOD it with sum of a+b to determine absolute position in
                                        //the red / green loop
            if (time < b){  // if it is on the beginning of the loop, so before first red :
                return true;  // it is GREEN
            }else{
                return false; // if not, it must be RED
            }
            
        }

        public static int getTimeIncrement(int a, int b, int c, int delay, int time ){
            { // that means we are in the green / red loop
                int o = (time + delay - a) % (b + c); // we reset the loop to the relative times
                if(o < (b)){  // if we are on GREEN at the moment we stop moving..
                    return time + delay;  // than sweet!! we can just say thats our time.
                }else{  // if not, we must calculate it
                    return (time + delay) + ((b+c)-o);  // we wait additional (b+c)-o time
                }                                      // time we need to reach the next GREEN
            }
        }

        public static int getTime (int px, int py, int nx, int ny, int time){
            if (px == 1){  // if we cant move on x, we give priorit to y
                nx = ny;
            }
            if (py == 1){  // if we cant move in y, we give priority to x
                ny = nx;
            }
            if(time <= Math.min(nx, ny)){  // if we havent missed our next best time
                return Math.min(nx, ny);  // nice, lets set time to our next nearest time.
            }else{  // we missed the next event
                return Math.max(nx, ny);  // so we move on our second best option
            }
        }
}
