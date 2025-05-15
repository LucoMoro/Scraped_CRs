//<Beginning of snippet n. 0>
import java.util.ArrayList;
import java.util.List;

public class RootProcessDetector {
    private static final List<String> WHITELISTED_PROCESSES = List.of(
        "system_server",
        "hwservicemanager",
        "mediaserver"
        // Add other legitimate processes here.
    );

    public static void main(String[] args) {
        List<String> rootProcesses = getRunningRootProcesses();
        List<String> unauthorizedProcesses = new ArrayList<>();

        for (String process : rootProcesses) {
            if (!WHITELISTED_PROCESSES.contains(process)) {
                unauthorizedProcesses.add(process);
            }
        }

        logWaiverQuestions(unauthorizedProcesses);
        validateProcesses(rootProcesses);
    }

    private static List<String> getRunningRootProcesses() {
        List<String> processes = new ArrayList<>();
        // Logic to retrieve currently running root processes on the device.
        // This would typically call system APIs to get processes.
        return processes;
    }

    private static void logWaiverQuestions(List<String> unauthorizedProcesses) {
        for (String process : unauthorizedProcesses) {
            System.out.println("Waiver Question: Why is the unauthorized root process " + process + " running?");
        }
    }

    private static void validateProcesses(List<String> rootProcesses) {
        for (String process : rootProcesses) {
            assert WHITELISTED_PROCESSES.contains(process) : "Unauthorized process detected: " + process;
        }
    }
}
//<End of snippet n. 0>