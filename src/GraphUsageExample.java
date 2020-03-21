import utilities.Graph;
import utilities.GraphInitializer;

public class GraphUsageExample {


    public static void main(String[] args)  {

        Graph graph = GraphInitializer.initializeGraph("input.txt");

        graph.printGraph();


        try {
            System.out.println(graph.getShortestPath(1, 3));
            graph.addEdge(4, 5);
            System.out.println(graph.getShortestPath(1, 5));
            System.out.println(graph.getShortestPath(5, 1));


            graph.addEdge(5, 3);
            System.out.println(graph.getShortestPath(1, 3));
            graph.removeEdge(2, 3);
            System.out.println(graph.getShortestPath(1, 3));
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
