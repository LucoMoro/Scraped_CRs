/*Lint tweaks. DO NOT MERGE

Fix missing dispose() call for manifest files, and add a method
to the TypographyDetector such that clients don't need a DOM node
to look up the suggested replacement.

Change-Id:I9faf8d2b7735399501db6d5a3ad72766d284ee2a*/




//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index dc62b5d..daf2a07 100644

//Synthetic comment -- @@ -848,16 +848,22 @@
if (parser != null) {
context.document = parser.parseXml(context);
if (context.document != null) {
                    try {
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
                    } finally {
                      if (context.document != null) { // else: freed by XmlVisitor above
                          parser.dispose(context, context.document);
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
            if (prev != null && prev.length() > 0) {
if (Character.isUpperCase(prev.charAt(0))) {
sb.append('$');
} else {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TypographyDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TypographyDetector.java
//Synthetic comment -- index 8f2ed47..ae5f24c 100644

//Synthetic comment -- @@ -431,8 +431,31 @@
*         offsets in the edit objects are relative to the text node.
*/
public static List<ReplaceEdit> getEdits(String issueId, String message, Node textNode) {
      return getEdits(issueId, message, textNode.getNodeValue());
    }

  /**
   * Returns a list of edits to be applied to fix the suggestion made by the
   * given warning. The specific issue id and message should be the message
   * provided by this detector in an earlier run.
   * <p>
   * This is intended to help tools implement automatic fixes of these
   * warnings. The reason only the message and issue id can be provided
   * instead of actual state passed in the data field to a reporter is that
   * fix operation can be run much later than the lint is processed (for
   * example, in a subsequent run of the IDE when only the warnings have been
   * persisted),
   *
   * @param issueId the issue id, which should be the id for one of the
   *            typography issues
   * @param message the actual error message, which should be a message
   *            provided by this detector
   * @param text the text of the XML node where the warning appeared
   * @return a list of edits, which is never null but could be empty. The
   *         offsets in the edit objects are relative to the text node.
   */
    public static List<ReplaceEdit> getEdits(String issueId, String message, String text) {
List<ReplaceEdit> edits = new ArrayList<ReplaceEdit>();
if (message.equals(ELLIPSIS_MESSAGE)) {
int offset = text.indexOf("...");                            //$NON-NLS-1$
if (offset != -1) {







