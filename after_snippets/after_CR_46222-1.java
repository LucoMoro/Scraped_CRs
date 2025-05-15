
//<Beginning of snippet n. 0>


private static final List<Issue> sIssues;

static {
        final int initialCapacity = 124;
List<Issue> issues = new ArrayList<Issue>(initialCapacity);

issues.add(AccessibilityDetector.ISSUE);
issues.add(IconDetector.ICON_NODPI);
issues.add(IconDetector.ICON_EXTENSION);
issues.add(IconDetector.ICON_COLORS);
        issues.add(IconDetector.ICON_XML_AND_PNG);
issues.add(TypographyDetector.DASHES);
issues.add(TypographyDetector.QUOTES);
issues.add(TypographyDetector.FRACTIONS);

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


6,
Severity.WARNING,
DuplicateResourceDetector.class,
            Scope.ALL_RESOURCES_SCOPE).addAnalysisScope(Scope.RESOURCE_FILE_SCOPE);

private static final String PRODUCT = "product";   //$NON-NLS-1$
private Map<ResourceType, Set<String>> mTypeMap;

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


import com.android.tools.lint.detector.api.Speed;
import com.android.tools.lint.detector.api.XmlContext;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import lombok.ast.TypeReference;
import lombok.ast.TypeReferencePart;
import lombok.ast.VariableReference;
import lombok.ast.libs.org.parboiled.google.collect.Lists;

/**
* Checks for common icon problems, such as wrong icon sizes, placing icons in the
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
if (folders != null) {
boolean checkFolders = context.isEnabled(ICON_DENSITIES)
|| context.isEnabled(ICON_MISSING_FOLDER)
                        || context.isEnabled(ICON_NODPI)
                        || context.isEnabled(ICON_XML_AND_PNG);
boolean checkDipSizes = context.isEnabled(ICON_DIP_SIZE);
boolean checkDuplicates = context.isEnabled(DUPLICATES_NAMES)
|| context.isEnabled(DUPLICATES_CONFIGURATIONS);
fileSizes = new HashMap<File, Long>();
}
Map<File, Set<String>> folderToNames = new HashMap<File, Set<String>>();
                Map<File, Set<String>> nonDpiFolderNames = new HashMap<File, Set<String>>();
for (File folder : folders) {
String folderName = folder.getName();
if (folderName.startsWith(DRAWABLE_FOLDER)) {
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
}

if (checkFolders && folderToNames.size() > 0) {
                    checkDensities(context, res, folderToNames, nonDpiFolderNames);
}
}
}
}
}

    private void checkDensities(Context context, File res,
            Map<File, Set<String>> folderToNames,
            Map<File, Set<String>> nonDpiFolderNames) {
// TODO: Is there a way to look at the manifest and figure out whether
// all densities are expected to be needed?
// Note: ldpi is probably not needed; it has very little usage
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

//<End of snippet n. 2>










//<Beginning of snippet n. 3>


ALL.add(IconDetector.ICON_MISSING_FOLDER);
ALL.add(IconDetector.ICON_NODPI);
ALL.add(IconDetector.ICON_COLORS);
        ALL.add(IconDetector.ICON_XML_AND_PNG);
}

@Override
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
assertEquals(
"res/drawable-mdpi/frame.xml: Warning: The following images appear in both -nodpi and in a density folder: frame.png, frame.xml [IconNoDpi]\n" +
"    res/drawable-mdpi/frame.png: <No location-specific message\n" +
            "res/drawable-nodpi/frame.xml: Warning: The following images appear both as density independent .xml files and as bitmap files: res/drawable-mdpi/frame.png, res/drawable-nodpi/frame.xml [IconXmlAndPng]\n" +
            "    res/drawable-mdpi/frame.png: <No location-specific message\n" +
"res: Warning: Missing density variation folders in res: drawable-hdpi, drawable-xhdpi [IconMissingDensityFolder]\n" +
            "0 errors, 3 warnings\n",

lintProject(
"res/drawable-mdpi/frame.png",
// drawable-hdpi: Warning: Missing the following drawables in drawable-hdpi: f.png (found in drawable-mdpi)
// drawable-xhdpi: Warning: Missing the following drawables in drawable-xhdpi: f.png (found in drawable-mdpi)
assertEquals(
            "res/drawable-xhdpi/f.xml: Warning: The following images appear both as density independent .xml files and as bitmap files: res/drawable-hdpi/f.xml, res/drawable-mdpi/f.png [IconXmlAndPng]\n" +
            "    res/drawable-mdpi/f.png: <No location-specific message\n" +
            "    res/drawable-hdpi/f.xml: <No location-specific message\n" +
            "0 errors, 1 warnings\n",

lintProject(
"res/drawable-mdpi/frame.png=>res/drawable-mdpi/f.png",

//<End of snippet n. 3>








