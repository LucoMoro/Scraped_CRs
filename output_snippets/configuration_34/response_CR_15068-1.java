<<Beginning of snippet n. 0>>
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PortScanner {

    private static final String TCP_PATH = "/proc/net/tcp";
    private static final String UDP_PATH = "/proc/net/udp";
    private static final int[] PROHIBITED_PORTS = { 23, 80, 443 }; // Example prohibited ports

    public static void main(String[] args) {
        PortScanResult result = scanListeningPorts();
        reportListeningPorts(result);
        if (result.isProhibitedDetected()) {
            takeEnforcementAction(result.getListeningPorts());
        }
    }

    private static PortScanResult scanListeningPorts() {
        Map<Integer, String> listeningPorts = new HashMap<>();
        boolean prohibitedDetected = false;
        prohibitedDetected |= scanFile(TCP_PATH, listeningPorts);
        prohibitedDetected |= scanFile(UDP_PATH, listeningPorts);
        return new PortScanResult(listeningPorts, prohibitedDetected);
    }

    private static boolean scanFile(String filePath, Map<Integer, String> portsMap) {
        boolean prohibitedDetected = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                prohibitedDetected |= parseEntry(line, portsMap);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath + " - " + e.getMessage());
            if (e.getMessage().contains("Permission denied")) {
                System.err.println("Permission denied when accessing: " + filePath);
            } else if (e.getMessage().contains("No such file or directory")) {
                System.err.println("File not found: " + filePath);
            } else {
                System.err.println("Unexpected error: " + e.getMessage());
            }
        }
        return prohibitedDetected;
    }

    private static boolean parseEntry(String entry, Map<Integer, String> portsMap) {
        String[] parts = entry.split("\\s+");
        if (parts.length < 4) return false;
        String state = parts[3];
        if ("0A".equals(state) || "0B".equals(state)) {
            int port = Integer.parseInt(parts[1].split(":")[1], 16);
            String pid = parts[8];
            portsMap.put(port, pid);
            if (isProhibitedPort(port)) {
                System.err.println("Prohibited port detected: " + port);
                return true;
            }
        }
        return false;
    }

    private static boolean isProhibitedPort(int port) {
        for (int prohibitedPort : PROHIBITED_PORTS) {
            if (port == prohibitedPort) {
                return true;
            }
        }
        return false;
    }

    private static void reportListeningPorts(PortScanResult result) {
        if (result.getListeningPorts().isEmpty()) {
            System.out.println("No listening ports detected.");
        } else {
            for (Map.Entry<Integer, String> entry : result.getListeningPorts().entrySet()) {
                System.out.println("Listening port: " + entry.getKey() + " (PID: " + entry.getValue() + ")");
            }
        }
        if (result.isProhibitedDetected()) {
            System.out.println("Warning: Prohibited ports were detected!");
        }
    }

    private static void takeEnforcementAction(Map<Integer, String> listeningPorts) {
        for (Integer port : listeningPorts.keySet()) {
            if (isProhibitedPort(port)) {
                System.err.println("Taking enforcement action against port: " + port);
                raiseAlert(port);
                applyFirewallRule(port);
            }
        }
    }

    private static void raiseAlert(int port) {
        System.out.println("Alert raised for prohibited port: " + port);
    }

    private static void applyFirewallRule(int port) {
        System.out.println("Firewall rule applied for prohibited port: " + port);
    }

    private static class PortScanResult {
        private final Map<Integer, String> listeningPorts;
        private final boolean prohibitedDetected;

        public PortScanResult(Map<Integer, String> listeningPorts, boolean prohibitedDetected) {
            this.listeningPorts = listeningPorts;
            this.prohibitedDetected = prohibitedDetected;
        }

        public Map<Integer, String> getListeningPorts() {
            return listeningPorts;
        }

        public boolean isProhibitedDetected() {
            return prohibitedDetected;
        }
    }
}
//<<End of snippet n. 0>>