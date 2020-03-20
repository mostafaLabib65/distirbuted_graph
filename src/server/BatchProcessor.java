package server;

import API.ClientStub;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class BatchProcessor implements ClientStub {
    @Override
    public ArrayList<String> execute(ArrayList<String> queries) throws RemoteException {
        System.out.println("Start executing!");
        return null;
    }
}
