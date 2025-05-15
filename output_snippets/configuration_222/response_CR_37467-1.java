//<Beginning of snippet n. 0>
return false;
}

// See if this element should be separated from the previous element.
// This is the case if we are not compressing whitespace (checked above),
// or if we are not immediately following a comment (in which case the
AdtUtils.endsWith(mOut, mOut.length() - mLineSeparator.length(), mLineSeparator);
}

private boolean newlineAfterElementClose(Element element, int depth) {
    if (hasBlankLineAbove()) {
        return false;
    }

    return element.getParentNode().getNodeType() == Node.ELEMENT_NODE
            && !keepElementAsSingleLine(depth - 1, (Element) element.getParentNode());
}

private boolean isMarkupElement(Element element) {
    // <u>, <b>, <i>, <div>, ...
    // http://developer.android.com/guide/topics/resources/string-resource.html#FormattingAndStyling
    return mStyle == XmlFormatStyle.RESOURCE && (element.getTagName().length() > 0);
}

/**
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
"]>\n" +
"<LinearLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
"    android:orientation=\"vertical\" >\n" +
                " <![CDATA[\n" +
"This is character data!\n" +
"<!-- This is not a comment! -->\n" +
"and <this is not an element>\n" +
"<resources>\n" +
"\n" +
"    <string name=\"welcome\">Welcome to <b>Android</b>!</string>\n" +
                "    <string name=\"glob_settings_top_text\"><b>To install a 24 Clock Widget, please <i>long press</i>\n" +
                " in Home Screen.</b> Configure the Global Settings here.</string>\n" +
"\n" +
"</resources>");
}
"\n" +
"</resources>");
}
}

//<End of snippet n. 1>