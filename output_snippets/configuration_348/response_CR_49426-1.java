//<Beginning of snippet n. 0>
private static Map<EnumSet<Scope>, List<Issue>> sScopeIssues = Maps.newHashMap();

/**
 * Issue reported by lint (not a specific detector) when it cannot even
 * parse an XML file prior to analysis
 */
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
return mClient;
}

/**
 * Returns the current phase number. The first pass is numbered 1. Only one pass
 * will be performed, unless a {@link Detector} calls {@link #requestRepeat}.
 */
assert detector instanceof Detector.ClassScanner : detector;
}
}
}
}

checkClasses(project, main);
}

if (mCanceled) {
return;
}
checkProGuard(project, main);
}
}
private void checkProGuard(Project project, Project main) {
    List<Detector> detectors = mScopeDetectors.get(Scope.PROGUARD_FILE);
    if (detectors != null) {
    }
}

/**
 * Map from VM class name to corresponding super class VM name, if available.
 * This map is typically null except <b>during</b> class processing.
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
new file mode 100644
//<End of snippet n. 2>

//<Beginning of snippet n. 3>
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.client.api.LintDriver;
import com.google.common.annotations.Beta;

import lombok.ast.Node;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.ast.AstVisitor;
import lombok.ast.MethodInvocation;

/**
 * A detector is able to find a particular problem. It might also be thought of as enforcing
 * a rule, but "rule" is a bit overloaded in ADT terminology since ViewRules are used in
// We want to distinguish this from just an *empty* list returned by the caller!
}

/**
 * Runs the detector. This method will not be called for certain specialized
 * detectors, such as {@link XmlScanner} and {@link JavaScanner}, where
@NonNull MethodNode method, @NonNull AbstractInsnNode instruction) {
}
//<End of snippet n. 3>

//<Beginning of snippet n. 4>
/**
 * @return the path to the manifest file, or null if it does not exist
 */
public File getManifestFile() {
    File manifestFile = new File(mDir, ANDROID_MANIFEST_XML);
    if (manifestFile.exists()) {
//<End of snippet n. 4>

//<Beginning of snippet n. 5>
/**
 * The analysis considers classes in the libraries for this project. These
 * will be analyzed before the classes themselves.
 */
JAVA_LIBRARIES;

/** Returns true if the given scope set corresponds to scanning a single file */
public static final EnumSet<Scope> CLASS_FILE_SCOPE = EnumSet.of(CLASS_FILE);
/** Scope-set used for detectors which are affected by the manifest only */
public static final EnumSet<Scope> MANIFEST_SCOPE = EnumSet.of(MANIFEST);
/** New Scope for OTHER */
public static final EnumSet<Scope> OTHER_SCOPE = EnumSet.of(Scope.OTHER);
//<End of snippet n. 5>

//<Beginning of snippet n. 6>
private static final String SWITCH_TABLE_PREFIX = "$SWITCH_TABLE$";  //$NON-NLS-1$
private static final String ORDINAL_METHOD = "ordinal"; //$NON-NLS-1$

private ApiLookup mApiDatabase;
private int mMinApi = -1;
private Set<String> mWarnedFields;

}
}

private int getMinSdk(Context context) {
    if (mMinApi == -1) {
        mMinApi = context.getMainProject().getMinSdk();
    }
//<End of snippet n. 6>

//<Beginning of snippet n. 7>
}
}
}

/** Add the issues found in the given jar file into the given list of issues */
//<End of snippet n. 7>

//<Beginning of snippet n. 8>
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LintUtils;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Project;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;

import java.io.File;
import java.io.IOException;

/**
 * Looks for packaged private key files.
 */
public class PrivateKeyDetector extends Detector {
/** Packaged private key files */
public static final Issue ISSUE = Issue.create(
        "PackagedPrivateKey", //$NON-NLS-1$
        8,
        Severity.WARNING,
        PrivateKeyDetector.class,
        Scope.ALL_RESOURCES_SCOPE);

/** Constructs a new {@link PrivateKeyDetector} check */
public PrivateKeyDetector() {
    return false;
}

private static void checkFolder(Context context, File dir) {
    if (dir.isDirectory()) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    checkFolder(context, file);
                } else {
                    if (isPrivateKeyFile(file)) {
                        String fileName = file.getParentFile().getName() + File.separator
                                + file.getName();
                        String message = String.format(
                                "The %1$s file seems to be a private key file. " +
                                        "Please make sure not to embed this in your APK file.", fileName);
                        context.report(ISSUE, Location.create(file), message, null);
                    }
                }
            }
        }
    }
}

@Override
public void afterCheckProject(@NonNull Context context) {
    if (!context.getProject().getReportIssues()) {
        // If this is a library project not being analyzed, ignore it
        return;
    }

    Project project = context.getProject();
    File projectFolder = project.getDir();

    for (File res : project.getResourceFolders()) {
        checkFolder(context, res);
    }
    checkFolder(context, new File(projectFolder, "assets"));

    for (File srcFolder : project.getJavaSourceFolders()) {
        checkFolder(context, srcFolder);
    }
    
    // New iteration logic for Scope.OTHER
    for (File otherFolder : project.getOtherFolders()) {
        checkFolder(context, otherFolder);
    }
}

@Override
public boolean appliesTo(@NonNull Context context, @NonNull File file) {
    return true;
}

@NonNull
//<End of snippet n. 8>