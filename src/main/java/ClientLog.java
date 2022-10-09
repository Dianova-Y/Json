import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ClientLog {
    String log = "productNum,amount\n";

    public void log(int productNum, int amount) {
        log += String.format(productNum + ", " + amount + "\n");
    }

    public void exportAsCSV(File logFile) throws IOException {
        try (FileWriter fileWriter = new FileWriter(logFile, true)) {
            fileWriter.write(log);
        }
    }
}

