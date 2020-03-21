package client;

import API.ClientStub;
import utilities.Constants;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Client {
    private Registry registry;
    private ClientStub stub;

    public Client(String hostName) throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry(hostName, Constants.PORT_NUMBER);
        stub = (ClientStub) registry.lookup("stub");
    }

    public String process(ArrayList<String> queries) throws RemoteException {
        String results = stub.execute(queries);
        return results;
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        Client client = new Client("localhost");
        //TODO Parse batches here and give ArrayList tp process.
        //TODO batches will be generated randomly client will sleep x time between batches
        //TODO x is an arg
        while (true){
            ArrayList<String> queries = new ArrayList<>();
            queries.add("A 1 2");
            queries.add("A 1 5");
            queries.add("D 2 4");
            queries.add("Q 1 4");
            System.out.println(client.process(queries));
        }
    }
}
