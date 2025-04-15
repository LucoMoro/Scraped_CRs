/*Cache applicable issue lists per scope set

Keep sets of applicable issues for each scope set. This
should make incremental lint slightly more efficient.

Also add a check that icon file extensions match the
actual file format.

Change-Id:I4a7f5e19b91aa3613480aa831ec42065528f02d3*/




//Synthetic comment -- diff --git a/common/src/com/android/SdkConstants.java b/common/src/com/android/SdkConstants.java
//Synthetic comment -- index 04fa987..833acdc 100644

//Synthetic comment -- @@ -835,6 +835,7 @@
public static final String DOT_XML = ".xml";                       //$NON-NLS-1$
public static final String DOT_GIF = ".gif";                       //$NON-NLS-1$
public static final String DOT_JPG = ".jpg";                       //$NON-NLS-1$
    public static final String DOT_JPEG = ".jpeg";                     //$NON-NLS-1$
public static final String DOT_PNG = ".png";                       //$NON-NLS-1$
public static final String DOT_9PNG = ".9.png";                    //$NON-NLS-1$
public static final String DOT_JAVA = ".java";                     //$NON-NLS-1$








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/IssueRegistry.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/IssueRegistry.java
//Synthetic comment -- index e780d79..f9c3d0e 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.google.common.annotations.Beta;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Collections;
//Synthetic comment -- @@ -44,6 +45,7 @@
public abstract class IssueRegistry {
private static List<Category> sCategories;
private static Map<String, Issue> sIdToIssue;
    private static Map<EnumSet<Scope>, List<Issue>> sScopeIssues = Maps.newHashMap();

/**
* Issue reported by lint (not a specific detector) when it cannot even
//Synthetic comment -- @@ -92,6 +94,45 @@
public abstract List<Issue> getIssues();

/**
     * Returns all available issues of a given scope (regardless of whether
     * they are actually enabled for a given configuration etc)
     *
     * @param scope the applicable scope set
     * @return a list of issues
     */
    @NonNull
    private List<Issue> getIssuesForScope(@NonNull EnumSet<Scope> scope) {
        List<Issue> list = sScopeIssues.get(scope);
        if (list == null) {
            List<Issue> issues = getIssues();
            if (scope.equals(Scope.ALL)) {
                list = issues;
            } else {
                int initialSize = 12;
                if (scope.contains(Scope.RESOURCE_FILE)) {
                    initialSize += 50;
                }
                if (scope.contains(Scope.JAVA_FILE)) {
                    initialSize += 12;
                }
                if (scope.contains(Scope.CLASS_FILE)) {
                    initialSize += 12;
                }
                list = new ArrayList<Issue>(initialSize);
                for (Issue issue : issues) {
                    // Determine if the scope matches
                    if (issue.isAdequate(scope)) {
                        list.add(issue);
                    }
                }
            }
            sScopeIssues.put(scope, list);
        }

        return list;
    }

