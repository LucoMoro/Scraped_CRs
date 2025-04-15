/*39477: Detect mixing .png and .xml drawable assets

Change-Id:Ieae4098cc5ea65ef4a38ffb788b1755a62312da8*/




//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index 9f0c064..38dd432 100644

//Synthetic comment -- @@ -55,7 +55,7 @@
private static final List<Issue> sIssues;

static {
        final int initialCapacity = 124;
List<Issue> issues = new ArrayList<Issue>(initialCapacity);

issues.add(AccessibilityDetector.ISSUE);
//Synthetic comment -- @@ -145,6 +145,7 @@
issues.add(IconDetector.ICON_NODPI);
issues.add(IconDetector.ICON_EXTENSION);
issues.add(IconDetector.ICON_COLORS);
        issues.add(IconDetector.ICON_XML_AND_PNG);
issues.add(TypographyDetector.DASHES);
issues.add(TypographyDetector.QUOTES);
issues.add(TypographyDetector.FRACTIONS);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/DuplicateResourceDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/DuplicateResourceDetector.java
//Synthetic comment -- index ffe006b..32d50b4 100644

//Synthetic comment -- @@ -72,7 +72,7 @@
6,
Severity.WARNING,
DuplicateResourceDetector.class,
            Scope.ALL_RESOURCES_SCOPE).addAnalysisScope(Scope.RESOURCE_FILE_SCOPE);

private static final String PRODUCT = "product";   //$NON-NLS-1$
private Map<ResourceType, Set<String>> mTypeMap;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/IconDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/IconDetector.java
//Synthetic comment -- index 3e7e2e8..9a72732 100644

//Synthetic comment -- @@ -59,6 +59,7 @@
import com.android.tools.lint.detector.api.Speed;
import com.android.tools.lint.detector.api.XmlContext;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

//Synthetic comment -- @@ -100,6 +101,7 @@
import lombok.ast.TypeReference;
import lombok.ast.TypeReferencePart;
import lombok.ast.VariableReference;
import lombok.ast.libs.org.parboiled.google.collect.Lists;

/**
* Checks for common icon problems, such as wrong icon sizes, placing icons in the
//Synthetic comment -- @@ -285,6 +287,20 @@
IconDetector.class,
Scope.ALL_RESOURCES_SCOPE);

    /** Icons appearing as both drawable xml files and bitmaps */
    public static final Issue ICON_XML_AND_PNG = Issue.create(
            "IconXmlAndPng", //$NON-NLS-1$
            "Finds icons that appear both as a drawable .xml file and as bitmaps",
            "If a drawable resource appears as an .xml file in the drawable/ folder, " +
            "it's usually not intentional for it to also appear as a bitmap using the " +
            "same name; generally you expect the drawable XML file to define states " +
            "and each state has a corresponding drawable bitmap.",
            Category.ICONS,
            7,
            Severity.WARNING,
            IconDetector.class,
            Scope.ALL_RESOURCES_SCOPE);

/** Wrong filename according to the format */
public static final Issue ICON_EXTENSION = Issue.create(
"IconExtension", //$NON-NLS-1$
//Synthetic comment -- @@ -355,7 +371,8 @@
if (folders != null) {
boolean checkFolders = context.isEnabled(ICON_DENSITIES)
|| context.isEnabled(ICON_MISSING_FOLDER)
                        || context.isEnabled(ICON_NODPI)
                        || context.isEnabled(ICON_XML_AND_PNG);
boolean checkDipSizes = context.isEnabled(ICON_DIP_SIZE);
boolean checkDuplicates = context.isEnabled(DUPLICATES_NAMES)
|| context.isEnabled(DUPLICATES_CONFIGURATIONS);
//Synthetic comment -- @@ -367,6 +384,7 @@
fileSizes = new HashMap<File, Long>();
}
Map<File, Set<String>> folderToNames = new HashMap<File, Set<String>>();
                Map<File, Set<String>> nonDpiFolderNames = new HashMap<File, Set<String>>();
for (File folder : folders) {
String folderName = folder.getName();
if (folderName.startsWith(DRAWABLE_FOLDER)) {
//Synthetic comment -- @@ -383,6 +401,15 @@
}
}
folderToNames.put(folder, names);
                            } else if (checkFolders) {
                                Set<String> names = new HashSet<String>(files.length);
                                for (File f : files) {
                                    String name = f.getName();
                                    if (isDrawableFile(name)) {
                                        names.add(name);
                                    }
                                }
                                nonDpiFolderNames.put(folder, names);
}
}
}
//Synthetic comment -- @@ -397,7 +424,7 @@
}

