package module;
import java.util.Scanner;

public class ModuleTwo {

    public void runLoop(){
        Scanner sc = new Scanner(System.in);
        char input;
        do {
            input = printMenu(sc);
            evaluateOption(input,sc);
        }while(!exit(input));
        System.out.println("Hej då!");


    }

    void evaluateOption(char c,Scanner sc){
        if(c == '1'){addNum(sc);return;}
        if(c == '2'){countLetters(sc);return;}
        if(c == '3'){mirrorString(sc);return;}
        if(c == '4'){sumWords(sc);return;}
        if(c == '5'){countLetters(sc);}
    }

    char printMenu(Scanner input){
        System.out.println("Vad vill du göra?");
        System.out.println("1. Addera två tal");
        System.out.println("2. Räkna bokstäver i en sträng");
        System.out.println("3. Spegelvänd en sträng");
        System.out.println("4. Summera alla tal i en sträng");
        System.out.println("e. Avsluta");
        System.out.print("Val: ");
        String choice = input.nextLine();
        return choice.charAt(0);

    }

    void addNum(Scanner sc){
        String[] num = new String[2];
        System.out.println("Nummer 1: ");
        num[0] = sc.nextLine();
        System.out.println("Nummer 2: ");
        num[1] = sc.nextLine();
        System.out.println("Summa: %d".formatted(stringToInt(num)));

    }

    void countLetters(Scanner sc){
        String str;
        char find;
        int i = 0,cnt = 0;
        System.out.println("Ange ord att räkna bokstav i: ");
        str = sc.nextLine();
        System.out.println("Bokstav att räkna: ");
        find = sc.nextLine().charAt(0);
        while(i < str.length()){if(str.charAt(i++) == find)cnt++;}
        System.out.println("Det finns %d %c i ord %s".formatted(cnt,find,str));

    }

    void mirrorString(Scanner sc){
        String str;
        int i = 0;
        System.out.println("Ange ord att spegelvända: ");
        str = sc.nextLine();
        i = str.length()-1;
        while(i>=0){System.out.print(str.charAt(i--));}
        System.out.print("\n");

    }

    void sumWords(Scanner sc){
        String str;
        System.out.println("Ange tal att addera: ");
        str = sc.nextLine();
        String[] nums = str.split(" ");
        System.out.println("Summa: %d".formatted(stringToInt(nums)));


    }

    int stringToInt(String[] num){
        try{
            int sum = 0;
            for(int i = 0;i < num.length;i++)sum+=Integer.parseInt(num[i]);
            return sum;
        }
        catch( NumberFormatException err){
            System.out.println("Incorrect number!");
            return 0;
        }
    }

    boolean exit(char c){
        return c == 'e';
    }

}