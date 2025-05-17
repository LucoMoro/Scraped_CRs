<<Beginning of snippet n. 0>>
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListeningPortsChecker {

    private static final Logger logger = Logger.getLogger(ListeningPortsChecker.class.getName());

    public static void main(String[] args) {
        try {
            Result result = checkListeningPorts();
            if (!result.isCompliant()) {
                logger.warning("Non-compliance detected. Listening ports: " + result.getListeningPorts());
            } else {
                logger.info("No listening ports detected. Compliance is achieved.");
            }
        } catch (IOException e) {
            logger.severe("Error accessing /proc/net directory: " + e.getMessage());
        }
    }

    public static Result checkListeningPorts() throws IOException {
        List<String> listeningPorts = new ArrayList<>();
        String[] files = {"/proc/net/tcp", "/proc/net/tcp6", "/proc/net/udp", "/proc/net/udp6"};
        
        for (String filePath : files) {
            File file = new File(filePath);
            if (file.exists() && file.isFile() && file.canRead()) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split("\\s+");
                        if (parts.length > 3 && parts[3].equals("0A")) {
                            listeningPorts.add(parts[1]);
                        }
                    }
                } catch (IOException e) {
                    logger.warning("Error reading file " + filePath + ": " + e.getMessage());
                }
            } else {
                logger.warning("Cannot access file " + filePath + ".");
            }
        }
        return new Result(listeningPorts);
    }

    public static class Result {
        private final List<String> listeningPorts;

        public Result(List<String> listeningPorts) {
            this.listeningPorts = listeningPorts;
        }

        public boolean isCompliant() {
            return listeningPorts.isEmpty();
        }

        public List<String> getListeningPorts() {
            return listeningPorts;
        }
    }
}
<<End of snippet n. 0>>