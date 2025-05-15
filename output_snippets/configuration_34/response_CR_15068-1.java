<<Beginning of snippet n. 0>>
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListeningPortScanner {

    private static final Logger logger = Logger.getLogger(ListeningPortScanner.class.getName());

    public List<String> scanForListeningPorts() {
        List<String> listeningPorts = new ArrayList<>();
        File tcpFile = new File("/proc/net/tcp");
        File udpFile = new File("/proc/net/udp");

        try {
            listeningPorts.addAll(parseFileForListeningPorts(tcpFile));
            listeningPorts.addAll(parseFileForListeningPorts(udpFile));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error accessing /proc/net directory: " + e.getMessage(), e);
        }

        return listeningPorts;
    }

    private List<String> parseFileForListeningPorts(File file) throws IOException {
        List<String> listeningPorts = new ArrayList<>();
        if (!file.exists() || !file.canRead()) {
            throw new IOException("File not accessible: " + file.getAbsolutePath());
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split("\\s+");
                if (columns.length > 3 && columns[3].equals("0A")) { // Indicates the state is "LISTEN"
                    String localAddress = columns[1];
                    String[] addressParts = localAddress.split(":");
                    String ipAddress = addressParts[0];
                    String port = addressParts[1];
                    listeningPorts.add(ipAddress + ":" + Integer.parseInt(port, 16));
                }
            }
        }
        return listeningPorts;
    }

    public static void main(String[] args) {
        ListeningPortScanner scanner = new ListeningPortScanner();
        List<String> ports = scanner.scanForListeningPorts();
        for (String port : ports) {
            logger.info("Listening port detected: " + port);
        }
    }
}
<<End of snippet n. 0>>