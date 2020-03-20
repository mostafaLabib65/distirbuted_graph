package API;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ClientStub extends Remote {
    ArrayList<String> execute(ArrayList<String> commands) throws RemoteException;
}
