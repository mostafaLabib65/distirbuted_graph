package utilities;

import javafx.util.Pair;
import utilities.Graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class GraphInitializer {


    public static Graph initializeGraph(String pathName, String algorithm) {
        try {
            HashSet<Integer> seenNodes = new HashSet<>();
            ArrayList<Integer> nodesId = new ArrayList<>();
            ArrayList<Pair<Integer, Integer>> edges = new ArrayList<>();
            File file = new File(pathName);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.equalsIgnoreCase("S")) {
                    break;
                }

                String[] nodes = line.split(" ");


                int node1 = Integer.parseInt(nodes[0]);
                int node2 = Integer.parseInt(nodes[1]);


                if (!seenNodes.contains(node1)) {
                    seenNodes.add(node1);
                    nodesId.add(node1);
                }

                if (!seenNodes.contains(node2)) {
                    seenNodes.add(node2);
                    nodesId.add(node2);
                }

                Pair<Integer, Integer> edge =
                        new Pair<>(
                                Integer.parseInt(nodes[0]),
                                Integer.parseInt(nodes[1])
                        );
                edges.add(edge);

            }
            scanner.close();

            if (algorithm.equals("BFS")) {
                Graph graph = new BFSGraph(nodesId.size());
                graph.initializeEdges(edges);
                return graph;
            } else {

                FloydWarshallGraph graph;
                graph = new FloydWarshallGraph(nodesId.size());
                Collections.sort(nodesId);
                graph.nameTheNodes(nodesId);
                graph.initializeEdges(edges);
                return graph;
            }

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }


    }


}
