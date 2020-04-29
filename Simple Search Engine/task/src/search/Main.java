package search;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static String[] entries;

    public static void main(String[] args) {
//        System.out.println("Enter the number of people in the database:");
//        int n = scanner.nextInt();
//        scanner.nextLine();
//
//        System.out.printf("Enter all people on %d subsequent lines:\n", n);
//        entries = getEntriesFromInput(n);
        File input;
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--data":
                    //TODO: catch case --data without filename
                    input = new File(".\\" + args[++i]);
                    try(Scanner fileScanner = new Scanner(input)){
                        ArrayList<String> lines = new ArrayList<>();
                        while (fileScanner.hasNextLine()){
                            lines.add(fileScanner.nextLine());
                        }
                        //System.out.println(lines.toString());
                        entries = lines.toArray(String[]::new);
                    }catch (FileNotFoundException e) {
                        System.out.println("Error: File not found");
                    }
            }
        }


        boolean running = true;
        while (running) {
            printMenuChoices();
            switch (scanner.nextLine()) {
                case "1":
                    handleSearch();
                    break;
                case "2":
                    printEntries();
                    break;
                case "0":
                    System.out.println("Bye!");
                    running = false;
                    break;
                default:
                    System.out.println("Incorrect option! try again.");
            }
        }
    }

    private static void printMenuChoices() {
        System.out.println("=== Menu ===\n" +
                            "1. Find a person\n" +
                            "2. Print all people\n" +
                            "0. Exit");
    }

    private static void handleSearch() {
            System.out.println("Enter data to search people:");
            processQuery(scanner.nextLine());
    }

    private static void printEntries() {
        System.out.println("--- LIST OF PEOPLE ---");
        for(String s : entries){
            System.out.println(s);
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
