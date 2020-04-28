package search;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String entries = scanner.nextLine();
        String query = scanner.nextLine();
        if(entries.contains(query)){
            String[] seperateEntries = entries.split(" ");
            for (int i = 0; i < seperateEntries.length; i++) {
                if (seperateEntries[i].equals(query)){
                    System.out.print((i+1) + " ");
                }
            }
        }else{
            System.out.println("Not found");
        }
    }
}
