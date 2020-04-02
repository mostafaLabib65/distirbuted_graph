package utilities;

import javafx.util.Pair;

import java.util.ArrayList;

public interface Graph {

    void initializeEdges(ArrayList<Pair<Integer,Integer>> edges);

    void addEdge(int a, int b) throws InterruptedException;

    void removeEdge(int a, int b) throws InterruptedException;

    int getShortestPath(int a, int b) throws InterruptedException;

    void printGraph();
}
