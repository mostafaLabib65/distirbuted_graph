package utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Batcher {


    // Graph params
    private int graphStartingNodes;
    private int nextNodeCounter;

    // Batch params
    private int maxBatchSize;
    private int minBatchSize;

    // Probability params
    private double queryOperationP;
    private double addOperationP;
    private double existingNodeP;
    private double addNewNodeP;

    public Batcher() {
        graphStartingNodes = 4;
        nextNodeCounter = graphStartingNodes + 1;
        maxBatchSize = 10;
        minBatchSize = 4;
        queryOperationP = 0.5;
        addOperationP = 0.9;
        existingNodeP = 0.75;
        addNewNodeP = 0.5;
    }

    public Batcher(HashMap<String, Object> params) {
        graphStartingNodes = params.get("graphStartingNodes") != null ? (int) params.get("graphStartingNodes") : 4;
        nextNodeCounter = params.get("nextNodeCounter") != null ? (int) params.get("nextNodeCounter") : 5;
        maxBatchSize = params.get("maxBatchSize") != null ? (int) params.get("maxBatchSize") : 10;
        minBatchSize = params.get("minBatchSize") != null ? (int) params.get("minBatchSize") : 4;
        queryOperationP = params.get("queryOperationP") != null ? (double) params.get("queryOperationP") : 0.5;
        addOperationP = params.get("addOperationP") != null ? (double) params.get("addOperationP") : 0.9;
        existingNodeP = params.get("existingNodeP") != null ? (double) params.get("existingNodeP") : 0.75;
        addNewNodeP = params.get("addNewNodeP") != null ? (double) params.get("addNewNodeP") : 0.5;
    }

    private ArrayList<String> batch = new ArrayList<>();

    public ArrayList<String> getBatch() {
        Random random = new Random();

        // Random batch size
        int randomBatchSize = random.nextInt(maxBatchSize) + minBatchSize;

        // Assembling the operations
        for(int i = 0 ; i < randomBatchSize ; i++){
            boolean forceExistingNodeSelection = false;
            StringBuilder builder = new StringBuilder();

            // Uniform sample for operation selection
            double sample = random.nextDouble();

            // Sample QUERY operation if value in range [0, queryOperationP)
            if(sample < queryOperationP) {
                builder.append("Q ");
                forceExistingNodeSelection = true;
            }
            // Sample ADD operation if value in range [queryOperationP, addOperationP)
            else if (queryOperationP <= sample && sample < addOperationP) {
                builder.append("A ");
            }
            // Sample DELETE operation if value in range [addOperationP, 1.0]
            else {
                builder.append("D ");
                forceExistingNodeSelection = true;
            }


            for(int j = 0 ; j < 2 ; j++) {
                // Uniform sample for node selection
                sample = random.nextDouble();
                int number;

                // Sample existing node uniformly from original graph if value in range [0, existingNodeP)
                if(forceExistingNodeSelection || sample < existingNodeP){
                    number = random.nextInt(graphStartingNodes) + 1;
                }
                // Sample new node
                else {
                    // Choose current next node or new node with probability 0.5
                    if(random.nextDouble() < addNewNodeP) {
                        number = nextNodeCounter;
                    } else {
                        number = ++nextNodeCounter;
                        graphStartingNodes++;
                    }
                }

                builder.append(number);

                if (j!=1) {
                    builder.append(" ");
                }
            }

            batch.add(builder.toString());
        }

        return batch;
    }
}
