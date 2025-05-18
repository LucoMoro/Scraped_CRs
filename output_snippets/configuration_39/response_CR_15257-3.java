//<Beginning of snippet n. 0>
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

public class RootProcessDetector {

    private static final List<String> WHITELISTED_PROCESSES = List.of(
        "init", "systemd", "kthreadd", "rcu_sched", "migration", "watchdog", "ksoftirqd", "kswapd0",
        "netns", "khugepaged", "oom_reaper", "kworker", "irqbalance", "udevadm", "dhclient", "sshd", 
        "hardware_driver1", "hardware_driver2" // Expanded with hardware drivers
    );

    public static void main(String[] args) {
        List<String> unwhitelistedProcesses = new ArrayList<>();
        try {
            unwhitelistedProcesses = detectUnwhitelistedRootProcesses();
        } catch (SecurityException e) {
            System.err.println("Permission error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
        for (String process : unwhitelistedProcesses) {
            logPotentiallySuspiciousProcess(process);
        }
    }

    private static List<String> detectUnwhitelistedRootProcesses() {
        List<String> runningProcesses = getRunningProcessesAsRoot();
        List<String> unwhitelistedProcesses = new ArrayList<>();

        for (String process : runningProcesses) {
            if (!WHITELISTED_PROCESSES.contains(process)) {
                unwhitelistedProcesses.add(process);
            }
        }
        return unwhitelistedProcesses;
    }

    private static List<String> getRunningProcessesAsRoot() {
        List<String> rootProcesses = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec("ps -eo user,pid,cmd");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("root")) {
                    String[] parts = line.split("\\s+", 3);
                    if (parts.length >= 3) {
                        rootProcesses.add(parts[2]);
                    }
                }
            }
            reader.close();
            return rootProcesses;
        } catch (SecurityException se) {
            throw new SecurityException("Permission denied while retrieving running processes.", se);
        } catch (Exception e) {
            throw new SecurityException("Failed to retrieve running processes: " + e.getMessage(), e);
        }
    }

    private static void logPotentiallySuspiciousProcess(String process) {
        System.out.println("[" + System.currentTimeMillis() + "] Suspicious process detected: " + process);
    }
}
//<End of snippet n. 0>