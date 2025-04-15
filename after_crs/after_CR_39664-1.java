/*Tweaks to the lint CLI offsets

Change-Id:I20592372d41235b36227a0d36014d77c23c7fc58*/




//Synthetic comment -- diff --git a/common/src/com/android/util/PositionXmlParser.java b/common/src/com/android/util/PositionXmlParser.java
//Synthetic comment -- index 08aeb0a..fe22f58 100644

//Synthetic comment -- @@ -356,7 +356,8 @@

Position attributePosition = createPosition(line, column, index);
// Also set end range for retrieval in getLocation
                    attributePosition.setEnd(createPosition(line, column + matcher.end() - index,
                            matcher.end()));
return attributePosition;
} else {
// No regexp match either: just fall back to element position








//Synthetic comment -- diff --git a/lint/cli/src/com/android/tools/lint/Main.java b/lint/cli/src/com/android/tools/lint/Main.java
//Synthetic comment -- index 18a2da0..24c7cda 100644

//Synthetic comment -- @@ -1008,7 +1008,23 @@
for (int i = 0; i < column; i++) {
sb.append(' ');
}

                            boolean displayCaret = true;
                            Position endPosition = location.getEnd();
                            if (endPosition != null) {
                                int endLine = endPosition.getLine();
                                int endColumn = endPosition.getColumn();
                                if (endLine == line && endColumn > column) {
                                    for (int i = column; i < endColumn; i++) {
                                        sb.append("~");
                                    }
                                    displayCaret = false;
                                }
                            }

                            if (displayCaret) {
                                sb.append('^');
                            }
sb.append('\n');
warning.errorLine = sb.toString();
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Location.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Location.java
//Synthetic comment -- index f5e7595..1e3b9fa 100644

//Synthetic comment -- @@ -243,7 +243,7 @@
}
char c = contents.charAt(offset);
if (c == '\n') {
                lineOffset = offset + 1;
line++;
}
}
//Synthetic comment -- @@ -353,6 +353,10 @@
return new Location(file, new DefaultPosition(line, column, index),
new DefaultPosition(line, -1, end + patternEnd.length()));
}
                    } else if (hints != null && (hints.isJavaSymbol() || hints.isWholeWord())) {
                        return new Location(file, new DefaultPosition(line, column, index),
                                new DefaultPosition(line, column + patternStart.length(),
                                        index + patternStart.length()));
}
return new Location(file, new DefaultPosition(line, column, index),
new DefaultPosition(line, column, index + patternStart.length()));








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ButtonDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ButtonDetector.java
//Synthetic comment -- index b644b14..79cea2e 100644

