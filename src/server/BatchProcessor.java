package server;

import API.ClientStub;
import utilities.Graph;
import utilities.Logger;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class BatchProcessor implements ClientStub {
    private Graph graph;
    private Logger logger;
    public BatchProcessor(Graph graph, Logger logger){
        this.graph = graph;
        this.logger = logger;
    }
    @Override
    public String execute(ArrayList<String> queries, int clientNum) throws IOException, InterruptedException {
        System.out.println("Start executing!");
        StringBuilder result = new StringBuilder();
        long startTime = System.nanoTime();
        try {
            for(String query: queries){
                String[] splited_query = query.split(" ");
                int a = Integer.parseInt(splited_query[1]);
                int b = Integer.parseInt(splited_query[2]);
                if(splited_query[0].equals("A")){
                    graph.addEdge(a, b);
                }else if (splited_query[0].equals("D")){
                    graph.removeEdge(a, b);
                }else {
                    int shortestPath = graph.getShortestPath(a, b);
                    result.append(shortestPath);
                    result.append("\n");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        long durationInMs = (endTime - startTime)/1000000;
        logger.log_batch(queries.size(), clientNum, durationInMs);
        return result.toString();
    }
}
