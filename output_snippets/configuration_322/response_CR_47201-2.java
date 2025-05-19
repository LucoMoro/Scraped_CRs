//<Beginning of snippet n. 0>


package com.android.ant;

import com.android.sdklib.io.FileOp;
import org.apache.tools.ant.BuildException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

private static final String EXT_FS = "fs"; // Filterscript file extension
private static final Pattern FS_FILE_PATTERN = Pattern.compile(".*\\.fs$", Pattern.CASE_INSENSITIVE);

private class AidlProcessor implements SourceProcessor {

    @Override
    public String getSourceFileExtension() {
        return "aidl";
    }

    @Override
    public void process(String filePath, String sourceFolder, List<String> sourceFolders, Project taskProject) {
        // Processing logic...
    }

    @Override
    public void displayMessage(DisplayType type, int count) {
        // Message display logic...
    }

    public void processFilterscript(String filterscript) {
        if (filterscript == null || !FS_FILE_PATTERN.matcher(filterscript).matches()) {
            throw new BuildException("Invalid filterscript file: " + filterscript);
        }
        // Filtering and processing logic...
        // Example: read filterscript content and perform necessary operations
        File file = new File(filterscript);
        if (!file.exists() || !file.isFile()) {
            throw new BuildException("Filterscript file does not exist: " + filterscript);
        }
        // Additional processing logic here
    }

    private Map<String, String> getFilesByNameEntryFilter(List<String> folders, String filter) {
        Map<String, String> sourceFiles = new HashMap<>();

        for (String folder : folders) {
            Iterator<?> iterator = getFilesByNameEntryFilter(folder, filter);
            while (iterator.hasNext()) {
                String fileName = iterator.next().toString();
                if (FS_FILE_PATTERN.matcher(fileName).matches()) {
                    sourceFiles.put(fileName, folder);
                }
            }
        }
        return sourceFiles;
    }

    private Iterator<?> getFilesByNameEntryFilter(String folder, String filter) {
        Project taskProject = getProject();
        FileSet fs = new FileSet();
        fs.setProject(taskProject);
        fs.setDir(new File(folder));
        NameEntry include = fs.createInclude();
        include.setName(filter);
        return fs.iterator();
    }
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>


interface SourceProcessor {
    String getSourceFileExtension();
    void process(String filePath, String sourceFolder, List<String> sourceFolders, Project taskProject);
    void displayMessage(DisplayType type, int count);
    void processFilterscript(String filterscript);
}

//<End of snippet n. 1>

//<Beginning of snippet n. 2>


package com.android.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.ExecTask;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

private static final String EXT_FS = "fs"; // Filterscript file extension
private static final Pattern FS_FILE_PATTERN = Pattern.compile(".*\\.fs$", Pattern.CASE_INSENSITIVE);

public class RenderScriptTask extends MultiFilesTask {

    private String mExecutable;
    private Path mIncludePath;
    private String mGenFolder;

    @Override
    public String getSourceFileExtension() {
        return "rs";
    }

    public void processFilterscript(String filterscript) {
        if (filterscript == null || !FS_FILE_PATTERN.matcher(filterscript).matches()) {
            throw new BuildException("Invalid filterscript file: " + filterscript);
        }
        // Filtering and processing logic...
        // Example: read filterscript content and perform necessary operations
        File file = new File(filterscript);
        if (!file.exists() || !file.isFile()) {
            throw new BuildException("Filterscript file does not exist: " + filterscript);
        }
        // Additional processing logic here
    }
}

//<End of snippet n. 2>

//<Beginning of snippet n. 3>


public static final String EXT_AIDL = "aidl"; //$NON-NLS-1$
public static final String EXT_RS = "rs"; //$NON-NLS-1$
public static final String EXT_DEP = "d"; //$NON-NLS-1$
public static final String EXT_SO = "so"; //$NON-NLS-1$
public static final String DOT_AIDL = "." + EXT_AIDL;
public static final String DOT_RS = "." + EXT_RS;
public static final String DOT_DEP = "." + EXT_DEP;
public static final String DOT_SO = "." + EXT_SO;

//<End of snippet n. 3>

//<Beginning of snippet n. 4>


import static com.android.SdkConstants.DOT_DEP;
import static com.android.SdkConstants.DOT_JAVA;
import static com.android.SdkConstants.DOT_RS;

import com.android.SdkConstants;

public final static String RE_AIDL_EXT = "\\" + DOT_AIDL + "$"; //$NON-NLS-1$ //$NON-NLS-2$
public final static String RE_RS_EXT = "\\" + DOT_RS + "$"; //$NON-NLS-1$ //$NON-NLS-2$
public final static String RE_DEP_EXT = "\\" + DOT_DEP + "$"; //$NON-NLS-1$ //$NON-NLS-2$

//<End of snippet n. 4>

//<Beginning of snippet n. 5>


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

private static Pattern sAidlPattern1 = Pattern.compile("^(.+?):(\\d+):?\\s(.+)$"); //$NON-NLS-1$

private enum AidlType {
    UNKNOWN, INTERFACE, PARCELABLE;
}

@Override
protected String getExtension() {
    return SdkConstants.EXT_AIDL;
}

//<End of snippet n. 5>

//<Beginning of snippet n. 6>


import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.resources.ResourceFolderType;
import com.android.sdklib.IAndroidTarget;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

private static Pattern sLlvmPattern1 = Pattern.compile("^(.+?):(\\d+):(\\d+):\\s(.+)$"); //$NON-NLS-1$

private static class RsChangeHandler extends SourceChangeHandler {

    @Override
    public boolean handleSourceFile(IFile file, int kind) {
        if (mProcessor.getExtension().equals(file.getFileExtension())) {
            if (kind == IResourceDelta.REMOVED) {
                mRemoved.add(file);
            } else {
                // Handle other logic...
            }
        }
        return false;
    }

    @Override
    protected String getExtension() {
        return SdkConstants.EXT_RS;
    }
}

//<End of snippet n. 6>

//<Beginning of snippet n. 7>


public void handleSourceFile(IFile file, int kind) {
    if (mProcessor.getExtension().equals(file.getFileExtension())) {
        if (kind == IResourceDelta.REMOVED) {
            mRemoved.add(file);
        } else {
            // Handle processing logic...
        }
    }
}

//<End of snippet n. 7>

//<Beginning of snippet n. 8>


import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

protected abstract String getExtension();

protected abstract String getSavePropertyName();

if (r.exists() && getExtension().equalsIgnoreCase(r.getFileExtension())) {
    mFiles.put((IFile) r, new SourceFileData((IFile) r));
}

//<End of snippet n. 8>