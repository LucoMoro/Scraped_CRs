<<Beginning of snippet n. 0>>
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RootProcessDetector {
    
    private static final List<String> WHITELISTED_PROCESSES = List.of(
        "init",  // Essential system process
        "system_server",  // Core Android service manager
        "mediaserver",  // Handles media playback
        "zygote",  // Main process for launching apps
        "servicemanager",  // Manages system services
        "adbd"  // Android Debug Bridge
    );

    public static void main(String[] args) {
        List<String> rootProcesses = getRootProcesses();
        outputRootProcesses(rootProcesses);
    }

    private static List<String> getRootProcesses() {
        List<String> rootProcesses = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec("ps");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("root")) {
                    rootProcesses.add(line.split("\\s+")[0]); // Assuming the first column is the process name
                }
            }
        } catch (Exception e) {
            System.err.println("Error retrieving root processes: " + e.getMessage());
        }
        return rootProcesses;
    }

    private static void outputRootProcesses(List<String> rootProcesses) {
        System.out.println("Detected root processes:");
        for (String process : rootProcesses) {
            if (WHITELISTED_PROCESSES.contains(process)) {
                System.out.println(process + " - Whitelisted: Essential system process.");
            } else {
                System.out.println(process + " - Not Whitelisted: Further review needed.");
            }
        }
    }
}
<<End of snippet n. 0>>