    /**
* Creates a list of detectors applicable to the given cope, and with the
* given configuration.
*
//Synthetic comment -- @@ -111,10 +152,16 @@
@NonNull Configuration configuration,
@NonNull EnumSet<Scope> scope,
@Nullable Map<Scope, List<Detector>> scopeToDetectors) {

        List<Issue> issues = getIssuesForScope(scope);
        if (issues.isEmpty()) {
            return new ArrayList<Detector>(0);
        }

Set<Class<? extends Detector>> detectorClasses = new HashSet<Class<? extends Detector>>();
Map<Class<? extends Detector>, EnumSet<Scope>> detectorToScope =
new HashMap<Class<? extends Detector>, EnumSet<Scope>>();

for (Issue issue : issues) {
Class<? extends Detector> detectorClass = issue.getDetectorClass();
EnumSet<Scope> issueScope = issue.getScope();
//Synthetic comment -- @@ -124,10 +171,7 @@
continue;
}

                assert issue.isAdequate(scope); // Ensured by getIssuesForScope above

detectorClass = client.replaceDetector(detectorClass);

//Synthetic comment -- @@ -249,5 +293,6 @@
protected static void reset() {
sIdToIssue = null;
sCategories = null;
        sScopeIssues = Maps.newHashMap();
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index 70d741e..05765a4 100644

//Synthetic comment -- @@ -55,7 +55,7 @@
private static final List<Issue> sIssues;

static {
        final int initialCapacity = 114;
List<Issue> issues = new ArrayList<Issue>(initialCapacity);

issues.add(AccessibilityDetector.ISSUE);
//Synthetic comment -- @@ -138,6 +138,7 @@
issues.add(IconDetector.DUPLICATES_NAMES);
issues.add(IconDetector.DUPLICATES_CONFIGURATIONS);
issues.add(IconDetector.ICON_NODPI);
        issues.add(IconDetector.ICON_EXTENSION);
issues.add(TypographyDetector.DASHES);
issues.add(TypographyDetector.QUOTES);
issues.add(TypographyDetector.FRACTIONS);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/IconDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/IconDetector.java
//Synthetic comment -- index e149cc0..cf87acd 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import static com.android.SdkConstants.ATTR_ICON;
import static com.android.SdkConstants.DOT_9PNG;
import static com.android.SdkConstants.DOT_GIF;
import static com.android.SdkConstants.DOT_JPEG;
import static com.android.SdkConstants.DOT_JPG;
import static com.android.SdkConstants.DOT_PNG;
import static com.android.SdkConstants.DOT_XML;
//Synthetic comment -- @@ -60,6 +61,7 @@
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
//Synthetic comment -- @@ -251,6 +253,20 @@
IconDetector.class,
Scope.ALL_RESOURCES_SCOPE);

    /** Wrong filename according to the format */
    public static final Issue ICON_EXTENSION = Issue.create(
            "IconExtension", //$NON-NLS-1$
            "Checks that the icon file extension matches the actual image format in the file",

            "Ensures that icons have the correct file extension (e.g. a .png file is " +
            "really in the PNG format and not for example a GIF file named .png.)",
            Category.ICONS,
            3,
            Severity.WARNING,
            IconDetector.class,
            Scope.ALL_RESOURCES_SCOPE);


private String mApplicationIcon;

/** Constructs a new {@link IconDetector} check */
//Synthetic comment -- @@ -308,7 +324,7 @@
for (File f : files) {
String name = f.getName();
if (isDrawableFile(name)) {
                                        names.add(name);
}
}
folderToNames.put(folder, names);
//Synthetic comment -- @@ -334,8 +350,8 @@

private static boolean isDrawableFile(String name) {
// endsWith(name, DOT_PNG) is also true for endsWith(name, DOT_9PNG)
        return endsWith(name, DOT_PNG)|| endsWith(name, DOT_JPG) || endsWith(name, DOT_GIF)
                || endsWith(name, DOT_XML) || endsWith(name, DOT_JPEG);
}

// This method looks for duplicates in the assets. This uses two pieces of information
//Synthetic comment -- @@ -948,6 +964,7 @@
// pass - most common case, avoids checking other extensions
} else if (endsWith(name, DOT_PNG)
|| endsWith(name, DOT_JPG)
                        || endsWith(name, DOT_JPEG)
|| endsWith(name, DOT_GIF)) {
context.report(ICON_LOCATION,
Location.create(file),
//Synthetic comment -- @@ -970,6 +987,15 @@
}
}

        if (context.isEnabled(ICON_EXTENSION)) {
            for (File file : files) {
                String path = file.getPath();
                if (isDrawableFile(path) && !endsWith(path, DOT_XML)) {
                    checkExtension(context, file);
                }
            }
        }

