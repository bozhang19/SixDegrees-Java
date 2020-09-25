import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * This class represents the console.
 *
 * @author Bo Zhang
 * @version 1.0
 */
public class Console {
    /**
     * Main method.
     *
     * @param args  the arguments
     * @throws FileNotFoundException  the exception to throw
     */
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scan = new Scanner(System.in);
        int fileChoice = getFileChoice(scan);

        HollywoodGraph graph = createGraph(fileChoice);

        System.out.println("Built graph successfully!\n");
        // graph info
        graph.printAdjacencyList();
        System.out.println("\nVertex size: " + graph.getVertexSize());
        System.out.println("Edge size: " + graph.getEdgeSize());

        System.out.print("\nEnter a source actor: ");
        String actor = scan.nextLine();

        System.out.println();
        System.out.println("Actor Numbers\n*************");

        Map<String, Integer> map = graph.generateActorNumbers(actor);
        printSortedMapInfo(map);
    }

    /**
     * Displays the menu and get user input of choice.
     *
     * @param scan  the scanner object to read user input
     * @return an int  the user's choice
     */
    public static int getFileChoice(Scanner scan) {
        System.out.println("Welcome to the Six-Degrees of Separation Game!\n");
        System.out.println("Please choose one of the following files:");
        System.out.println("(enter any other number to test with the small file)");
        System.out.println("1. 20_actors_100_movies.txt");
        System.out.println("2. 300_actors_50_movies.txt");
        System.out.println("3. 900_actors_100_movies.txt");

        int choice = scan.nextInt();
        scan.nextLine();

        return choice;
    }

    /**
     * Reads and processes the input file.
     *
     * @param fileNum  the number the user chose
     * @throws FileNotFoundException  the exception to throw
     * @return a graph  the Hollywood graph generated
     */
    public static HollywoodGraph createGraph(int fileNum) throws FileNotFoundException {
        // only use those three files described on Canvas
        String fileName;
        if (fileNum == 1) {
            fileName = "actors_movies/20_actors_100_movies.txt";
        } else if (fileNum == 2) {
            fileName = "actors_movies/300_actors_50_movies.txt";
        } else if (fileNum == 3) {
            fileName = "actors_movies/900_actors_100_movies.txt";
        } else {
            fileName = "actors_movies/small.txt";
        }

        File file = new File(fileName);
        Scanner fileScanner = new Scanner(file);

        System.out.println("Building graph, please wait...");
        HollywoodGraph graph = new HollywoodGraph();

        while (fileScanner.hasNextLine()) {
            String nextLine = fileScanner.nextLine();
            String[] tokens = nextLine.split(Pattern.quote(" | "));
            graph.add(tokens);
        }

        return graph;
    }

    /**
     * Prints each pair in the map based on their values in ascending order.
     *
     * @param map  the hashmap
     */
    public static void printSortedMapInfo(Map<String, Integer> map) {
        Set<Map.Entry<String, Integer>> set = map.entrySet();
        List<Map.Entry<String, Integer>> list = new ArrayList<>(set);
        list.sort(Comparator.comparing(object -> (object.getValue())));
        int totalActorNumber = 0;
        int numActors = 0;
        for (Map.Entry<String, Integer> entry : list) {
            totalActorNumber += entry.getValue();
            numActors++;
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        System.out.println("\nThe average \"Actor Number\" above is: " + decimalFormat.format((double)totalActorNumber / numActors));
    }
}
