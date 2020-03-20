package server;


import API.ClientStub;
import Utilities.Constants;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    private static Server server;
    private Registry serverRegistry;
    private ClientStub stub;

    private Server() throws RemoteException {
        serverRegistry = LocateRegistry.createRegistry(Constants.PORT_NUMBER);
    }

    public static Server getInstance() throws RemoteException {
        if (server == null) {
            server = new Server();
        }
        return server;
    }

    public void start() throws RemoteException {
        stub = new BatchProcessor();
        ClientStub remoteProcessor = (ClientStub) UnicastRemoteObject.exportObject(stub, 0);
        serverRegistry.rebind("stub", remoteProcessor);
        System.out.println("Server is running");
    }

    public static void main(String[] args) throws RemoteException {
        Server serverTest = Server.getInstance();
        serverTest.start();
    }

}
