//<Beginning of snippet n. 0>
private static final List<Issue> sIssues;

static {
    final int initialCapacity = 123;
    List<Issue> issues = new ArrayList<Issue>(initialCapacity);

    issues.add(AccessibilityDetector.ISSUE);
    issues.add(IconDetector.ICON_NODPI);
    issues.add(IconDetector.ICON_EXTENSION);
    issues.add(IconDetector.ICON_COLORS);
    issues.add(TypographyDetector.DASHES);
    issues.add(TypographyDetector.QUOTES);
    issues.add(TypographyDetector.FRACTIONS);
    sIssues = Collections.unmodifiableList(issues);
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
6,
Severity.WARNING,
DuplicateResourceDetector.class,
Scope.RESOURCE_FILE_SCOPE);

private static final String PRODUCT = "product"; //$NON-NLS-1$
private Map<ResourceType, Set<String>> mTypeMap;
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
import com.android.tools.lint.detector.api.Speed;
import com.android.tools.lint.detector.api.XmlContext;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import lombok.ast.TypeReference;
import lombok.ast.TypeReferencePart;
import lombok.ast.VariableReference;

public static final Issue ICON_EXTENSION = Issue.create(
        "IconExtension", //$NON-NLS-1$
        // Additional issue attributes can be set here
);

if (folders != null) {
    boolean checkFolders = context.isEnabled(ICON_DENSITIES)
            || context.isEnabled(ICON_MISSING_FOLDER)
            || context.isEnabled(ICON_NODPI);
    boolean checkDipSizes = context.isEnabled(ICON_DIP_SIZE);
    boolean checkDuplicates = context.isEnabled(DUPLICATES_NAMES)
            || context.isEnabled(DUPLICATES_CONFIGURATIONS);
    
    fileSizes = new HashMap<File, Long>();
}

Map<File, Set<String>> folderToNames = new HashMap<File, Set<String>>();
for (File folder : folders) {
    String folderName = folder.getName();
    if (folderName.startsWith(DRAWABLE_FOLDER)) {
        String[] fileList = folder.list();
        if (fileList != null) {
            Set<String> names = new HashSet<>(Arrays.asList(fileList));
            long pngCount = names.stream().filter(name -> name.endsWith(".png")).count();
            long xmlCount = names.stream().filter(name -> name.endsWith(".xml")).count();
            if (pngCount > 0 && xmlCount > 0) {
                context.report(ICON_EXTENSION, location, "Mixed drawable formats detected in " + folder.getAbsolutePath() + ": .png and .xml files present.");
                names.stream()
                    .filter(name -> name.endsWith(".png") || name.endsWith(".xml"))
                    .forEach(name -> context.report(ICON_EXTENSION, location, "File: " + name + " found in " + folder.getAbsolutePath()));
            }
            folderToNames.put(folder, names);
        }
    }
}

if (checkFolders && !folderToNames.isEmpty()) {
    checkDensities(context, res, folderToNames);
}
//<End of snippet n. 2>

//<Beginning of snippet n. 3>
if (context.isEnabled(ICON_DENSITIES)) {
    Set<String> allNames = new HashSet<String>();
}

ALL.add(IconDetector.ICON_MISSING_FOLDER);
ALL.add(IconDetector.ICON_NODPI);
ALL.add(IconDetector.ICON_COLORS);

@Override
public void testApi1() throws Exception {
    mEnabled = ALL;
    assertEquals(
        "res/drawable-mdpi/frame.xml: Warning: The following images appear in both -nodpi and in a density folder: frame.png, frame.xml [IconNoDpi]\n" +
        "    res/drawable-mdpi/frame.png: <No location-specific message\n" +
        "res: Warning: Missing density variation folders in res: drawable-hdpi, drawable-xhdpi [IconMissingDensityFolder]\n" +
        "0 errors, 2 warnings\n" +
        "",
        lintProject("res/drawable-mdpi/frame.png",
            assertEquals("No warnings.",
            lintProject("res/drawable-mdpi/frame.png=>res/drawable-mdpi/f.png")
    );
}
//<End of snippet n. 3>