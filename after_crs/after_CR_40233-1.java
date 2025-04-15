/*Include additional metadata in the XML report

This CL adds additional metadata into the lint XML report, such as the
full issue explanation, the issue priority, the issue summary, the
additional information URL, and the error line.

This allows for example the Jenkins plugin to provide all this
information for a build report without having to bundle a (possibly
out of date) snapshot of lint's metadata the way it does right now.

Change-Id:Iaf674ffa6bbef318ce3f636404d72c74de4b1c50*/




//Synthetic comment -- diff --git a/lint/cli/src/com/android/tools/lint/Main.java b/lint/cli/src/com/android/tools/lint/Main.java
//Synthetic comment -- index fbb0b16..f0cbf8b 100644

//Synthetic comment -- @@ -683,8 +683,18 @@
"\"lint --ignore UnusedResources,UselessLeaf /my/project/path\"\n";
}

private void printVersion() {
        String revision = getRevision();
        if (revision != null) {
            System.out.println(String.format("lint: version %1$s", revision));
        } else {
            System.out.println("lint: unknown version");
        }
    }

    @SuppressWarnings("resource") // Eclipse doesn't know about Closeables.closeQuietly
    @Nullable
    String getRevision() {
File file = findResource("tools" + File.separator +     //$NON-NLS-1$
"source.properties");          //$NON-NLS-1$
if (file != null && file.exists()) {
//Synthetic comment -- @@ -696,8 +706,7 @@

String revision = properties.getProperty("Pkg.Revision"); //$NON-NLS-1$
if (revision != null && revision.length() > 0) {
                    return revision;
}
} catch (IOException e) {
// Couldn't find or read the version info: just print out unknown below
//Synthetic comment -- @@ -706,7 +715,7 @@
}
}

        return null;
}

private void displayValidIds(IssueRegistry registry, PrintStream out) {








//Synthetic comment -- diff --git a/lint/cli/src/com/android/tools/lint/XmlReporter.java b/lint/cli/src/com/android/tools/lint/XmlReporter.java
//Synthetic comment -- index f924def..04ac1d9 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.tools.lint;

import com.android.tools.lint.checks.BuiltinIssueRegistry;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Position;
import com.google.common.annotations.Beta;
//Synthetic comment -- @@ -52,18 +54,53 @@

@Override
public void write(int errorCount, int warningCount, List<Warning> issues) throws IOException {
        mWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");      //$NON-NLS-1$
        mWriter.write("<issues format=\"3\"");                              //$NON-NLS-1$
        String revision = mClient.getRevision();
        if (revision != null) {
            mWriter.write(String.format(" by=\"lint %1$s\"", revision));    //$NON-NLS-1$
        }
        mWriter.write(">\n");                                               //$NON-NLS-1$

if (issues.size() > 0) {
for (Warning warning : issues) {
mWriter.write('\n');
indent(mWriter, 1);
mWriter.write("<issue"); //$NON-NLS-1$
                Issue issue = warning.issue;
                writeAttribute(mWriter, 2, "id", issue.getId());                      //$NON-NLS-1$
                writeAttribute(mWriter, 2, "severity",
                        warning.severity.getDescription());
                writeAttribute(mWriter, 2, "message", warning.message);               //$NON-NLS-1$

                writeAttribute(mWriter, 2, "category",                                //$NON-NLS-1$
                        issue.getCategory().getFullName());
                writeAttribute(mWriter, 2, "priority",                                //$NON-NLS-1$
                        Integer.toString(issue.getPriority()));
                writeAttribute(mWriter, 2, "summary", issue.getDescription());        //$NON-NLS-1$
                writeAttribute(mWriter, 2, "explanation", issue.getExplanation());    //$NON-NLS-1$
                if (issue.getMoreInfo() != null) {
                    writeAttribute(mWriter, 2, "url", issue.getMoreInfo());           //$NON-NLS-1$
                }
                if (warning.errorLine != null && !warning.errorLine.isEmpty()) {
                    String line = warning.errorLine;
                    int index1 = line.indexOf('\n');
                    if (index1 != -1) {
                        int index2 = line.indexOf('\n', index1 + 1);
                        if (index2 != -1) {
                            String line1 = line.substring(0, index1);
                            String line2 = line.substring(index1 + 1, index2);
                            writeAttribute(mWriter, 2, "errorLine1", line1);          //$NON-NLS-1$
                            writeAttribute(mWriter, 2, "errorLine2", line2);       //$NON-NLS-1$
                        }
                    }
                }
                if (mClient.getRegistry() instanceof BuiltinIssueRegistry &&
                        ((BuiltinIssueRegistry) mClient.getRegistry()).hasAutoFix(
                                "adt", issue)) { //$NON-NLS-1$
                    writeAttribute(mWriter, 2, "quickfix", "adt");      //$NON-NLS-1$ //$NON-NLS-2$
                }

assert (warning.file != null) == (warning.location != null);

if (warning.file != null) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/XmlReporterTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/XmlReporterTest.java
new file mode 100644
//Synthetic comment -- index 0000000..4206161

//Synthetic comment -- @@ -0,0 +1,140 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.tools.lint;

import com.android.tools.lint.checks.AbstractCheckTest;
import com.android.tools.lint.checks.HardcodedValuesDetector;
import com.android.tools.lint.checks.ManifestOrderDetector;
import com.android.tools.lint.detector.api.DefaultPosition;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Project;
import com.android.tools.lint.detector.api.Severity;
import com.android.util.PositionXmlParser;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import org.w3c.dom.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("javadoc")
public class XmlReporterTest extends AbstractCheckTest {
    public void test() throws Exception {
        File file = new File(getTargetDir(), "report");
        try {
            Main client = new Main() {
                @Override
                String getRevision() {
                    return "unittest"; // Hardcode version to keep unit test output stable
                }
            };
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
            warning1.file = new File("/foo/bar/Foo/AndroidManifest.xml");
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
                "        priority=\"2\"\n" +
                "        summary=\"Checks that the minimum SDK and target SDK attributes are defined\"\n" +
                "        explanation=\"The manifest should contain a &lt;uses-sdk> element which defines the minimum minimum API Level required for the application to run, as well as the target version (the highest API level you have tested the version for.)\"\n" +
                "        url=\"http://developer.android.com/guide/topics/manifest/uses-sdk-element.html\"\n" +
                "        errorLine1=\"    &lt;uses-sdk android:minSdkVersion=&quot;8&quot; />\"\n" +
                "        errorLine2=\"    ^\">\n" +
                "        <location\n" +
                "            file=\"AndroidManifest.xml\"\n" +
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
                "            file=\"res/layout/main.xml\"\n" +
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
        return null;
    }
}







