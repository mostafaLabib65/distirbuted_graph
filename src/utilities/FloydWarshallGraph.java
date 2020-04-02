package utilities;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import static java.lang.Integer.max;
import static java.lang.Math.min;

public class FloydWarshallGraph implements Graph {
    private int[][] adjacencyMatrix;
    private HashMap<Integer, Integer> nameMapping;
    private int numberOfVertecies;
    private boolean updated;
    private int[][] shortestPathMatrix;
    private final int INF = 10000000;


    //synchronization
    private Semaphore readersLock = new Semaphore(1);
    private Semaphore queue = new Semaphore(1);
    private Semaphore writersLock = new Semaphore(1);
    private Semaphore readersCountLock = new Semaphore(1);
    private Semaphore writersCountLock = new Semaphore(1);
    private int readerCount = 0;
    private int writerCount = 0;

    public FloydWarshallGraph(int size) {
        adjacencyMatrix = new int[size][size];
        shortestPathMatrix = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j< size; j++) {
                if(i == j){
                    adjacencyMatrix[i][j] = 0;

                }else{
                    adjacencyMatrix[i][j] = INF;

                }
            }

        }


        nameMapping = new HashMap<>();
        numberOfVertecies = size;
        updated = true;
    }


    public void nameTheNodes(ArrayList<Integer> nodes) {
        for(int i = 0 ; i < numberOfVertecies; i++) {
            nameMapping.put(nodes.get(i), i);
        }
    }

    public void initializeEdges(ArrayList<Pair<Integer,Integer>> edges) {
        for(Pair<Integer,Integer> edge : edges) {
            adjacencyMatrix[nameMapping.get(edge.getKey())][nameMapping.get(edge.getValue())] = 1;
        }
    }

    @Override
    public void addEdge(int a, int b) throws InterruptedException {


        writersCountLock.acquire();
        writerCount++;
        if (writerCount == 1)
            readersLock.acquire();
        writersCountLock.release();
        writersLock.acquire();
        updated = true;

        if(!nameMapping.containsKey(a) && !nameMapping.containsKey(b)) {
            adjacencyMatrix = copyMatrixWithIncrease(adjacencyMatrix, 2);
            nameMapping.put(a,numberOfVertecies);
            numberOfVertecies += 1;
            nameMapping.put(b,numberOfVertecies);
            numberOfVertecies += 1;
        } else if (!nameMapping.containsKey(a)) {
            adjacencyMatrix = copyMatrixWithIncrease(adjacencyMatrix, 1);
            nameMapping.put(a,numberOfVertecies);
            numberOfVertecies += 1;
        } else if (!nameMapping.containsKey(b)) {
            adjacencyMatrix = copyMatrixWithIncrease(adjacencyMatrix, 1);
            nameMapping.put(b,numberOfVertecies);
            numberOfVertecies += 1;
        }
        adjacencyMatrix[nameMapping.get(a)][nameMapping.get(b)] = 1;

        writersLock.release();
        writersCountLock.acquire();
        writerCount--;
        if (writerCount == 0)
            readersLock.release();
        writersCountLock.release();
    }

    private int[][] copyMatrixWithIncrease(int[][] m1, int increase){

        int[][] tmp;
        int d1;
        int d2;
        try {
            d1 = m1.length + increase;
            d2 = m1[0].length + increase;
        } catch (Exception e) {
            d1 = increase;
            d2 = increase;
        }
        tmp = new int[d1][d2];

        for (int i = 0; i < d1; i++) {
            for (int j = 0; j< d2; j++) {
                if(i != j){
                    tmp[i][j] = INF;
                }else {
                    tmp[i][j] = 0;
                }
            }
        }

        for (int i = 0; i < d1 - increase; i++) {
            for (int j = 0 ; j < d2 - increase; j++) {
                tmp[i][j] = m1[i][j];
            }
        }

        return tmp;
    }

    @Override
    public void removeEdge(int a, int b) throws InterruptedException {



        writersCountLock.acquire();
        writerCount++;
        if (writerCount == 1)
            readersLock.acquire();
        writersCountLock.release();
        writersLock.acquire();



        updated = true;
        if(a != b){
            adjacencyMatrix[nameMapping.get(a)][nameMapping.get(b)] = INF;
        }
        writersLock.release();
        writersCountLock.acquire();
        writerCount--;
        if (writerCount == 0)
            readersLock.release();
        writersCountLock.release();


    }

    @Override
    public int getShortestPath(int a, int b) throws InterruptedException {


        int result;
        queue.acquire();
        readersLock.acquire();
        readersCountLock.acquire();
        readerCount++;
        if (readerCount == 1) {
            writersLock.acquire();
        }
        if(updated)
            calculateShortestPaths();
        readersCountLock.release();
        readersLock.release();
        queue.release();

        result = shortestPathMatrix[nameMapping.get(a)][nameMapping.get(b)];

        readersCountLock.acquire();
        readerCount--;
        if (readerCount == 0)
            writersLock.release();
        readersCountLock.release();

        if(result == INF) return -1;
        return result;
    }

    public void calculateShortestPaths() {

        shortestPathMatrix = new int[numberOfVertecies][numberOfVertecies];
        for (int i = 0; i < numberOfVertecies; i++) {
            for (int j = 0 ; j < numberOfVertecies; j++) {
                shortestPathMatrix[i][j] = adjacencyMatrix[i][j];
            }
        }

        for (int k = 0; k < numberOfVertecies; k++) {
            for (int i = 0; i < numberOfVertecies; i++){
                for (int j = 0; j < numberOfVertecies; j++) {
                    shortestPathMatrix[i][j] =
                            min(
                                    shortestPathMatrix[i][j],
                                    shortestPathMatrix[i][k]+ shortestPathMatrix[k][j]
                            );
                }
            }
        }

        updated = false;

    }


    @Override
    public synchronized void printGraph(){
        for (int i = 0; i < numberOfVertecies; i++) {
            for (int j = 0 ; j < numberOfVertecies; j++) {
                if(adjacencyMatrix[i][j] == INF)
                    System.out.print("-1 ");
                else
                    System.out.print(" "+adjacencyMatrix[i][j] +" ");
            }
            System.out.println();
        }
    }



}
