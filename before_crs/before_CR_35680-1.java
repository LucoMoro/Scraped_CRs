/*Improve XML CData formatting

Fix 29277: Strings XML formatter: unwanted whitespaces
           inserted when using CDATA sectionhttp://code.google.com/p/android/issues/detail?id=29277This changes the XML formatter to keep CDATA sections
not containing newlines to keep the CDATA on the same
line without indentation or newlines, and multi-line
CDATA sections to be separated on their own line and
flushed left.

Change-Id:I8576cff87e2880b0264479cc24c54cecc7841340*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/formatting/XmlPrettyPrinter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/formatting/XmlPrettyPrinter.java
//Synthetic comment -- index 0cf08eb..754da29 100644

//Synthetic comment -- @@ -264,11 +264,17 @@
}

private void printCharacterData(int depth, Node node) {
        indent(depth);
mOut.append("<![CDATA["); //$NON-NLS-1$
        mOut.append(node.getNodeValue());
        mOut.append("]]>");     //$NON-NLS-1$
        mOut.append(mLineSeparator);
}

private void printText(Node node) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/formatting/XmlPrettyPrinterTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/formatting/XmlPrettyPrinterTest.java
//Synthetic comment -- index fe7dcdd..736931b 100644

//Synthetic comment -- @@ -236,7 +236,7 @@
"]>\n" +
"<LinearLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
"    android:orientation=\"vertical\" >\n" +
                "    <![CDATA[\n" +
"This is character data!\n" +
"<!-- This is not a comment! -->\n" +
"and <this is not an element>\n" +
//Synthetic comment -- @@ -875,4 +875,44 @@
"\n" +
"</resources>");
}
}







