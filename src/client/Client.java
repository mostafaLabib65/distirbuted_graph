package client;

import API.ClientStub;
import utilities.Batcher;
import utilities.Constants;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;

public class Client {
    private Registry registry;
    private ClientStub stub;
    private int clientNum;
    public Client(String hostName, int clientNum) throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry(hostName, Constants.PORT_NUMBER);
        stub = (ClientStub) registry.lookup("stub");
        this.clientNum = clientNum;
    }

    public String process(ArrayList<String> queries) throws IOException, InterruptedException {
        String results = stub.execute(queries, this.clientNum);
        System.out.println(results);
        return results;
    }

    public static void main(String[] args) throws IOException, NotBoundException, InterruptedException {
         Client client = new Client("156.212.50.151", Integer.parseInt(args[1]));

        String MODE = "DEFAULT";
        long SLEEP_TIME = 10000L;
        Batcher batcher;

        long timeToSleep = args.length > 0 ? Long.parseLong(args[0]) : SLEEP_TIME;
        int runs = 0;
        while(true){
            if(MODE.equals("DEFAULT")){
                batcher = new Batcher();
            }
            // Optional to pass params
            else {
                HashMap<String, Object> params = new HashMap<>();
                params.put("maxBatchSize", 70);
                batcher = new Batcher(params);
            }

            ArrayList<String> queries = batcher.getBatch();

            System.out.println("=========================");
            System.out.println("Current run: " + runs);
            for(String str: queries){
                System.out.println(str);
            }
            System.out.println("=========================");

            client.process(queries);
            Thread.sleep(timeToSleep);
            runs++;
        }
    }
}
