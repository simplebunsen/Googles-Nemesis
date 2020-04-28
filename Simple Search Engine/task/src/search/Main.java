package search;

import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static String[] entries;

    public static void main(String[] args) {

        System.out.println("Enter the number of people in the database:");
        int n = scanner.nextInt();
        scanner.nextLine();

        System.out.printf("Enter all people on %d subsequent lines:\n", n);
        entries = getEntriesFromInput(n);

        System.out.println("Enter the number of search queries you will send:");
        int q = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < q; i++) {
            System.out.println("Enter data to search people:");
            processQuery(scanner.nextLine());
        }
    }

    private static String[] getEntriesFromInput(int n) {
        String[] entries = new String[n];
        for (int i = 0; i < n; i++) {
            entries[i] = scanner.nextLine();
        }
        return entries;
    }

    private static void processQuery(String input) {
        input = input.toLowerCase();

        boolean found = false;
        for (String entry : entries) {
            String s = entry.toLowerCase();
            if (s.contains(input)) {
                if(!found) System.out.println("The system has found people:");
                found = true;
                System.out.println(entry);
            }
        }
        if (!found) System.out.println("No matching people found.");
    }
}
