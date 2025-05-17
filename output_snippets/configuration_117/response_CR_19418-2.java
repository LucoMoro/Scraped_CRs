//<Beginning of snippet n. 0>

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.ImportTask;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Path.PathElement;
import org.xml.sax.InputSource;
import org.apache.tools.ant.Main;

import java.io.File;

public final class SetupTask extends ImportTask {
    private final static String RULES_MAIN = "main_rules.xml";
    private final static String REQUIRED_ANT_VERSION = System.getProperty("required.ant.version", "1.10.0");

    public void execute() throws BuildException {
        Project antProject = getProject();

        try {
            String currentAntVersion = Main.getAntVersion();

            if (compareVersions(currentAntVersion, REQUIRED_ANT_VERSION) < 0) {
                throw new BuildException("Incompatible Ant version. Required: " + REQUIRED_ANT_VERSION + ", Current: " + currentAntVersion);
            }
        } catch (Exception e) {
            throw new BuildException("Error checking Ant version: " + e.getMessage(), e);
        }

        // Removed unused variables
        // File sdkDir = TaskHelper.getSdkLocation(antProject);
        // String sdkOsPath = sdkDir.getPath();

        // Removed unused return statement
        // return libraries; 
    }

    private int compareVersions(String version1, String version2) {
        String[] parts1 = version1.split("\\.");
        String[] parts2 = version2.split("\\.");
        for (int i = 0; i < Math.max(parts1.length, parts2.length); i++) {
            int v1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
            int v2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;
            if (v1 != v2) {
                return Integer.compare(v1, v2);
            }
        }
        return 0;
    }
}

//<End of snippet n. 0>