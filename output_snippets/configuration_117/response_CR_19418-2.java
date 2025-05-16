//<Beginning of snippet n. 0>


import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.ImportTask;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Path.PathElement;
import org.xml.sax.InputSource;

import java.io.File;

public final class SetupTask extends ImportTask {
    // main rules file
    private final static String RULES_MAIN = "main_rules.xml";
    private final static String MIN_REQUIRED_ANT_VERSION = "1.10.0";

    // test rules file - depends on android_rules.xml
    public void execute() throws BuildException {
        Project antProject = getProject();
        
        String antVersion = (String) antProject.getProperties().get("ant.version");
        if (antVersion == null || !isVersionSupported(antVersion)) {
            throw new BuildException("Unsupported Ant version: " + antVersion +
                    ". Minimum required version is " + MIN_REQUIRED_ANT_VERSION +
                    ". Please upgrade your Ant installation.");
        }

        // get the SDK location
        File sdkDir = TaskHelper.getSdkLocation(antProject);
        String sdkOsPath = sdkDir.getPath();

        return libraries;
    }

    private boolean isVersionSupported(String version) {
        String[] minVersionParts = MIN_REQUIRED_ANT_VERSION.split("\\.");
        String[] currentVersionParts = version.split("\\.");
        for (int i = 0; i < Math.max(minVersionParts.length, currentVersionParts.length); i++) {
            int minPart = (i < minVersionParts.length) ? Integer.parseInt(minVersionParts[i]) : 0;
            int currentPart = (i < currentVersionParts.length) ? Integer.parseInt(currentVersionParts[i]) : 0;
            if (currentPart < minPart) {
                return false;
            } else if (currentPart > minPart) {
                return true;
            }
        }
        return true;
    }
}

//<End of snippet n. 0>