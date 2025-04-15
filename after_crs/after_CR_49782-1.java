/*Improve warning message when comment preceedes XML prologue. DO NOT MERGE

Change-Id:Ie1e7c6ed18b5b76537e2ed3e14b1bb9084cf0ab4*/




//Synthetic comment -- diff --git a/lint/cli/src/main/java/com/android/tools/lint/LintCliXmlParser.java b/lint/cli/src/main/java/com/android/tools/lint/LintCliXmlParser.java
//Synthetic comment -- index 3e1408a..bfbe7cb 100644

//Synthetic comment -- @@ -42,9 +42,10 @@
public class LintCliXmlParser extends PositionXmlParser implements IDomParser {
@Override
public Document parseXml(@NonNull XmlContext context) {
        String xml = null;
try {
// Do we need to provide an input stream for encoding?
            xml = context.getContents();
if (xml != null) {
return super.parse(xml);
}
//Synthetic comment -- @@ -57,12 +58,23 @@
e.getLocalizedMessage(),
null);
} catch (SAXException e) {
            Location location = Location.create(context.file);
            String message = e.getCause() != null ? e.getCause().getLocalizedMessage() :
                    e.getLocalizedMessage();
            if (message.startsWith("The processing instruction target matching "
                    + "\"[xX][mM][lL]\" is not allowed.")) {
                int prologue = xml.indexOf("<?xml ");
                int comment = xml.indexOf("<!--");
                if (prologue != -1 && comment != -1 && comment < prologue) {
                    message = "The XML prologue should appear before, not after, the first XML "
                            + "header/copyright comment. " + message;
                }
            }
context.report(
// Must provide an issue since API guarantees that the issue parameter
// is valid
                    IssueRegistry.PARSER_ERROR, location,
                    message,
null);
} catch (Throwable t) {
context.log(t, null);







