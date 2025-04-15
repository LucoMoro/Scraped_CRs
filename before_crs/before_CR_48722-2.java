/*Remove ExportedActivity check. DO NOT MERGE

Change-Id:I94407b61f09d0b33d3a8d16246c1435142c33aba*/
//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/SecurityDetectorTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/SecurityDetectorTest.java
//Synthetic comment -- index 1c293c3..198058d 100644

//Synthetic comment -- @@ -143,58 +143,6 @@
"src/test/pkg/WorldWriteableFile.java.txt=>src/test/pkg/WorldWriteableFile.java"));
}

    public void testActivity0() throws Exception {
        // Activities that do not have intent-filters do not need warnings
        assertEquals(
            "No warnings.",
            lintProject(
                "exportactivity0.xml=>AndroidManifest.xml",
                "res/values/strings.xml"));
    }

    public void testActivity1() throws Exception {
        assertEquals(
            "AndroidManifest.xml:12: Warning: Exported activity does not require permission [ExportedActivity]\n" +
            "        <activity\n" +
            "        ^\n" +
            "0 errors, 1 warnings\n" +
            "",
            lintProject(
                "exportactivity1.xml=>AndroidManifest.xml",
                "res/values/strings.xml"));
    }

    public void testActivity2() throws Exception {
        // Defines a permission on the <activity> element
        assertEquals(
            "No warnings.",
            lintProject(
                "exportactivity2.xml=>AndroidManifest.xml",
                "res/values/strings.xml"));
    }

    public void testActivity3() throws Exception {
        // Defines a permission on the parent <application> element
        assertEquals(
            "No warnings.",
            lintProject(
                "exportactivity3.xml=>AndroidManifest.xml",
                "res/values/strings.xml"));
    }

    public void testActivity4() throws Exception {
        // Not defining exported, but have intent-filters
        assertEquals(
            "AndroidManifest.xml:12: Warning: Exported activity does not require permission [ExportedActivity]\n" +
            "        <activity\n" +
            "        ^\n" +
            "0 errors, 1 warnings\n" +
            "",
            lintProject(
                "exportactivity4.xml=>AndroidManifest.xml",
                "res/values/strings.xml"));
    }

public void testReceiver0() throws Exception {
// Activities that do not have intent-filters do not need warnings
assertEquals(








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index 8d0f9ca..fa4e40e 100644

//Synthetic comment -- @@ -136,7 +136,6 @@
issues.add(ManifestOrderDetector.ILLEGAL_REFERENCE);
issues.add(SecurityDetector.EXPORTED_PROVIDER);
issues.add(SecurityDetector.EXPORTED_SERVICE);
        issues.add(SecurityDetector.EXPORTED_ACTIVITY);
issues.add(SecurityDetector.EXPORTED_RECEIVER);
issues.add(SecurityDetector.OPEN_PROVIDER);
issues.add(SecurityDetector.WORLD_READABLE);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/SecurityDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/SecurityDetector.java
//Synthetic comment -- index bc8d47b..b6cc957 100644

//Synthetic comment -- @@ -102,20 +102,6 @@
SecurityDetector.class,
Scope.MANIFEST_SCOPE);

    /** Exported activities */
    public static final Issue EXPORTED_ACTIVITY = Issue.create(
            "ExportedActivity", //$NON-NLS-1$
            "Checks for exported activities that do not require permissions",
            "Exported activities (activities which either set `exported=true` or contain " +
            "an intent-filter and do not specify `exported=false`) should define a " +
            "permission that an entity must have in order to launch the activity " +
            "or bind to it. Without this, any application can use this activity.",
            Category.SECURITY,
            2,
            Severity.WARNING,
            SecurityDetector.class,
            Scope.MANIFEST_SCOPE);

/** Exported receivers */
public static final Issue EXPORTED_RECEIVER = Issue.create(
"ExportedReceiver", //$NON-NLS-1$
//Synthetic comment -- @@ -211,8 +197,6 @@
checkGrantPermission(context, element);
} else if (tag.equals(TAG_PROVIDER)) {
checkProvider(context, element);
        } else if (tag.equals(TAG_ACTIVITY)) {
            checkActivity(context, element);
} else if (tag.equals(TAG_RECEIVER)) {
checkReceiver(context, element);
}
//Synthetic comment -- @@ -267,16 +251,6 @@
return false;
}

    private static void checkActivity(XmlContext context, Element element) {
        // Do not flag launch activities. Even if not explicitly exported, it's
        // safe to assume that those activities should be exported.
        if (getExported(element) && isUnprotectedByPermission(element) && !isLauncher(element)) {
            // No declared permission for this exported activity: complain
            context.report(EXPORTED_ACTIVITY, element, context.getLocation(element),
                           "Exported activity does not require permission", null);
        }
    }

private static boolean isStandardReceiver(Element element) {
// Checks whether a broadcast receiver receives a standard Android action
for (Element child : LintUtils.getChildren(element)) {







