package server;


import API.ClientStub;
import utilities.Constants;
import utilities.Graph;
import utilities.GraphInitializer;
import utilities.Logger;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    private static Server server;
    private Registry serverRegistry;
    private ClientStub stub;
    private Graph graph = GraphInitializer.initializeGraph("input.txt");
    private Logger logger = new Logger();
    private Server() throws RemoteException, IOException {
        System.setProperty("java.rmi.server.hostname","156.212.50.151");
//        System.setProperty("remoting.bind_by_host","false");
        serverRegistry = LocateRegistry.createRegistry(5001);
    }

    public static Server getInstance() throws IOException, RemoteException {
        if (server == null) {
            server = new Server();
        }
        return server;
    }

    public void start() throws RemoteException {
        stub = new BatchProcessor(this.graph, this.logger);
        ClientStub remoteProcessor = (ClientStub) UnicastRemoteObject.exportObject(stub, 5010);
        serverRegistry.rebind("stub", remoteProcessor);
        System.out.println("Server is running");
    }

    public static void main(String[] args) throws IOException, RemoteException {
        Server serverTest = Server.getInstance();
        serverTest.start();
    }

}
