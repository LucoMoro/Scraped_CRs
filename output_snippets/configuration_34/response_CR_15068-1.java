<<Beginning of snippet n. 0>>
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListeningPortsScanner {
    private static final Logger LOGGER = Logger.getLogger(ListeningPortsScanner.class.getName());
    private static final List<Integer> PROHIBITED_PORTS = List.of(/* Define prohibited ports here */);
    private boolean prohibitedPortsDetected = false;

    public List<Integer> getListeningPorts() {
        List<Integer> listeningPorts = new ArrayList<>();
        String[] files = {"/proc/net/tcp", "/proc/net/udp"};

        for (String file : files) {
            try (BufferedReader br = new BufferedReader(new FileReader(new File(file)))) {
                // Skip the first line (headers)
                br.readLine();
                String line;

                while ((line = br.readLine()) != null) {
                    String[] parts = line.trim().split("\\s+");
                    if (parts.length > 0) {
                        String localAddress = parts[1];
                        if (localAddress.contains(":")) {
                            String[] addressParts = localAddress.split(":");
                            int port = Integer.parseInt(addressParts[addressParts.length - 1], 16);
                            if (!PROHIBITED_PORTS.contains(port)) {
                                listeningPorts.add(port);
                            } else {
                                LOGGER.severe("Prohibited port detected: " + port);
                                prohibitedPortsDetected = true;
                                enforceSecurityPolicy(port);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error accessing file: " + file, e);
            }
        }
        return listeningPorts;
    }

    private void enforceSecurityPolicy(int port) {
        // Implement logic to raise alerts or apply firewall rules to the prohibited port
    }

    public boolean areProhibitedPortsDetected() {
        return prohibitedPortsDetected;
    }

    public static void main(String[] args) {
        ListeningPortsScanner scanner = new ListeningPortsScanner();
        List<Integer> ports = scanner.getListeningPorts();
        if (!ports.isEmpty()) {
            System.out.println("Listening ports: " + ports);
        } else {
            System.out.println("No listening ports found.");
        }
        if (scanner.areProhibitedPortsDetected()) {
            System.out.println("Prohibited ports were detected during the scan.");
        }
    }
}
<<End of snippet n. 0>>