/*Lint tweaks

Fix missing dispose() call for manifest files, and add a method
to the TypographyDetector such that clients don't need a DOM node
to look up the suggested replacement.

Change-Id:If048a62a6b0b1c8af83a3e0b62a28876d4449764*/
//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index dc62b5d..daf2a07 100644

//Synthetic comment -- @@ -848,16 +848,22 @@
if (parser != null) {
context.document = parser.parseXml(context);
if (context.document != null) {
                    project.readManifest(context.document);

                    if ((!project.isLibrary() || (main != null && main.isMergingManifests()))
                            && mScope.contains(Scope.MANIFEST)) {
                        List<Detector> detectors = mScopeDetectors.get(Scope.MANIFEST);
                        if (detectors != null) {
                            XmlVisitor v = new XmlVisitor(parser, detectors);
                            fireEvent(EventType.SCANNING_FILE, context);
                            v.visitFile(context, manifestFile);
}
}
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/ClassContext.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/ClassContext.java
//Synthetic comment -- index 161f088..5150eff 100644

//Synthetic comment -- @@ -666,7 +666,7 @@
StringBuilder sb = new StringBuilder(fqcn.length());
String prev = null;
for (String part : Splitter.on('.').split(fqcn)) {
            if (prev != null) {
if (Character.isUpperCase(prev.charAt(0))) {
sb.append('$');
} else {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TypographyDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TypographyDetector.java
//Synthetic comment -- index 8f2ed47..ae5f24c 100644

//Synthetic comment -- @@ -431,8 +431,31 @@
*         offsets in the edit objects are relative to the text node.
*/
public static List<ReplaceEdit> getEdits(String issueId, String message, Node textNode) {
List<ReplaceEdit> edits = new ArrayList<ReplaceEdit>();
        String text = textNode.getNodeValue();
if (message.equals(ELLIPSIS_MESSAGE)) {
int offset = text.indexOf("...");                            //$NON-NLS-1$
if (offset != -1) {







