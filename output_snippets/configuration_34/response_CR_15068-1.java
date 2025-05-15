//<Beginning of snippet n. 0>
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PortScanner {

    private static final String[] PROTOCOLS = {"tcp", "tcp6", "udp", "udp6"};
    private static final String PROC_NET_PATH = "/proc/net/";

    public static void main(String[] args) {
        PortScanner scanner = new PortScanner();
        scanner.scanPorts();
    }

    public void scanPorts() {
        for (String protocol : PROTOCOLS) {
            File file = new File(PROC_NET_PATH + protocol);
            if (file.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.contains("LISTEN")) {
                            String[] parts = line.trim().split(" +");
                            String localAddress = parts[1]; // Adjust based on file format
                            logOpenPort(localAddress);
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error reading file: " + file.getAbsolutePath() + " - " + e.getMessage());
                }
            } else {
                System.err.println("File not found: " + file.getAbsolutePath());
            }
        }
    }

    private void logOpenPort(String port) {
        // Log the open port detected
        System.out.println("Open port detected: " + port);
        // Implement prohibition logic or alerts as needed
    }
}
//<End of snippet n. 0>