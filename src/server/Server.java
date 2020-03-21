package server;


import API.ClientStub;
import utilities.Constants;
import utilities.Graph;
import utilities.GraphInitializer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    private static Server server;
    private Registry serverRegistry;
    private ClientStub stub;
    private Graph graph = GraphInitializer.initializeGraph("input.txt");

    private Server() throws RemoteException {
//        System.setProperty("java.rmi.server.hostname","127.0.0.1");
//        System.setProperty("remoting.bind_by_host","false");
        serverRegistry = LocateRegistry.createRegistry(Constants.PORT_NUMBER);
    }

    public static Server getInstance() throws RemoteException {
        if (server == null) {
            server = new Server();
        }
        return server;
    }

    public void start() throws RemoteException {
        stub = new BatchProcessor(this.graph);
        ClientStub remoteProcessor = (ClientStub) UnicastRemoteObject.exportObject(stub, 0);
        serverRegistry.rebind("stub", remoteProcessor);
        System.out.println("Server is running");
    }

    public static void main(String[] args) throws RemoteException {
        Server serverTest = Server.getInstance();
        serverTest.start();
    }

}
