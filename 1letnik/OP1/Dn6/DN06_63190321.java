import java.util.Scanner;
import java.util.ArrayList;

public class DN06_63190321 {
    static int [] tape = new int[10002];
    static int curr_index = 0;
    static int openBrackets = 0;
    static boolean exitLoop = false;
    static ArrayList<Integer> loops = new ArrayList<Integer>();  
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        char[] commands = sc.nextLine().toCharArray();

        for (int i = 0; i < commands.length; i++) {
            char c = commands[i];

            if(exitLoop){
                if(c == '['){
                    openBrackets ++;
                }
                if(c == ']'){
                    openBrackets --;
                    if(openBrackets == 0){
                        exitLoop = false;
                    }
                }
            }else{
                switch (c) {
                    case '+':
                        tape[curr_index] = (tape[curr_index] + 1) % 256;
                        break;
                    case '-':
                        tape[curr_index] = (tape[curr_index] + 255) % 256;
                        break;
                    case '>':
                        moveForward();
                        break;
                    case '<':
                        moveBackward();
                        break;
                    case '[':
                        openLoop(i);
                        break;
                    case ']':
                        i = closeLoop(i);
                        break;
                    case '.':
                        System.out.println(tape[curr_index]);
                        break;
                    case ',':
                        tape[curr_index] = sc.nextInt();
                        break;
                    }
            }
        }
    }
    
    public static void moveForward(){
        curr_index = ((curr_index + 1) % tape.length);
    }

    public static void moveBackward(){
        curr_index = (curr_index - 1 + tape.length) % tape.length;
    }

    public static void openLoop(int i){
        if(tape[curr_index] == 0){
            openBrackets ++;
            exitLoop = true;
        }else{
            loops.add(i);
        }
        
    }

    public static int closeLoop(int i){
        if(tape[curr_index] == 0){
            loops.remove(loops.size() - 1);
            return i;
        }else{
            return loops.get(loops.size() - 1);
        }
    }


}