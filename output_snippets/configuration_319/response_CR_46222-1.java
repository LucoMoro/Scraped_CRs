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
private static final String PRODUCT = "product";   //$NON-NLS-1$
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

/**
* Checks for common icon problems, such as wrong icon sizes, placing icons in the
* wrong folder, etc.
*/
public static final Issue ICON_EXTENSION = Issue.create(
    "IconExtension", //$NON-NLS-1$
    "Wrong filename according to the format",
    Severity.WARNING,
    IconDetector.class,
    Scope.ALL_RESOURCES_SCOPE);

private void checkDrawableFolders(File[] folders, Context context) {
    if (folders != null) {
        boolean checkFolders = context.isEnabled(ICON_DENSITIES)
                || context.isEnabled(ICON_MISSING_FOLDER)
                || context.isEnabled(ICON_NODPI);
        boolean checkDipSizes = context.isEnabled(ICON_DIP_SIZE);
        boolean checkDuplicates = context.isEnabled(DUPLICATES_NAMES)
                || context.isEnabled(DUPLICATES_CONFIGURATIONS);
        Map<File, Long> fileSizes = new HashMap<File, Long>();
        Map<File, Set<String>> folderToNames = new HashMap<File, Set<String>>();
        Set<String> mixedAssets = new HashSet<>();

        for (File folder : folders) {
            String folderName = folder.getName();
            if (folderName.startsWith(DRAWABLE_FOLDER)) {
                Set<String> names = collectAssetNames(folder);
                folderToNames.put(folder, names);
                boolean hasPng = names.stream().anyMatch(name -> name.endsWith(".png"));
                boolean hasXml = names.stream().anyMatch(name -> name.endsWith(".xml"));
                if (hasPng && hasXml) {
                    mixedAssets.add(folder.getPath());
                }
            }
        }

        if (!mixedAssets.isEmpty()) {
            String message = "Warning: Mixed drawable assets detected in folders: " + String.join(", ", mixedAssets) +
                             ". Please separate assets into distinct folders.";
            context.report(ICON_EXTENSION, context.getLocation(file), message);
        }

        if (checkFolders && folderToNames.size() > 0) {
            checkDensities(context, folders, folderToNames);
        }
    }
}

private Set<String> collectAssetNames(File folder) {
    // Logic to retrieve asset names from the given folder
    return Arrays.stream(folder.listFiles())
                 .map(File::getName)
                 .collect(Collectors.toSet());
}

private void checkDensities(Context context, File[] res, Map<File, Set<String>> folderToNames) {
    // Implementation remains the same as before
}

//<End of snippet n. 2>

//<Beginning of snippet n. 3>
ALL.add(IconDetector.ICON_MISSING_FOLDER);
ALL.add(IconDetector.ICON_NODPI);
ALL.add(IconDetector.ICON_COLORS);
}

@Override
public void testApi1() throws Exception {
    mEnabled = ALL;
    assertEquals(
        "res/drawable-mdpi/frame.xml: Warning: The following images appear in both -nodpi and in a density folder: frame.png, frame.xml [IconNoDpi]\n" +
        "    res/drawable-mdpi/frame.png: <No location-specific message\n" +
        "res: Warning: Missing density variation folders in res: drawable-hdpi, drawable-xhdpi [IconMissingDensityFolder]\n" +
        "0 errors, 2 warnings\n" +
        "",
        lintProject(
            "res/drawable-mdpi/frame.png",
            // drawable-hdpi: Warning: Missing the following drawables in drawable-hdpi: f.png (found in drawable-mdpi)
            // drawable-xhdpi: Warning: Missing the following drawables in drawable-xhdpi: f.png (found in drawable-mdpi)
            "res/drawable-mdpi/frame.png=>res/drawable-mdpi/f.png"
        )
    );
    assertEquals("No warnings.", lintProject("res/drawable-mdpi/frame.png"));
}

//<End of snippet n. 3>