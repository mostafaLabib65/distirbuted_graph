package API;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ClientStub extends Remote {
    String execute(ArrayList<String> commands, int clientNum) throws IOException, InterruptedException;
}
