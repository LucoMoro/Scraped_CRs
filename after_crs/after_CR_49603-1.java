/*42565: Lint reports Analytics resource as unused

Change-Id:I9e171315b64969ce1f5c796138bdd72f1f89d86e*/




//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/UnusedResourceDetectorTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/UnusedResourceDetectorTest.java
//Synthetic comment -- index 9113e79..d668a81 100644

//Synthetic comment -- @@ -263,4 +263,15 @@
"src/test/pkg/Foo.java.txt=>src/test/pkg/Foo.java",
"AndroidManifest.xml"));
}

    public void testAnalytics() throws Exception {
        // See http://code.google.com/p/android/issues/detail?id=42565
        mEnableIds = false;
        assertEquals(
                "No warnings.",

                lintProject(
                        "res/values/analytics.xml"
        ));
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/UnusedResourceDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/UnusedResourceDetector.java
//Synthetic comment -- index 2a2d77c..f960118 100644

//Synthetic comment -- @@ -396,12 +396,17 @@
if (mUnused.containsKey(resource)) {
if (context.getDriver().isSuppressed(getIssue(resource), item)) {
mUnused.remove(resource);
                                continue;
}
if (!context.getProject().getReportIssues()) {
mUnused.remove(resource);
                                continue;
}
                            if (isAnalyticsFile(context)) {
                                mUnused.remove(resource);
                                continue;
                            }

recordLocation(resource, context.getLocation(nameAttribute));
}
}
//Synthetic comment -- @@ -418,6 +423,18 @@
}
}

    private static final String ANALYTICS_FILE = "analytics.xml"; //$NON-NLS-1$

    /**
     * Returns true if this XML file corresponds to an Analytics configuration file;
     * these contain some attributes read by the library which won't be flagged as
     * used by the application
     */
    private static boolean isAnalyticsFile(XmlContext context) {
        File file = context.file;
        return file.getPath().endsWith(ANALYTICS_FILE) && file.getName().equals(ANALYTICS_FILE);
    }

private void checkChildRefs(Element item) {
// Look for ?attr/ and @dimen/foo etc references in the item children
NodeList childNodes = item.getChildNodes();







