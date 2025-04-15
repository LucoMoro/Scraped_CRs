/*39612: Question Mark causes Eclipse Graphical Layout Editor to Freak Out

Handle string values starting with ? and @ even if they do not correspond
to actual theme or resource URLs.

Also fix the code which handles processing strings read from XML files;
apply unescaping rules (for unicode, newlines and tabs, removing quotes,
etc).

Also make the style warning include the full resource URI (it was only
logging the stripped URI).

Change-Id:Iae84b0449a90d0b4d54f4ca0fd2cf8000e64a80c*/
//Synthetic comment -- diff --git a/sdk_common/src/main/java/com/android/ide/common/resources/ResourceResolver.java b/sdk_common/src/main/java/com/android/ide/common/resources/ResourceResolver.java
//Synthetic comment -- index e053724..ed6840a 100644

//Synthetic comment -- @@ -189,7 +189,8 @@
if (reference == null) {
return null;
}
        if (reference.startsWith(PREFIX_THEME_REF)) {
// no theme? no need to go further!
if (mTheme == null) {
return null;
//Synthetic comment -- @@ -198,6 +199,7 @@
boolean frameworkOnly = false;

// eliminate the prefix from the string
if (reference.startsWith(ANDROID_THEME_PREFIX)) {
frameworkOnly = true;
reference = reference.substring(ANDROID_THEME_PREFIX.length());
//Synthetic comment -- @@ -207,7 +209,7 @@

// at this point, value can contain type/name (drawable/foo for instance).
// split it to make sure.
            String[] segments = reference.split("\\/");

// we look for the referenced item name.
String referenceName = null;
//Synthetic comment -- @@ -224,6 +226,18 @@
} else {
// it's just an item name.
referenceName = segments[0];
}

// now we look for android: in the referenceName in order to support format
//Synthetic comment -- @@ -241,7 +255,7 @@
mLogger.warning(LayoutLog.TAG_RESOURCES_RESOLVE_THEME_ATTR,
String.format("Couldn't find theme resource %1$s for the current theme",
reference),
                        new ResourceValue(ResourceType.ATTR, referenceName, frameworkOnly));
}

return item;
//Synthetic comment -- @@ -262,16 +276,17 @@
}

// at this point, value contains type/[android:]name (drawable/foo for instance)
            String[] segments = reference.split("\\/");
            if (segments.length <= 1) {
return null;
}

// now we look for android: in the resource name in order to support format
// such as: @drawable/android:name
            if (segments[1].startsWith(PREFIX_ANDROID)) {
frameworkOnly = true;
                segments[1] = segments[1].substring(PREFIX_ANDROID.length());
}

ResourceType type = ResourceType.getEnum(segments[0]);
//Synthetic comment -- @@ -281,7 +296,19 @@
return null;
}

            return findResValue(type, segments[1],
forceFrameworkOnly ? true :frameworkOnly);
}









//Synthetic comment -- diff --git a/sdk_common/src/main/java/com/android/ide/common/resources/ValueResourceParser.java b/sdk_common/src/main/java/com/android/ide/common/resources/ValueResourceParser.java
//Synthetic comment -- index 53a6bd1..a2f7f8d 100644

//Synthetic comment -- @@ -16,6 +16,11 @@

package com.android.ide.common.resources;

import com.android.ide.common.rendering.api.AttrResourceValue;
import com.android.ide.common.rendering.api.DeclareStyleableResourceValue;
import com.android.ide.common.rendering.api.ResourceValue;
//Synthetic comment -- @@ -64,7 +69,7 @@
@Override
public void endElement(String uri, String localName, String qName) throws SAXException {
if (mCurrentValue != null) {
            mCurrentValue.setValue(trimXmlWhitespaces(mCurrentValue.getValue()));
}

if (inResources && qName.equals(NODE_RESOURCES)) {
//Synthetic comment -- @@ -213,96 +218,261 @@
}
}

    public static String trimXmlWhitespaces(String value) {
        if (value == null) {
return null;
}

        // look for carriage return and replace all whitespace around it by just 1 space.
        int index;

        while ((index = value.indexOf('\n')) != -1) {
            // look for whitespace on each side
            int left = index - 1;
            while (left >= 0) {
                if (Character.isWhitespace(value.charAt(left))) {
                    left--;
                } else {
break;
}
}

            int right = index + 1;
            int count = value.length();
            while (right < count) {
                if (Character.isWhitespace(value.charAt(right))) {
                    right++;
                } else {
break;
}
}

            // remove all between left and right (non inclusive) and replace by a single space.
            String leftString = null;
            if (left >= 0) {
                leftString = value.substring(0, left + 1);
}
            String rightString = null;
            if (right < count) {
                rightString = value.substring(right);
}

            if (leftString != null) {
                value = leftString;
                if (rightString != null) {
                    value += " " + rightString;
}
} else {
                value = rightString != null ? rightString : "";
            }
        }

        // now we un-escape the string
        int length = value.length();
        char[] buffer = value.toCharArray();

        for (int i = 0 ; i < length ; i++) {
            if (buffer[i] == '\\' && i + 1 < length) {
                if (buffer[i+1] == 'u') {
                    if (i + 5 < length) {
                        // this is unicode char \u1234
                        int unicodeChar = Integer.parseInt(new String(buffer, i+2, 4), 16);

                        // put the unicode char at the location of the \
                        buffer[i] = (char)unicodeChar;

                        // offset the rest of the buffer since we go from 6 to 1 char
                        if (i + 6 < buffer.length) {
                            System.arraycopy(buffer, i+6, buffer, i+1, length - i - 6);
                        }
                        length -= 5;
}
                } else {
                    if (buffer[i+1] == 'n') {
                        // replace the 'n' char with \n
                        buffer[i+1] = '\n';
                    }

                    // offset the buffer to erase the \
                    System.arraycopy(buffer, i+1, buffer, i, length - i - 1);
                    length--;
}
            } else if (buffer[i] == '"') {
                // if the " was escaped it would have been processed above.
                // offset the buffer to erase the "
                System.arraycopy(buffer, i+1, buffer, i, length - i - 1);
                length--;

                // unlike when unescaping, we want to process the next char too
                i--;
}
}

        return new String(buffer, 0, length);
}
}








//Synthetic comment -- diff --git a/sdk_common/src/test/java/com/android/ide/common/resources/ValueResourceParserTest.java b/sdk_common/src/test/java/com/android/ide/common/resources/ValueResourceParserTest.java
new file mode 100644
//Synthetic comment -- index 0000000..aed6060

//Synthetic comment -- @@ -0,0 +1,148 @@







