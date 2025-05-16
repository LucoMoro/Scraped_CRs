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

public static final Issue ICON_EXTENSION = Issue.create(
        "IconExtension", //$NON-NLS-1$
        "Wrong filename according to the format");

private void checkMixedDrawableTypes(XmlContext context, File[] folders) {
    boolean hasPng = false;
    boolean hasXml = false;

    for (File folder : folders) {
        String folderName = folder.getName();
        if (folderName.startsWith(DRAWABLE_FOLDER)) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(".png")) {
                        hasPng = true;
                    } else if (file.getName().endsWith(".xml")) {
                        hasXml = true;
                    }
                    if (hasPng && hasXml) {
                        context.report(
                            Issue.create("MixedDrawableTypes", 
                            "Both .png and .xml files found in the same drawable folder", 
                            Severity.WARNING), 
                            context.getLocation(file), 
                            "Mixed drawable asset types detected");
                        return;
                    }
                }
            }
        }
    }
}

private void checkDensities(Context context, File res, Map<File, Set<String>> folderToNames) {
    // TODO: Is there a way to look at the manifest and figure out whether
    // all densities are expected to be needed?
    // Note: ldpi is probably not needed; it has very little usage

    if (context.isEnabled(ICON_DENSITIES) && folderToNames.size() > 0) {
        checkMixedDrawableTypes(context, folderToNames.keySet().toArray(new File[0]));
        // Existing logic continues...
    }
}
//<End of snippet n. 2>

//<Beginning of snippet n. 3>
ALL.add(IconDetector.ICON_MISSING_FOLDER);
ALL.add(IconDetector.ICON_NODPI);
ALL.add(IconDetector.ICON_COLORS);

@Override
public void testApi1() throws Exception {
    mEnabled = ALL;
    assertEquals(
        // Existing test assertions
        "res/drawable-mdpi/frame.xml: Warning: The following images appear in both -nodpi and in a density folder: frame.png, frame.xml [IconNoDpi]\n" +
        "    res/drawable-mdpi/frame.png: <No location-specific message\n" +
        "res: Warning: Missing density variation folders in res: drawable-hdpi, drawable-xhdpi [IconMissingDensityFolder]\n" +
        "0 errors, 2 warnings\n" +
        "",
        lintProject(
            "res/drawable-mdpi/frame.png"
        )
    );
    // Existing logic continues...
}
//<End of snippet n. 3>