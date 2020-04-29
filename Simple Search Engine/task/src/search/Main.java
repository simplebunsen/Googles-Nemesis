package search;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;



public class Main {

    public static Scanner scanner = new Scanner(System.in);
    public static String[] entries;
    public static Map<String, List<Integer>> invertedIndexMap;
    public static SearchManager searchManager;

    public static void main(String[] args) {
//        System.out.println("Enter the number of people in the database:");
//        int n = scanner.nextInt();
//        scanner.nextLine();
//
//        System.out.printf("Enter all people on %d subsequent lines:\n", n);
//        entries = getEntriesFromInput(n);

        initialize(args);
        searchManager = new SearchManager(entries, invertedIndexMap);


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

    private static void initialize(String[] args) {
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

        invertedIndexMap = new HashMap<>();
        for (int i = 0; i < entries.length; i++) {
            String s = entries[i];
            String[] split = s.split(" ");
            for (String word : split) {
                word = word.toLowerCase();
                List<Integer> newList;
                if(!invertedIndexMap.containsKey(word)){
                    newList = new ArrayList<>();
                }else{
                    newList = invertedIndexMap.get(word);
                }
                newList.add(i);
                invertedIndexMap.put(word, newList);
            }
        }
        //System.out.println(invertedIndexMap.toString());
    }

    private static void printMenuChoices() {
        System.out.println("=== Menu ===\n" +
                            "1. Find a person\n" +
                            "2. Print all people\n" +
                            "0. Exit");
    }

    private static void handleSearch() {
        System.out.println("Select a matching strategy: ALL, ANY, NONE");
        searchManager.setMethod(scanner.nextLine());
        if (searchManager.getMethod() == null) return;

        System.out.println("Enter data to search people:");
        searchManager.searchAndPrint(scanner.nextLine());
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
//        boolean found = false;
//        for (String entry : entries) {
//            String s = entry.toLowerCase();
//            if (s.contains(input)) {
//                if(!found) System.out.println("The system has found people:");
//                found = true;
//                System.out.println(entry);
//            }
//        }
//        if (!found) System.out.println("No matching people found.");
    }
}
interface SearchMethod {
    void searchAndPrint(String input);
}

class AllSearchMethod implements SearchMethod{

    String[] entries;
    Map<String, List<Integer>> invertedIndexMap;

    public AllSearchMethod(String[] entries, Map<String, List<Integer>> invertedIndexMap) {
        this.entries = entries;
        this.invertedIndexMap = invertedIndexMap;
    }

    @Override
    public void searchAndPrint(String input) {
        input = input.toLowerCase();
        String[] inputSplit = input.split(" ");
        List<String> output = new ArrayList<>();
        System.out.println(invertedIndexMap.toString());
        System.out.println(Arrays.toString(inputSplit));
        List<Integer> indexCandidates = new ArrayList<>(invertedIndexMap.get(inputSplit[0]));
        //inverted index search.

        for (String s : inputSplit) {
            if (invertedIndexMap.containsKey(s)) {
                indexCandidates.retainAll(invertedIndexMap.get(s));
            } else {
                output.clear();
                break;
            }
        }
        for (Integer i : indexCandidates){
            output.add(entries[i]);
        }

        if (output.isEmpty()) {
            System.out.println("No matching people found, that hava all words of the query...");
        } else {
            System.out.println(String.valueOf(output.size()).concat(" persons have been found that has all" +
                    "words in the query"));
            for (String s : output) {
                System.out.println(s);
            }
        }
    }
}

class AnySearchMethod implements SearchMethod{

    String[] entries;
    Map<String, List<Integer>> invertedIndexMap;

    public AnySearchMethod(String[] entries, Map<String, List<Integer>> invertedIndexMap) {
        this.entries = entries;
        this.invertedIndexMap = invertedIndexMap;
    }

    @Override
    public void searchAndPrint(String input) {
        input = input.toLowerCase();
        String[] inputSplit = input.split(" ");
        List<String> output = new ArrayList<>();
        //inverted index search.
        for (String s : inputSplit) {
            if (invertedIndexMap.containsKey(s)) {
                for (Integer i : invertedIndexMap.get(s)) {
                    output.add(entries[i]);
                }
            }
        }
        if (output.isEmpty()) {
            System.out.println("No matching people found...");
        } else {
            System.out.println(String.valueOf(output.size()).concat(" persons have been found"));
            for (String s : output) {
                System.out.println(s);
            }
        }
    }
}

class NoneSearchMethod implements SearchMethod{
    String[] entries;
    Map<String, List<Integer>> invertedIndexMap;

    public NoneSearchMethod(String[] entries, Map<String, List<Integer>> invertedIndexMap) {
        this.entries = entries;
        this.invertedIndexMap = invertedIndexMap;
    }

    @Override
    public void searchAndPrint(String input) {
        input = input.toLowerCase();
        String[] inputSplit = input.split(" ");
        List<String> output = new ArrayList<>(Arrays.asList(entries));
        List<String> complement = new ArrayList<>();
        //inverted index search.
        for (String s : inputSplit) {
            if (invertedIndexMap.containsKey(s)) {
                for (Integer i : invertedIndexMap.get(s)) {
                    complement.add(entries[i]);
                }
            }
        }
        output.removeAll(complement);
        if (output.isEmpty()) {
            System.out.println("No matching people found... (only lines containing the query exist)");
        } else {
            System.out.println(String.valueOf(output.size()).concat(" persons have been found to not have the query words"));
            for (String s : output) {
                System.out.println(s);
            }
        }
    }
}

class SearchManager {
    public SearchMethod getMethod() {
        return method;
    }

    private SearchMethod method;

    String[] entries;
    Map<String, List<Integer>> invertedIndexMap;

    public SearchManager(String[] entries, Map<String, List<Integer>> invertedIndexMap) {
        this.entries = entries;
        this.invertedIndexMap = invertedIndexMap;
    }

    public void setMethod(String method){
        switch (method) {
            case "ALL":
                this.method = new AllSearchMethod(entries, invertedIndexMap);
                break;
            case "ANY":
                this.method = new AnySearchMethod(entries, invertedIndexMap);
                break;
            case "NONE":
                this.method = new NoneSearchMethod(entries, invertedIndexMap);
                break;
            default:
                System.out.println("Unknown input for method");
                this.method = null;
        }
    }

    public void searchAndPrint(String input){
        try {
            method.searchAndPrint(input);
        } catch (Exception e) {

        }
    }
}