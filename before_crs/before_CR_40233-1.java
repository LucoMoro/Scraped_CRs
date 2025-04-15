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

    @SuppressWarnings("resource") // Eclipse doesn't know about Closeables.closeQuietly
private void printVersion() {
File file = findResource("tools" + File.separator +     //$NON-NLS-1$
"source.properties");          //$NON-NLS-1$
if (file != null && file.exists()) {
//Synthetic comment -- @@ -696,8 +706,7 @@

String revision = properties.getProperty("Pkg.Revision"); //$NON-NLS-1$
if (revision != null && revision.length() > 0) {
                    System.out.println(String.format("lint: version %1$s", revision));
                    return;
}
} catch (IOException e) {
// Couldn't find or read the version info: just print out unknown below
//Synthetic comment -- @@ -706,7 +715,7 @@
}
}

        System.out.println("lint: unknown version");
}

private void displayValidIds(IssueRegistry registry, PrintStream out) {








//Synthetic comment -- diff --git a/lint/cli/src/com/android/tools/lint/XmlReporter.java b/lint/cli/src/com/android/tools/lint/XmlReporter.java
//Synthetic comment -- index f924def..04ac1d9 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.tools.lint;

import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Position;
import com.google.common.annotations.Beta;
//Synthetic comment -- @@ -52,18 +54,53 @@

@Override
public void write(int errorCount, int warningCount, List<Warning> issues) throws IOException {
        mWriter.write(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +     //$NON-NLS-1$
                "<issues>\n");                                       //$NON-NLS-1$

if (issues.size() > 0) {
for (Warning warning : issues) {
mWriter.write('\n');
indent(mWriter, 1);
mWriter.write("<issue"); //$NON-NLS-1$
                writeAttribute(mWriter, 2, "id", warning.issue.getId());   //$NON-NLS-1$
                writeAttribute(mWriter, 2, "severity", warning.severity.getDescription()); //$NON-NLS-1$
                writeAttribute(mWriter, 2, "message", warning.message);  //$NON-NLS-1$
assert (warning.file != null) == (warning.location != null);

if (warning.file != null) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/XmlReporterTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/XmlReporterTest.java
new file mode 100644
//Synthetic comment -- index 0000000..4206161

//Synthetic comment -- @@ -0,0 +1,140 @@







