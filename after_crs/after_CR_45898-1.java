/*38838: lint -fullpaths doesn't always work

Change-Id:I09500947973897b412b61da17284f04acb8ece20*/




//Synthetic comment -- diff --git a/lint/cli/src/com/android/tools/lint/Main.java b/lint/cli/src/com/android/tools/lint/Main.java
//Synthetic comment -- index ad086c7..d2f9439 100644

//Synthetic comment -- @@ -23,6 +23,7 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.annotations.VisibleForTesting;
import com.android.tools.lint.checks.BuiltinIssueRegistry;
import com.android.tools.lint.client.api.Configuration;
import com.android.tools.lint.client.api.DefaultConfiguration;
//Synthetic comment -- @@ -41,6 +42,7 @@
import com.android.tools.lint.detector.api.Project;
import com.android.tools.lint.detector.api.Severity;
import com.android.utils.SdkUtils;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Closeables;
//Synthetic comment -- @@ -1317,6 +1319,62 @@
}
}

    /**
     * Given a file, it produces a cleaned up path from the file.
     * This will clean up the path such that
     * <ul>
     *   <li>  {@code foo/./bar} becomes {@code foo/bar}
     *   <li>  {@code foo/bar/../baz} becomes {@code foo/baz}
     * </ul>
     *
     * Unlike {@link File#getCanonicalPath()} however, it will <b>not</b> attempt
     * to make the file canonical, such as expanding symlinks and network mounts.
     *
     * @param file the file to compute a clean path for
     * @return the cleaned up path
     */
    @VisibleForTesting
    @NonNull
    static String getCleanPath(@NonNull File file) {
        String path = file.getPath();
        StringBuilder sb = new StringBuilder(path.length());

        if (path.startsWith(File.separator)) {
            sb.append(File.separator);
        }
        elementLoop:
        for (String element : Splitter.on(File.separatorChar).omitEmptyStrings().split(path)) {
            if (element.equals(".")) {          //$NON-NLS-1$
                continue;
            } else if (element.equals("..")) {  //$NON-NLS-1$
                if (sb.length() > 0) {
                    for (int i = sb.length() - 1; i >= 0; i--) {
                        char c = sb.charAt(i);
                        if (c == File.separatorChar) {
                            sb.setLength(i == 0 ? 1 : i);
                            continue elementLoop;
                        }
                    }
                    sb.setLength(0);
                    continue;
                }
            }

            if (sb.length() > 1) {
                sb.append(File.separatorChar);
            } else if (sb.length() > 0 && sb.charAt(0) != File.separatorChar) {
                sb.append(File.separatorChar);
            }
            sb.append(element);
        }
        if (path.endsWith(File.separator) && sb.length() > 0
                && sb.charAt(sb.length() - 1) != File.separatorChar) {
            sb.append(File.separator);
        }

        return sb.toString();
    }

