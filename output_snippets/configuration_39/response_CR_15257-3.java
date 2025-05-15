```
//<Beginning of snippet n. 0>
import java.util.HashSet;
import java.util.Set;

public class RootProcessDetector {
    private static final Set<String> WHITELISTED_PROCESSES = new HashSet<>();
    
    static {
        // Define a comprehensive whitelist of allowed root processes
        WHITELISTED_PROCESSES.add("system_server");
        WHITELISTED_PROCESSES.add("mdnsd");
        WHITELISTED_PROCESSES.add("logd");
        WHITELISTED_PROCESSES.add("zygote");
        // Add other legitimate root processes as needed...
    }

    public boolean isProcessWhitelisted(String processName) {
        return WHITELISTED_PROCESSES.contains(processName);
    }

    public void detectRootProcesses() {
        Set<String> runningProcesses = getRunningProcesses(); // Assume this method provides currently running processes
        for (String process : runningProcesses) {
            if (!isProcessWhitelisted(process)) {
                // Handle unwhitelisted processes
                flagUnwhitelistedProcess(process);
            }
        }
    }

    private void flagUnwhitelistedProcess(String process) {
        System.out.println("Warning: Unwhitelisted root process detected - " + process);
    }

    // For testing purposes
    public static void runTests() {
        RootProcessDetector detector = new RootProcessDetector();
        
        // Test for whitelisted process
        assert detector.isProcessWhitelisted("system_server");
        
        // Test for non-whitelisted process
        assert !detector.isProcessWhitelisted("unknown_process");
        
        // Simulate detection of root processes
        detector.detectRootProcesses(); // Should correctly flag unlisted processes
        
        // Add edge cases handling as necessary
    }
    
    // Assume a method to retrieve running processes for real implementation
    private Set<String> getRunningProcesses() {
        // This should return the actual set of processes running on the device
        return new HashSet<>(); // Placeholder for actual implementation
    }

    public static void main(String[] args) {
        runTests();
    }
}
//<End of snippet n. 0>