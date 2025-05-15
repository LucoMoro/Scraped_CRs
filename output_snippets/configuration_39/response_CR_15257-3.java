<<Beginning of snippet n. 0>>
//<Beginning of snippet n. 1>
import java.util.Arrays;
import java.util.List;

public class RootProcessChecker {

    private static final List<String> WHITELIST = Arrays.asList(
            "systemd", "bash", "sshd", "cron", "init"
    );

    public static void main(String[] args) {
        checkRootProcesses();
    }

    public static void checkRootProcesses() {
        List<String> runningProcesses = getRunningProcesses();
        for (String process : runningProcesses) {
            if (isRootProcess(process) && !isWhitelisted(process)) {
                System.out.println("Unwhitelisted root process detected: " + process);
                // Handle unwhitelisted process
            }
        }
    }

    private static List<String> getRunningProcesses() {
        // Simulating retrieval of currently running processes
        // In a real implementation, this would interface with system-specific APIs
        return Arrays.asList("systemd", "bash", "unknown_process");
    }

    private static boolean isRootProcess(String process) {
        // Logic to determine if the process is running as root
        return true; // Placeholder: Assume all are root for simulation
    }

    private static boolean isWhitelisted(String process) {
        return WHITELIST.contains(process);
    }
}
//<End of snippet n. 1>
//<End of snippet n. 0>