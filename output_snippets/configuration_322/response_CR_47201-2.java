//<Beginning of snippet n. 0>
package com.android.ant;

import com.android.sdklib.io.FileOp;

import org.apache.tools.ant.BuildException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

private class AidlProcessor implements SourceProcessor {

    private static final String EXT_FILTERSCRIPT = "filterscript"; // Define filter script extension

    @Override
    public String getSourceFileExtension() {
        return EXT_AIDL;
    }

    // Other methods...

    private Map<String, String> getFilesByNameEntryFilter(List<String> folders, String filter) {
        Map<String, String> sourceFiles = new HashMap<String, String>();

        for (String folder : folders) {
            Iterator<?> iterator = getFilesByNameEntryFilter(folder, filter);

            while (iterator.hasNext()) {
                sourceFiles.put(iterator.next().toString(), folder);
            }
        }

        return sourceFiles;
    }

    // Modifying to handle filter script files
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
}

Project taskProject = getProject();
String extension = processor.getSourceFileExtension(); // Use new extension properly

ArrayList<String> sourceFolders = new ArrayList<String>();
Map<String, String> sourceFiles = getFilesByNameEntryFilter(sourceFolders,
                "**/*." + extension);

if (sourceFiles.size() > 0) {
    processor.displayMessage(DisplayType.FOUND, sourceFiles.size());
}

if (sourceFilePath.toLowerCase(Locale.US).endsWith("." + extension)) {
    String sourceFolder = sourceFiles.get(sourceFilePath);
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

/**
 * Task to execute renderscript.
 */
public class RenderScriptTask extends MultiFilesTask {
    private String mExecutable;
    private Path mIncludePath;
    private String mGenFolder;

    @Override
    public String getSourceFileExtension() {
        return EXT_RS;
    }

    // Other methods...
}
//<End of snippet n. 2>

//<Beginning of snippet n. 3>
public static final String EXT_AIDL = "aidl"; //$NON-NLS-1$
public static final String EXT_RS = "rs"; //$NON-NLS-1$
public static final String EXT_DEP = "d"; //$NON-NLS-1$
public static final String EXT_FILTERSCRIPT = "filterscript"; //$NON-NLS-1$

public static final String DOT_AIDL = "." + EXT_AIDL; //$NON-NLS-1$
public static final String DOT_RS = "." + EXT_RS; //$NON-NLS-1$
public static final String DOT_DEP = "." + EXT_DEP; //$NON-NLS-1$
public static final String DOT_FILTERSCRIPT = "." + EXT_FILTERSCRIPT; //$NON-NLS-1$
//<End of snippet n. 3>

//<Beginning of snippet n. 4>
import static com.android.SdkConstants.DOT_DEP;
import static com.android.SdkConstants.DOT_JAVA;
import static com.android.SdkConstants.DOT_RS;
import static com.android.SdkConstants.DOT_FILTERSCRIPT;

import com.android.SdkConstants;

public final static String RE_AIDL_EXT = "\\" + DOT_AIDL + "$"; //$NON-NLS-1$ //$NON-NLS-2$
public final static String RE_RS_EXT = "\\" + DOT_RS + "$"; //$NON-NLS-1$ //$NON-NLS-2$
public final static String RE_DEP_EXT = "\\" + DOT_DEP + "$"; //$NON-NLS-1$ //$NON-NLS-2$
public final static String RE_FILTERSCRIPT_EXT = "\\" + DOT_FILTERSCRIPT + "$"; //$NON-NLS-1$ //$NON-NLS-2$
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
                addFileToCompile(file);
                return true;
            }
        }
        return false;
    }
}
@Override
protected String getExtension() {
    return SdkConstants.EXT_RS; 
}
//<End of snippet n. 6>

//<Beginning of snippet n. 7>
public void handleSourceFile(IFile file, int kind) {
    if (mProcessor.getExtension().equals(file.getFileExtension())) {
        if (kind == IResourceDelta.REMOVED) {
            mRemoved.add(file);
        } else {
            addFileToCompile(file);
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