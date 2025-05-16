```
<<Beginning of snippet n. 0>>
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RootProcessChecker {
    private static final Set<String> WHITELIST = new HashSet<>(Arrays.asList(
        "systemd", "init", "login", "sshd", "cron" // Add more whitelisted processes as needed
    ));

    public static void main(String[] args) {
        try {
            checkUnwhitelistedRootProcesses();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void checkUnwhitelistedRootProcesses() throws IOException {
        String line;
        Process process = Runtime.getRuntime().exec("ps -eo user,comm");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while ((line = reader.readLine()) != null) {
                String[] processInfo = line.trim().split("\\s+");
                if (processInfo.length >= 2 && "root".equals(processInfo[0])) {
                    String processName = processInfo[1];
                    if (!WHITELIST.contains(processName)) {
                        System.out.println("Unwhitelisted root process detected: " + processName);
                    }
                }
            }
        }
    }
}
<<End of snippet n. 0>>