String getDisplayPath(Project project, File file) {
String path = file.getPath();
if (!mFullPath && path.startsWith(project.getReferenceDir().getPath())) {
//Synthetic comment -- @@ -1328,6 +1386,8 @@
if (path.length() == 0) {
path = file.getName();
}
        } else if (mFullPath) {
            path = getCleanPath(file.getAbsoluteFile());
}

return path;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/MainTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/MainTest.java
//Synthetic comment -- index 4e7a029..d015a3d 100644

//Synthetic comment -- @@ -260,4 +260,39 @@
super("Unit test");
}
}

    public void test_getCleanPath() throws Exception {
        assertEquals("foo", Main.getCleanPath(new File("foo")));
        String sep = File.separator;
        assertEquals("foo" + sep + "bar",
                Main.getCleanPath(new File("foo" + sep + "bar")));
        assertEquals(sep,
                Main.getCleanPath(new File(sep)));
        assertEquals("foo" + sep + "bar",
                Main.getCleanPath(new File("foo" + sep + "." + sep + "bar")));
        assertEquals("bar",
                Main.getCleanPath(new File("foo" + sep + ".." + sep + "bar")));
        assertEquals("",
                Main.getCleanPath(new File("foo" + sep + "..")));
        assertEquals("foo",
                Main.getCleanPath(new File("foo" + sep + "bar" + sep + "..")));
        assertEquals("foo" + sep + ".foo" + sep + "bar",
                Main.getCleanPath(new File("foo" + sep + ".foo" + sep + "bar")));
        assertEquals("foo" + sep + "bar",
                Main.getCleanPath(new File("foo" + sep + "bar" + sep + ".")));
        assertEquals("foo" + sep + "...",
                Main.getCleanPath(new File("foo" + sep + "...")));
        assertEquals(".." + sep + "foo",
                Main.getCleanPath(new File(".." + sep + "foo")));
        assertEquals(sep + "foo",
                Main.getCleanPath(new File(sep + "foo")));
        assertEquals(sep,
                Main.getCleanPath(new File(sep + "foo" + sep + "..")));
        assertEquals(sep + "foo",
                Main.getCleanPath(new File(sep + "foo" + sep + "bar " + sep + "..")));
        assertEquals(sep + "c:",
                Main.getCleanPath(new File(sep + "c:")));
        assertEquals(sep + "c:" + sep + "foo",
                Main.getCleanPath(new File(sep + "c:" + sep + "foo")));
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/XmlReporterTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/XmlReporterTest.java
//Synthetic comment -- index 95147b4..166f721 100644

//Synthetic comment -- @@ -133,6 +133,105 @@
}
}

    public void testFullPaths() throws Exception {
        File file = new File(getTargetDir(), "report");
        try {
            Main client = new Main() {
                @Override
                String getRevision() {
                    return "unittest"; // Hardcode version to keep unit test output stable
                }
            };
            client.mFullPath = true;

            file.getParentFile().mkdirs();
            XmlReporter reporter = new XmlReporter(client, file);
            Project project = Project.create(client, new File("/foo/bar/Foo"),
                    new File("/foo/bar/Foo"));

            Warning warning1 = new Warning(ManifestOrderDetector.USES_SDK,
                    "<uses-sdk> tag should specify a target API level (the highest verified " +
                    "version; when running on later versions, compatibility behaviors may " +
                    "be enabled) with android:targetSdkVersion=\"?\"",
                    Severity.WARNING, project, null);
            warning1.line = 6;
            warning1.file = new File("/foo/bar/../Foo/AndroidManifest.xml");
            warning1.errorLine = "    <uses-sdk android:minSdkVersion=\"8\" />\n    ^\n";
            warning1.path = "AndroidManifest.xml";
            warning1.location = Location.create(warning1.file,
                    new DefaultPosition(6, 4, 198), new DefaultPosition(6, 42, 236));

            Warning warning2 = new Warning(HardcodedValuesDetector.ISSUE,
                    "[I18N] Hardcoded string \"Fooo\", should use @string resource",
                    Severity.WARNING, project, null);
            warning2.line = 11;
            warning2.file = new File("/foo/bar/Foo/res/layout/main.xml");
            warning2.errorLine = " (java.lang.String)         android:text=\"Fooo\" />\n" +
                          "        ~~~~~~~~~~~~~~~~~~~\n";
            warning2.path = "res/layout/main.xml";
            warning2.location = Location.create(warning2.file,
                    new DefaultPosition(11, 8, 377), new DefaultPosition(11, 27, 396));

            List<Warning> warnings = new ArrayList<Warning>();
            warnings.add(warning1);
            warnings.add(warning2);

            reporter.write(0, 2, warnings);

            String report = Files.toString(file, Charsets.UTF_8);
            assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<issues format=\"3\" by=\"lint unittest\">\n" +
                "\n" +
                "    <issue\n" +
                "        id=\"UsesMinSdkAttributes\"\n" +
                "        severity=\"Warning\"\n" +
                "        message=\"&lt;uses-sdk> tag should specify a target API level (the highest verified version; when running on later versions, compatibility behaviors may be enabled) with android:targetSdkVersion=&quot;?&quot;\"\n" +
                "        category=\"Correctness\"\n" +
                "        priority=\"9\"\n" +
                "        summary=\"Checks that the minimum SDK and target SDK attributes are defined\"\n" +
                "        explanation=\"The manifest should contain a `&lt;uses-sdk>` element which defines the minimum minimum API Level required for the application to run, as well as the target version (the highest API level you have tested the version for.)\"\n" +
                "        url=\"http://developer.android.com/guide/topics/manifest/uses-sdk-element.html\"\n" +
                "        errorLine1=\"    &lt;uses-sdk android:minSdkVersion=&quot;8&quot; />\"\n" +
                "        errorLine2=\"    ^\">\n" +
                "        <location\n" +
                "            file=\"/foo/Foo/AndroidManifest.xml\"\n" +
                "            line=\"7\"\n" +
                "            column=\"5\"/>\n" +
                "    </issue>\n" +
                "\n" +
                "    <issue\n" +
                "        id=\"HardcodedText\"\n" +
                "        severity=\"Warning\"\n" +
                "        message=\"[I18N] Hardcoded string &quot;Fooo&quot;, should use @string resource\"\n" +
                "        category=\"Internationalization\"\n" +
                "        priority=\"5\"\n" +
                "        summary=\"Looks for hardcoded text attributes which should be converted to resource lookup\"\n" +
                "        explanation=\"Hardcoding text attributes directly in layout files is bad for several reasons:\n" +
                "\n" +
                "* When creating configuration variations (for example for landscape or portrait)you have to repeat the actual text (and keep it up to date when making changes)\n" +
                "\n" +
                "* The application cannot be translated to other languages by just adding new translations for existing string resources.\"\n" +
                "        errorLine1=\" (java.lang.String)         android:text=&quot;Fooo&quot; />\"\n" +
                "        errorLine2=\"        ~~~~~~~~~~~~~~~~~~~\">\n" +
                "        <location\n" +
                "            file=\"/foo/bar/Foo/res/layout/main.xml\"\n" +
                "            line=\"12\"\n" +
                "            column=\"9\"/>\n" +
                "    </issue>\n" +
                "\n" +
                "</issues>\n",
                report);

            // Make sure the XML is valid
            Document document = new PositionXmlParser().parse(report);
            assertNotNull(document);
            assertEquals(2, document.getElementsByTagName("issue").getLength());
        } finally {
            file.delete();
        }
    }

@Override
protected Detector getDetector() {
fail("Not used in this test");