//Synthetic comment -- @@ -249,7 +249,7 @@
&& isEnglishResource(context)
&& context.isEnabled(CASE)) {
assert label.equalsIgnoreCase(CANCEL_LABEL);
                                        context.report(CASE, context.getLocation(child),
String.format(
"The standard Android way to capitalize %1$s " +
"is \"Cancel\" (tip: use @android:string/ok instead)",
//Synthetic comment -- @@ -266,7 +266,7 @@
&& isEnglishResource(context)
&& context.isEnabled(CASE)) {
assert text.equalsIgnoreCase(OK_LABEL);
                                        context.report(CASE, context.getLocation(child),
String.format(
"The standard Android way to capitalize %1$s " +
"is \"OK\" (tip: use @android:string/ok instead)",








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/TranslationDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/TranslationDetector.java
//Synthetic comment -- index 70e0723..a60e206 100644

//Synthetic comment -- @@ -422,7 +422,7 @@
return;
}

                    Location location = context.getLocation(attribute);
location.setClientData(element);
location.setSecondary(mMissingLocations.get(name));
mMissingLocations.put(name, location);
//Synthetic comment -- @@ -433,7 +433,7 @@
mExtraLocations.remove(name);
return;
}
                Location location = context.getLocation(attribute);
location.setClientData(element);
location.setMessage("Also translated here");
location.setSecondary(mExtraLocations.get(name));








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/TypographyDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/TypographyDetector.java
//Synthetic comment -- index 5ad52b3..5ddc75b 100644

//Synthetic comment -- @@ -226,7 +226,7 @@
Node child = childNodes.item(i);
if (child.getNodeType() == Node.TEXT_NODE) {
String text = child.getNodeValue();
                checkText(context, element, child, text);
} else if (child.getNodeType() == Node.ELEMENT_NODE &&
child.getParentNode().getNodeName().equals(TAG_STRING_ARRAY)) {
// String array item children
//Synthetic comment -- @@ -235,19 +235,19 @@
Node item = items.item(j);
if (item.getNodeType() == Node.TEXT_NODE) {
String text = item.getNodeValue();
                        checkText(context, child, item, text);
}
}
}
}
}

    private void checkText(XmlContext context, Node element, Node textNode, String text) {
if (mCheckEllipsis) {
// Replace ... with ellipsis character?
int ellipsis = text.indexOf("..."); //$NON-NLS-1$
if (ellipsis != -1 && !text.startsWith(".", ellipsis + 3)) { //$NON-NLS-1$
                context.report(ELLIPSIS, element, context.getLocation(textNode),
ELLIPSIS_MESSAGE, null);
}
}
//Synthetic comment -- @@ -267,7 +267,7 @@
Character.isWhitespace(matcher.group(1).charAt(
matcher.group(1).length() - 1));
if (!isNegativeNumber) {
                        context.report(DASHES, element, context.getLocation(textNode),
EN_DASH_MESSAGE,
null);
}
//Synthetic comment -- @@ -278,7 +278,7 @@
// Don't suggest replacing -- or "--" with an m dash since these are sometimes
// used as digit marker strings
if (emdash > 1 && !text.startsWith("-", emdash + 2)) {   //$NON-NLS-1$
                    context.report(DASHES, element, context.getLocation(textNode),
EM_DASH_MESSAGE, null);
}
}
//Synthetic comment -- @@ -292,7 +292,7 @@
if (quoteEnd != -1 && quoteEnd > quoteStart + 1
&& (quoteEnd < text.length() -1 || quoteStart > 0)
&& SINGLE_QUOTE.matcher(text).matches()) {
                    context.report(QUOTES, element, context.getLocation(textNode),
SINGLE_QUOTE_MESSAGE, null);
return;
}
//Synthetic comment -- @@ -300,7 +300,7 @@
// Check for apostrophes that can be replaced by typographic apostrophes
if (quoteEnd == -1 && quoteStart > 0
&& Character.isLetterOrDigit(text.charAt(quoteStart - 1))) {
                    context.report(QUOTES, element, context.getLocation(textNode),
TYPOGRAPHIC_APOSTROPHE_MESSAGE, null);
return;
}
//Synthetic comment -- @@ -312,7 +312,7 @@
int quoteEnd = text.indexOf('"', quoteStart + 1);
if (quoteEnd != -1 && quoteEnd > quoteStart + 1) {
if (quoteEnd < text.length() -1 || quoteStart > 0) {
                        context.report(QUOTES, element, context.getLocation(textNode),
DBL_QUOTES_MESSAGE, null);
return;
}
//Synthetic comment -- @@ -322,7 +322,7 @@
// Check for grave accent quotations
if (text.indexOf('`') != -1 && GRAVE_QUOTATION.matcher(text).matches()) {
// Are we indenting ``like this'' or `this' ? If so, complain
                context.report(QUOTES, element, context.getLocation(textNode),
GRAVE_QUOTE_MESSAGE, null);
return;
}
//Synthetic comment -- @@ -341,19 +341,19 @@
String top = matcher.group(1);    // Numerator
String bottom = matcher.group(2); // Denominator
if (top.equals("1") && bottom.equals("2")) { //$NON-NLS-1$ //$NON-NLS-2$
                    context.report(FRACTIONS, element, context.getLocation(textNode),
String.format(FRACTION_MESSAGE, '\u00BD', "&#189;", "1/2"), null);
} else if (top.equals("1") && bottom.equals("4")) { //$NON-NLS-1$ //$NON-NLS-2$
                    context.report(FRACTIONS, element, context.getLocation(textNode),
String.format(FRACTION_MESSAGE, '\u00BC', "&#188;", "1/4"), null);
} else if (top.equals("3") && bottom.equals("4")) { //$NON-NLS-1$ //$NON-NLS-2$
                    context.report(FRACTIONS, element, context.getLocation(textNode),
String.format(FRACTION_MESSAGE, '\u00BE', "&#190;", "3/4"), null);
} else if (top.equals("1") && bottom.equals("3")) { //$NON-NLS-1$ //$NON-NLS-2$
                    context.report(FRACTIONS, element, context.getLocation(textNode),
String.format(FRACTION_MESSAGE, '\u2153', "&#8531;", "1/3"), null);
} else if (top.equals("2") && bottom.equals("3")) { //$NON-NLS-1$ //$NON-NLS-2$
                    context.report(FRACTIONS, element, context.getLocation(textNode),
String.format(FRACTION_MESSAGE, '\u2154', "&#8532;", "2/3"), null);
}
}
//Synthetic comment -- @@ -364,7 +364,7 @@
if (text.indexOf('(') != -1
&& (text.contains("(c)") || text.contains("(C)"))) { //$NON-NLS-1$ //$NON-NLS-2$
// Suggest replacing with copyright symbol?
                context.report(OTHER, element, context.getLocation(textNode),
COPYRIGHT_MESSAGE, null);
// Replace (R) and TM as well? There are unicode characters for these but they
// are probably not very common within Android app strings.








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/UnusedResourceDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/UnusedResourceDetector.java
//Synthetic comment -- index 2524a78..a593f1f 100644

//Synthetic comment -- @@ -337,8 +337,9 @@
public void visitElement(@NonNull XmlContext context, @NonNull Element element) {
if (TAG_RESOURCES.equals(element.getTagName())) {
for (Element item : LintUtils.getChildren(element)) {
                Attr nameAttribute = item.getAttributeNode(ATTR_NAME);
                if (nameAttribute != null) {
                    String name = nameAttribute.getValue();
if (name.indexOf('.') != -1) {
name = name.replace('.', '_');
}
//Synthetic comment -- @@ -363,7 +364,7 @@
mUnused.remove(resource);
return;
}
                            recordLocation(resource, context.getLocation(nameAttribute));
}
}
}







