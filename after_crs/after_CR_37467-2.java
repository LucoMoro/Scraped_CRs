/*Fix XML formatter to handle complex HTML strings better

The XML formatter did not handle <string> definitions where the
content contained nested HTML (where the tags included other tags than
<u>, <i> and <s>). An exmaple of a string which formats poorly is in
the testcase for issue 32619 (which is an issue unrelated to
formatting).

Change-Id:Ie70a0f0097aca99c40953a6e5dd4408f903d19e2*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/formatting/XmlPrettyPrinter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/formatting/XmlPrettyPrinter.java
//Synthetic comment -- index 22a86af..a5e26bf 100644

//Synthetic comment -- @@ -752,6 +752,10 @@
return false;
}

        if (isMarkupElement(element)) {
            return false;
        }

// See if this element should be separated from the previous element.
// This is the case if we are not compressing whitespace (checked above),
// or if we are not immediately following a comment (in which case the
//Synthetic comment -- @@ -876,14 +880,36 @@
return false;
}

        if (isMarkupElement(element)) {
            return false;
        }

return element.getParentNode().getNodeType() == Node.ELEMENT_NODE
&& !keepElementAsSingleLine(depth - 1, (Element) element.getParentNode());
}

private boolean isMarkupElement(Element element) {
        // The documentation suggests that the allowed tags are <u>, <b> and <i>:
        //   developer.android.com/guide/topics/resources/string-resource.html#FormattingAndStyling
        // However, the full set of tags accepted by Html.fromHtml is much larger. Therefore,
        // instead consider *any* element nested inside a <string> definition to be a markup
        // element. See frameworks/base/core/java/android/text/Html.java and look for
        // HtmlToSpannedConverter#handleStartTag.

        if (mStyle != XmlFormatStyle.RESOURCE) {
            return false;
        }

        Node curr = element.getParentNode();
        while (curr != null) {
            if (STRING_ELEMENT.equals(curr.getNodeName())) {
                return true;
            }

            curr = curr.getParentNode();
        }

        return false;
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/formatting/XmlPrettyPrinterTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/formatting/XmlPrettyPrinterTest.java
//Synthetic comment -- index 736931b..731621c 100644

//Synthetic comment -- @@ -236,7 +236,7 @@
"]>\n" +
"<LinearLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
"    android:orientation=\"vertical\" >\n" +
                "<![CDATA[\n" +
"This is character data!\n" +
"<!-- This is not a comment! -->\n" +
"and <this is not an element>\n" +
//Synthetic comment -- @@ -850,8 +850,9 @@
"<resources>\n" +
"\n" +
"    <string name=\"welcome\">Welcome to <b>Android</b>!</string>\n" +
                "    <string name=\"glob_settings_top_text\"><b>To install a 24 Clock Widget, " +
                "please <i>long press</i> in Home Screen.</b> Configure the Global Settings " +
                "here.</string>\n" +
"\n" +
"</resources>");
}
//Synthetic comment -- @@ -915,4 +916,25 @@
"\n" +
"</resources>");
}

    public void testComplexString() throws Exception {
        checkFormat(
                "res/values/strings.xml",
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources>\n" +
                "<string name=\"progress_completed_export_all\">The database has " +
                "<b>successfully</b> been exported into: <br /><br /><font size=\"14\">" +
                "\\\"<i>%s</i>\\\"</font></string>" +
                "</resources>",

                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources>\n" +
                "\n" +
                "    <string name=\"progress_completed_export_all\">The database has " +
                "<b>successfully</b> been exported into: <br /><br /><font size=\"14\">" +
                "\\\"<i>%s</i>\\\"</font></string>\n" +
                "\n" +
                "</resources>");
    }

}







