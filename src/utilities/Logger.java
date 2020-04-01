package utilities;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.Semaphore;

public class Logger {
    private FileWriter writer;
    private Semaphore writerLock = new Semaphore(1);

    Date date= new Date();
    public Logger() throws IOException {
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);
        String name = "log-" + ts + ".txt";
        writer = new FileWriter(name);
    }

    public void log_batch(int length, int clientNum, long executionTime) throws IOException, InterruptedException {
        writerLock.acquire();
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);
        writer.write(ts + " " + clientNum + " " + length + " " + executionTime + " ms\n");
        writerLock.release();
    }
}
