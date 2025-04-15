/*Add lint launcher icon shape check

Change-Id:I789e74b667afbaea4089a62060b7138dadd8da6b*/
//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index e521803..a211029 100644

//Synthetic comment -- @@ -55,7 +55,7 @@
private static final List<Issue> sIssues;

static {
        final int initialCapacity = 128;
List<Issue> issues = new ArrayList<Issue>(initialCapacity);

issues.add(AccessibilityDetector.ISSUE);
//Synthetic comment -- @@ -150,6 +150,7 @@
issues.add(IconDetector.ICON_EXTENSION);
issues.add(IconDetector.ICON_COLORS);
issues.add(IconDetector.ICON_XML_AND_PNG);
issues.add(TypographyDetector.DASHES);
issues.add(TypographyDetector.QUOTES);
issues.add(TypographyDetector.FRACTIONS);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/IconDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/IconDetector.java
//Synthetic comment -- index eb87a5d..f435810 100644

//Synthetic comment -- @@ -333,6 +333,26 @@
ICON_TYPE_SCOPE).setMoreInfo(
"http://developer.android.com/design/style/iconography.html"); //$NON-NLS-1$

/** Constructs a new {@link IconDetector} check */
public IconDetector() {
}
//Synthetic comment -- @@ -1113,6 +1133,25 @@
return file.getName().contains("-nodpi");
}

private void checkDrawableDir(Context context, File folder, File[] files,
Map<File, Dimension> pixelSizes, Map<File, Long> fileSizes) {
if (folder.getName().equals(DRAWABLE_FOLDER)
//Synthetic comment -- @@ -1179,6 +1218,18 @@
}
}

// Check icon sizes
if (context.isEnabled(ICON_EXPECTED_SIZE)) {
checkExpectedSizes(context, folder, files);
//Synthetic comment -- @@ -1205,6 +1256,36 @@
}
}
}
}

/**
//Synthetic comment -- @@ -1231,7 +1312,7 @@
// also check that they actually include a -v11 or -v14 folder with proper
// icons, since the below won't flag the older icons.
try {
            BufferedImage image = ImageIO.read(file);
if (image != null) {
if (isActionBarIcon) {
checkPixels:








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/IconDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/IconDetectorTest.java
//Synthetic comment -- index 4053c33..e2c20ba 100644

//Synthetic comment -- @@ -49,6 +49,7 @@
ALL.add(IconDetector.ICON_NODPI);
ALL.add(IconDetector.ICON_COLORS);
ALL.add(IconDetector.ICON_XML_AND_PNG);
}

@Override
//Synthetic comment -- @@ -444,5 +445,16 @@
));
}


}
\ No newline at end of file







