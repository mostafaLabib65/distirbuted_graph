package utilities;


import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.Semaphore;


public class BFSGraph implements Graph {

    private HashMap<Integer, ArrayList<Integer>> adjacencyList;
    private final int INF = 10000000;


    //synchronization
    private Semaphore readersLock = new Semaphore(1);
    private Semaphore queue = new Semaphore(1);
    private Semaphore writersLock = new Semaphore(1);
    private Semaphore readersCountLock = new Semaphore(1);
    private Semaphore writersCountLock = new Semaphore(1);
    private int readerCount = 0;
    private int writerCount = 0;

    public BFSGraph(int size) {
        adjacencyList = new HashMap<>();
    }

    @Override
    public void initializeEdges(ArrayList<Pair<Integer, Integer>> edges) {
        for (Pair<Integer, Integer> edge : edges) {
            if (adjacencyList.containsKey(edge.getKey()))
                adjacencyList.get(edge.getKey()).add(edge.getValue());
            else
                adjacencyList.put(edge.getKey(), new ArrayList<Integer>(){{add(edge.getValue());}});
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

        if (!adjacencyList.containsKey(a) && !adjacencyList.containsKey(b)) {
            adjacencyList.put(a, new ArrayList<Integer>(){{add(b);}});
        } else if (!adjacencyList.containsKey(a)) {
            adjacencyList.put(a, new ArrayList<Integer>(){{add(b);}});
        } else if (!adjacencyList.containsKey(b)) {
            adjacencyList.get(a).add(b);
            adjacencyList.put(b, new ArrayList<>());
        } else {
            adjacencyList.get(a).add(b);
        }

        writersLock.release();
        writersCountLock.acquire();
        writerCount--;
        if (writerCount == 0)
            readersLock.release();
        writersCountLock.release();
    }

    @Override
    public void removeEdge(int a, int b) throws InterruptedException {
        writersCountLock.acquire();
        writerCount++;
        if (writerCount == 1)
            readersLock.acquire();
        writersCountLock.release();
        writersLock.acquire();

        if (adjacencyList.containsKey(a))
            if (adjacencyList.get(a).contains(b))
                adjacencyList.get(a).remove(new Integer(b));

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
        result = calculateShortestPaths(a, b);
        readersCountLock.release();
        readersLock.release();
        queue.release();


        readersCountLock.acquire();
        readerCount--;
        if (readerCount == 0)
            writersLock.release();
        readersCountLock.release();

        return result;
    }

    private int calculateShortestPaths(int a, int b) {
        if (a == b)
            return 0;

        Queue<Integer> q = new LinkedList<>();
        HashMap<Integer, Integer> distance = new HashMap<>(INF);
        HashSet<Integer> visited = new HashSet<>();

        q.add(a);
        visited.add(a);
        distance.put(a, 0);
        while (!q.isEmpty()) {
            Integer u = q.poll();

            if (adjacencyList.containsKey(u)) {
                for (Integer v : adjacencyList.get(u)) {
                    if (!visited.contains(v)) {
                        int dist = distance.get(u) + 1;
                        if (v == b)
                            return dist;
                        q.add(v);
                        visited.add(v);
                        distance.put(v, dist);
                    }
                }
            }
        }
        return -1;
    }

    @Override
    public void printGraph() {
    }

}
