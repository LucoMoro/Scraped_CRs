/*39477: Detect mixing .png and .xml drawable assets

Change-Id:Ieae4098cc5ea65ef4a38ffb788b1755a62312da8*/
//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index 9f0c064..38dd432 100644

//Synthetic comment -- @@ -55,7 +55,7 @@
private static final List<Issue> sIssues;

static {
        final int initialCapacity = 123;
List<Issue> issues = new ArrayList<Issue>(initialCapacity);

issues.add(AccessibilityDetector.ISSUE);
//Synthetic comment -- @@ -145,6 +145,7 @@
issues.add(IconDetector.ICON_NODPI);
issues.add(IconDetector.ICON_EXTENSION);
issues.add(IconDetector.ICON_COLORS);
issues.add(TypographyDetector.DASHES);
issues.add(TypographyDetector.QUOTES);
issues.add(TypographyDetector.FRACTIONS);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/IconDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/IconDetector.java
//Synthetic comment -- index 3e7e2e8..eb87a5d 100644

//Synthetic comment -- @@ -59,6 +59,8 @@
import com.android.tools.lint.detector.api.Speed;
import com.android.tools.lint.detector.api.XmlContext;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

//Synthetic comment -- @@ -285,6 +287,20 @@
IconDetector.class,
Scope.ALL_RESOURCES_SCOPE);

/** Wrong filename according to the format */
public static final Issue ICON_EXTENSION = Issue.create(
"IconExtension", //$NON-NLS-1$
//Synthetic comment -- @@ -355,7 +371,8 @@
if (folders != null) {
boolean checkFolders = context.isEnabled(ICON_DENSITIES)
|| context.isEnabled(ICON_MISSING_FOLDER)
                        || context.isEnabled(ICON_NODPI);
boolean checkDipSizes = context.isEnabled(ICON_DIP_SIZE);
boolean checkDuplicates = context.isEnabled(DUPLICATES_NAMES)
|| context.isEnabled(DUPLICATES_CONFIGURATIONS);
//Synthetic comment -- @@ -367,6 +384,7 @@
fileSizes = new HashMap<File, Long>();
}
Map<File, Set<String>> folderToNames = new HashMap<File, Set<String>>();
for (File folder : folders) {
String folderName = folder.getName();
if (folderName.startsWith(DRAWABLE_FOLDER)) {
//Synthetic comment -- @@ -383,6 +401,15 @@
}
}
folderToNames.put(folder, names);
}
}
}
//Synthetic comment -- @@ -397,7 +424,7 @@
}

if (checkFolders && folderToNames.size() > 0) {
                    checkDensities(context, res, folderToNames);
}
}
}
//Synthetic comment -- @@ -799,7 +826,9 @@
}
}

    private void checkDensities(Context context, File res, Map<File, Set<String>> folderToNames) {
// TODO: Is there a way to look at the manifest and figure out whether
// all densities are expected to be needed?
// Note: ldpi is probably not needed; it has very little usage
//Synthetic comment -- @@ -879,6 +908,81 @@
}
}

if (context.isEnabled(ICON_DENSITIES)) {
// Look for folders missing some of the specific assets
Set<String> allNames = new HashSet<String>();








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/IconDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/IconDetectorTest.java
//Synthetic comment -- index 0671a43..4053c33 100644

//Synthetic comment -- @@ -48,6 +48,7 @@
ALL.add(IconDetector.ICON_MISSING_FOLDER);
ALL.add(IconDetector.ICON_NODPI);
ALL.add(IconDetector.ICON_COLORS);
}

@Override
//Synthetic comment -- @@ -95,6 +96,19 @@
"res/drawable-hdpi/ic_launcher.png"));
}

public void testApi1() throws Exception {
mEnabled = ALL;
assertEquals(
//Synthetic comment -- @@ -174,9 +188,10 @@
assertEquals(
"res/drawable-mdpi/frame.xml: Warning: The following images appear in both -nodpi and in a density folder: frame.png, frame.xml [IconNoDpi]\n" +
"    res/drawable-mdpi/frame.png: <No location-specific message\n" +
"res: Warning: Missing density variation folders in res: drawable-hdpi, drawable-xhdpi [IconMissingDensityFolder]\n" +
            "0 errors, 2 warnings\n" +
            "",

lintProject(
"res/drawable-mdpi/frame.png",
//Synthetic comment -- @@ -191,7 +206,10 @@
// drawable-hdpi: Warning: Missing the following drawables in drawable-hdpi: f.png (found in drawable-mdpi)
// drawable-xhdpi: Warning: Missing the following drawables in drawable-xhdpi: f.png (found in drawable-mdpi)
assertEquals(
            "No warnings.",

lintProject(
"res/drawable-mdpi/frame.png=>res/drawable-mdpi/f.png",







