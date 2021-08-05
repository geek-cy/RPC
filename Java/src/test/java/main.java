import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class main{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        Set<Integer> set = new HashSet<>();

        while(sc.hasNextInt()){
            int n = sc.nextInt();
            for(int i = 0;i <= n;i++){
                int k = sc.nextInt();
                set.add(k);
                System.out.println();
            }
        }
        set.clear();
    }
}