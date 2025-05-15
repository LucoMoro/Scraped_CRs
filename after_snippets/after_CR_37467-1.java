
//<Beginning of snippet n. 0>


return false;
}

        if (isMarkupElement(element)) {
            return false;
        }

// See if this element should be separated from the previous element.
// This is the case if we are not compressing whitespace (checked above),
// or if we are not immediately following a comment (in which case the
AdtUtils.endsWith(mOut, mOut.length() - mLineSeparator.length(), mLineSeparator);
}

    private boolean newlineAfterElementClose(Element element, int depth) { // TODO: if isMarkupElement don't do it
if (hasBlankLineAbove()) {
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
        // element:
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

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


"]>\n" +
"<LinearLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
"    android:orientation=\"vertical\" >\n" +
                "<![CDATA[\n" +
"This is character data!\n" +
"<!-- This is not a comment! -->\n" +
"and <this is not an element>\n" +
"<resources>\n" +
"\n" +
"    <string name=\"welcome\">Welcome to <b>Android</b>!</string>\n" +
                "    <string name=\"glob_settings_top_text\"><b>To install a 24 Clock Widget, " +
                "please <i>long press</i> in Home Screen.</b> Configure the Global Settings " +
                "here.</string>\n" +
"\n" +
"</resources>");
}
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

//<End of snippet n. 1>