if (checkFolders && folderToNames.size() > 0) {
                    checkDensities(context, res, folderToNames, nonDpiFolderNames);
}
}
}
//Synthetic comment -- @@ -799,7 +826,9 @@
}
}

    private void checkDensities(Context context, File res,
            Map<File, Set<String>> folderToNames,
            Map<File, Set<String>> nonDpiFolderNames) {
// TODO: Is there a way to look at the manifest and figure out whether
// all densities are expected to be needed?
// Note: ldpi is probably not needed; it has very little usage
//Synthetic comment -- @@ -879,6 +908,81 @@
}
}

        if (context.isEnabled(ICON_XML_AND_PNG)) {
            Map<File, Set<String>> folderMap = Maps.newHashMap(folderToNames);
            folderMap.putAll(nonDpiFolderNames);
            Set<String> xmlNames = Sets.newHashSetWithExpectedSize(100);
            Set<String> bitmapNames = Sets.newHashSetWithExpectedSize(100);

            for (Map.Entry<File, Set<String>> entry : folderMap.entrySet()) {
                Set<String> names = entry.getValue();
                for (String name : names) {
                    if (endsWith(name, DOT_XML)) {
                        xmlNames.add(name);
                    } else if (isDrawableFile(name)) {
                        bitmapNames.add(name);
                    }
                }
            }
            if (!xmlNames.isEmpty() && !bitmapNames.isEmpty()) {
                // Make sure that none of the nodpi names appear in a non-nodpi folder
                Set<String> overlap = nameIntersection(xmlNames, bitmapNames);
                if (!overlap.isEmpty()) {
                    Multimap<String, File> map = ArrayListMultimap.create();
                    Set<String> bases = Sets.newHashSetWithExpectedSize(overlap.size());
                    for (String name : overlap) {
                        bases.add(LintUtils.getBaseName(name));
                    }

                    for (String base : bases) {
                        for (Map.Entry<File, Set<String>> entry : folderMap.entrySet()) {
                            File folder = entry.getKey();
                            for (String n : entry.getValue()) {
                                if (base.equals(LintUtils.getBaseName(n))) {
                                    map.put(base, new File(folder, n));
                                }
                            }
                        }
                    }
                    List<String> sorted = new ArrayList<String>(map.keySet());
                    Collections.sort(sorted);
                    for (String name : sorted) {
                        List<File> lists = Lists.newArrayList(map.get(name));
                        Collections.sort(lists);

                        // Chain locations together
                        Location location = null;
                        for (File file : lists) {
                            Location linkedLocation = location;
                            location = Location.create(file);
                            location.setSecondary(linkedLocation);
                        }

                        List<String> fileNames = Lists.newArrayList();
                        boolean seenXml = false;
                        boolean seenNonXml = false;
                        for (File f : lists) {
                            boolean isXml = endsWith(f.getPath(), DOT_XML);
                            if (isXml && !seenXml) {
                                fileNames.add(context.getProject().getDisplayPath(f));
                                seenXml = true;
                            } else if (!isXml && !seenNonXml) {
                                fileNames.add(context.getProject().getDisplayPath(f));
                                seenNonXml = true;
                            }
                        }

                        context.report(ICON_XML_AND_PNG, location,
                            String.format(
                                "The following images appear both as density independent .xml files and as bitmap files: %1$s",
                                LintUtils.formatList(fileNames,
                                        context.getDriver().isAbbreviating() ? 10 : -1)),
                            null);
                    }
                }
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
        ALL.add(IconDetector.ICON_XML_AND_PNG);
}

@Override
//Synthetic comment -- @@ -95,6 +96,19 @@
"res/drawable-hdpi/ic_launcher.png"));
}

    public void testMixed() throws Exception {
        mEnabled = Collections.singleton(IconDetector.ICON_XML_AND_PNG);
        assertEquals(
            "res/drawable/background.xml: Warning: The following images appear both as density independent .xml files and as bitmap files: res/drawable-mdpi/background.png, res/drawable/background.xml [IconXmlAndPng]\n" +
            "    res/drawable-mdpi/background.png: <No location-specific message\n" +
            "0 errors, 1 warnings\n",

            lintProject(
                    "apicheck/minsdk4.xml=>AndroidManifest.xml",
                    "apicheck/minsdk4.xml=>res/drawable/background.xml",
                    "res/drawable/ic_launcher.png=>res/drawable-mdpi/background.png"));
    }

public void testApi1() throws Exception {
mEnabled = ALL;
assertEquals(
//Synthetic comment -- @@ -174,9 +188,10 @@
assertEquals(
"res/drawable-mdpi/frame.xml: Warning: The following images appear in both -nodpi and in a density folder: frame.png, frame.xml [IconNoDpi]\n" +
"    res/drawable-mdpi/frame.png: <No location-specific message\n" +
            "res/drawable-nodpi/frame.xml: Warning: The following images appear both as density independent .xml files and as bitmap files: res/drawable-mdpi/frame.png, res/drawable-nodpi/frame.xml [IconXmlAndPng]\n" +
            "    res/drawable-mdpi/frame.png: <No location-specific message\n" +
"res: Warning: Missing density variation folders in res: drawable-hdpi, drawable-xhdpi [IconMissingDensityFolder]\n" +
            "0 errors, 3 warnings\n",

lintProject(
"res/drawable-mdpi/frame.png",
//Synthetic comment -- @@ -191,7 +206,10 @@
// drawable-hdpi: Warning: Missing the following drawables in drawable-hdpi: f.png (found in drawable-mdpi)
// drawable-xhdpi: Warning: Missing the following drawables in drawable-xhdpi: f.png (found in drawable-mdpi)
assertEquals(
            "res/drawable-xhdpi/f.xml: Warning: The following images appear both as density independent .xml files and as bitmap files: res/drawable-hdpi/f.xml, res/drawable-mdpi/f.png [IconXmlAndPng]\n" +
            "    res/drawable-mdpi/f.png: <No location-specific message\n" +
            "    res/drawable-hdpi/f.xml: <No location-specific message\n" +
            "0 errors, 1 warnings\n",

lintProject(
"res/drawable-mdpi/frame.png=>res/drawable-mdpi/f.png",