// Check icon sizes
if (context.isEnabled(ICON_EXPECTED_SIZE)) {
checkExpectedSizes(context, folder, files);
//Synthetic comment -- @@ -981,7 +1007,8 @@
// I don't check file sizes twice!
String fileName = file.getName();

                if (endsWith(fileName, DOT_PNG) || endsWith(fileName, DOT_JPG)
                        || endsWith(fileName, DOT_JPEG)) {
// Only scan .png files (except 9-patch png's) and jpg files for
// dip sizes. Duplicate checks can also be performed on ninepatch files.
if (pixelSizes != null && !endsWith(fileName, DOT_9PNG)) {
//Synthetic comment -- @@ -996,6 +1023,52 @@
}
}

    private void checkExtension(Context context, File file) {
        try {
            ImageInputStream input = ImageIO.createImageInputStream(file);
            if (input != null) {
                try {
                    Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
                    while (readers.hasNext()) {
                        ImageReader reader = readers.next();
                        try {
                            reader.setInput(input);

                            // Check file extension
                            String formatName = reader.getFormatName();
                            if (formatName != null && !formatName.isEmpty()) {
                                String path = file.getPath();
                                int index = path.lastIndexOf('.');
                                String extension = path.substring(index+1).toLowerCase(Locale.US);

                                if (!formatName.equalsIgnoreCase(extension)) {
                                    if (endsWith(path, DOT_JPG)
                                            && formatName.equals("JPEG")) { //$NON-NLS-1$
                                        return;
                                    }
                                    String message = String.format(
                                            "Misleading file extension; named .%1$s but the " +
                                            "file format is %2$s", extension, formatName);
                                    Location location = Location.create(file);
                                    context.report(ICON_EXTENSION, location, message, null);
                                }
                                break;
                            }
                        } finally {
                            reader.dispose();
                        }
                    }
                } finally {
                    if (input != null) {
                        input.close();
                    }
                }
            }
        } catch (IOException e) {
            // Pass -- we can't handle all image types, warn about those we can
        }
    }

private void checkExpectedSizes(Context context, File folder, File[] files) {
String folderName = folder.getName();

//Synthetic comment -- @@ -1119,7 +1192,7 @@
String fileName = file.getName();
// Only scan .png files (except 9-patch png's) and jpg files
if (!((endsWith(fileName, DOT_PNG) && !endsWith(fileName, DOT_9PNG)) ||
                endsWith(fileName, DOT_JPG) || endsWith(fileName, DOT_JPEG))) {
return;
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/MainTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/MainTest.java
//Synthetic comment -- index 6bd8c2d..0ea384e 100644

//Synthetic comment -- @@ -18,7 +18,6 @@

import com.android.tools.lint.checks.AbstractCheckTest;
import com.android.tools.lint.checks.AccessibilityDetector;
import com.android.tools.lint.detector.api.Detector;

import java.io.ByteArrayOutputStream;
//Synthetic comment -- @@ -29,12 +28,6 @@

@SuppressWarnings("javadoc")
public class MainTest extends AbstractCheckTest {
public void testWrap() {
String s =
"Hardcoding text attributes directly in layout files is bad for several reasons:\n" +








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/AbstractCheckTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/AbstractCheckTest.java
//Synthetic comment -- index 8b06c4d..561e424 100644

//Synthetic comment -- @@ -55,6 +55,12 @@
/** Common utility methods for the various lint check tests */
@SuppressWarnings("javadoc")
public abstract class AbstractCheckTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        BuiltinIssueRegistry.reset();
    }

protected abstract Detector getDetector();

private Detector mDetector;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/IconDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/IconDetectorTest.java
//Synthetic comment -- index f45ae08..1a84982 100644

//Synthetic comment -- @@ -17,6 +17,12 @@
package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Project;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("javadoc")
public class IconDetectorTest extends AbstractCheckTest {
//Synthetic comment -- @@ -25,7 +31,33 @@
return new IconDetector();
}

    private Set<Issue> mEnabled = new HashSet<Issue>();
    private static Set<Issue> ALL = new HashSet<Issue>();
    static {
        ALL.add(IconDetector.DUPLICATES_CONFIGURATIONS);
        ALL.add(IconDetector.DUPLICATES_NAMES);
        ALL.add(IconDetector.GIF_USAGE);
        ALL.add(IconDetector.ICON_DENSITIES);
        ALL.add(IconDetector.ICON_DIP_SIZE);
        ALL.add(IconDetector.ICON_EXPECTED_SIZE);
        ALL.add(IconDetector.ICON_EXTENSION);
        ALL.add(IconDetector.ICON_LOCATION);
        ALL.add(IconDetector.ICON_MISSING_FOLDER);
        ALL.add(IconDetector.ICON_NODPI);
    }

    @Override
    protected TestConfiguration getConfiguration(Project project) {
        return new TestConfiguration() {
            @Override
            public boolean isEnabled(Issue issue) {
                return super.isEnabled(issue) && mEnabled.contains(issue);
            }
        };
    }

public void test() throws Exception {
        mEnabled = ALL;
assertEquals(
"res/drawable-mdpi/sample_icon.gif: Warning: Using the .gif format for bitmaps is discouraged [GifUsage]\n" +
"res/drawable/ic_launcher.png: Warning: The ic_launcher.png icon has identical contents in the following configuration folders: drawable-mdpi, drawable [IconDuplicatesConfig]\n" +
//Synthetic comment -- @@ -49,6 +81,7 @@
}

public void testApi1() throws Exception {
        mEnabled = ALL;
assertEquals(
"No warnings.",

//Synthetic comment -- @@ -59,6 +92,7 @@
}

public void test2() throws Exception {
        mEnabled = ALL;
assertEquals(
"res/drawable-hdpi/other.9.png: Warning: The following unrelated icon files have identical contents: appwidget_bg.9.png, other.9.png [IconDuplicates]\n" +
"    res/drawable-hdpi/appwidget_bg.9.png: <No location-specific message\n" +
//Synthetic comment -- @@ -78,6 +112,7 @@
}

public void testNoDpi() throws Exception {
        mEnabled = ALL;
assertEquals(
"res/drawable-mdpi/frame.png: Warning: The following images appear in both -nodpi and in a density folder: frame.png [IconNoDpi]\n" +
"res/drawable-xlarge-nodpi-v11/frame.png: Warning: The frame.png icon has identical contents in the following configuration folders: drawable-mdpi, drawable-nodpi, drawable-xlarge-nodpi-v11 [IconDuplicatesConfig]\n" +
//Synthetic comment -- @@ -94,6 +129,7 @@
}

public void testNoDpi2() throws Exception {
        mEnabled = ALL;
// Having additional icon names in the no-dpi folder should not cause any complaints
assertEquals(
"res/drawable-xhdpi/frame.png: Warning: The image frame.png varies significantly in its density-independent (dip) size across the various density versions: drawable-ldpi/frame.png: 629x387 dp (472x290 px), drawable-mdpi/frame.png: 472x290 dp (472x290 px), drawable-hdpi/frame.png: 315x193 dp (472x290 px), drawable-xhdpi/frame.png: 236x145 dp (472x290 px) [IconDipSize]\n" +
//Synthetic comment -- @@ -119,6 +155,7 @@
}

public void testNoDpiMix() throws Exception {
        mEnabled = ALL;
assertEquals(
"res/drawable-mdpi/frame.xml: Warning: The following images appear in both -nodpi and in a density folder: frame.png, frame.xml [IconNoDpi]\n" +
"    res/drawable-mdpi/frame.png: <No location-specific message\n" +
//Synthetic comment -- @@ -133,6 +170,7 @@


public void testMixedFormat() throws Exception {
        mEnabled = ALL;
// Test having a mixture of .xml and .png resources for the same name
// Make sure we don't get:
// drawable-hdpi: Warning: Missing the following drawables in drawable-hdpi: f.png (found in drawable-mdpi)
//Synthetic comment -- @@ -145,4 +183,26 @@
"res/drawable/states.xml=>res/drawable-hdpi/f.xml",
"res/drawable/states.xml=>res/drawable-xhdpi/f.xml"));
}

    public void testMisleadingFileName() throws Exception {
        mEnabled = Collections.singleton(IconDetector.ICON_EXTENSION);
        assertEquals(
            "res/drawable-mdpi/frame.gif: Warning: Misleading file extension; named .gif but the file format is png [IconExtension]\n" +
            "res/drawable-mdpi/frame.jpg: Warning: Misleading file extension; named .jpg but the file format is png [IconExtension]\n" +
            "res/drawable-mdpi/myjpg.png: Warning: Misleading file extension; named .png but the file format is JPEG [IconExtension]\n" +
            "res/drawable-mdpi/sample_icon.jpeg: Warning: Misleading file extension; named .jpeg but the file format is gif [IconExtension]\n" +
            "res/drawable-mdpi/sample_icon.jpg: Warning: Misleading file extension; named .jpg but the file format is gif [IconExtension]\n" +
            "res/drawable-mdpi/sample_icon.png: Warning: Misleading file extension; named .png but the file format is gif [IconExtension]\n" +
            "0 errors, 6 warnings\n",

            lintProject(
                "res/drawable-mdpi/sample_icon.jpg=>res/drawable-mdpi/myjpg.jpg", // VALID
                "res/drawable-mdpi/sample_icon.jpg=>res/drawable-mdpi/myjpg.jpeg", // VALID
                "res/drawable-mdpi/frame.png=>res/drawable-mdpi/frame.gif",
                "res/drawable-mdpi/frame.png=>res/drawable-mdpi/frame.jpg",
                "res/drawable-mdpi/sample_icon.jpg=>res/drawable-mdpi/myjpg.png",
                "res/drawable-mdpi/sample_icon.gif=>res/drawable-mdpi/sample_icon.jpg",
                "res/drawable-mdpi/sample_icon.gif=>res/drawable-mdpi/sample_icon.jpeg",
                "res/drawable-mdpi/sample_icon.gif=>res/drawable-mdpi/sample_icon.png"));
    }
}
\ No newline at end of file







