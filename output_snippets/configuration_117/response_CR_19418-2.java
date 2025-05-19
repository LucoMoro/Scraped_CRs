//<Beginning of snippet n. 0>


import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.ImportTask;
import org.apache.tools.ant.types.Path;
import org.xml.sax.InputSource;

import java.io.File;

public final class SetupTask extends ImportTask {
    private final static String RULES_MAIN = "main_rules.xml";
    private final static String MINIMUM_ANT_VERSION = "1.10.0";

    public void execute() throws BuildException {
        String antVersion = getProject().getProperty("ant.version");
        if (antVersion.compareTo(MINIMUM_ANT_VERSION) < 0) {
            throw new BuildException("Ant version must be at least " + MINIMUM_ANT_VERSION + ". Current version: " + antVersion);
        }
        
        Project antProject = getProject();
        File sdkDir = TaskHelper.getSdkLocation(antProject);
        String sdkOsPath = sdkDir.getPath();
        
        Path libraries = new Path(antProject);

        System.out.println("Current Ant version: " + antVersion + " - compatible");
    }
}

//<End of snippet n. 0>