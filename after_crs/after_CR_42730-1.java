/*Work around missing DOM method implementation in Eclipe 3.6

Change-Id:I67b37a0511dfba210dbcef393ee7610cef8b56a2*/




//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/TypoDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/TypoDetector.java
//Synthetic comment -- index ad3b7a3..0eb9866 100644

//Synthetic comment -- @@ -169,7 +169,7 @@
private void visit(XmlContext context, Node node) {
if (node.getNodeType() == Node.TEXT_NODE) {
// TODO: Figure out how to deal with entities
            check(context, node, node.getNodeValue());
} else {
NodeList children = node.getChildNodes();
for (int i = 0, n = children.getLength(); i < n; i++) {







