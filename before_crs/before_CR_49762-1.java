/*Improve warning message when comment preceedes XML prologue

Change-Id:I44e274c0f32e37a8a3cb1b4d64c0bb5af1df9659*/
//Synthetic comment -- diff --git a/lint/cli/src/main/java/com/android/tools/lint/LintCliXmlParser.java b/lint/cli/src/main/java/com/android/tools/lint/LintCliXmlParser.java
//Synthetic comment -- index 3e1408a..bfbe7cb 100644

//Synthetic comment -- @@ -42,9 +42,10 @@
public class LintCliXmlParser extends PositionXmlParser implements IDomParser {
@Override
public Document parseXml(@NonNull XmlContext context) {
try {
// Do we need to provide an input stream for encoding?
            String xml = context.getContents();
if (xml != null) {
return super.parse(xml);
}
//Synthetic comment -- @@ -57,12 +58,23 @@
e.getLocalizedMessage(),
null);
} catch (SAXException e) {
context.report(
// Must provide an issue since API guarantees that the issue parameter
// is valid
                    IssueRegistry.PARSER_ERROR, Location.create(context.file),
                    e.getCause() != null ? e.getCause().getLocalizedMessage() :
                        e.getLocalizedMessage(),
null);
} catch (Throwable t) {
context.log(t, null);







