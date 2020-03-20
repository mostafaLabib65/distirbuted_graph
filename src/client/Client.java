package client;

import API.ClientStub;
import Utilities.Constants;

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

    public ArrayList<String> process(ArrayList<String> queries) throws RemoteException {
        ArrayList<String> results = stub.execute(queries);
        return results;
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        Client client = new Client("localhost");
        client.process(new ArrayList<>());
    }
}
