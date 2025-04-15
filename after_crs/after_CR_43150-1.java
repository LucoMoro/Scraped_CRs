/*37546: Graphical layout in Eclipse does not render unicode characters

Change-Id:I1fd07245e68b39af16a38bcec8a711867e87e5c6*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index 40d5e6f..cf79c90 100644

//Synthetic comment -- @@ -1360,4 +1360,47 @@
return AdtPlugin.getDisplay() != null
&& AdtPlugin.getDisplay().getThread() == Thread.currentThread();
}

    /**
     * Replaces any {@code \\uNNNN} references in the given string with the corresponding
     * unicode characters.
     *
     * @param s the string to perform replacements in
     * @return the string with unicode escapes replaced with actual characters
     */
    @NonNull
    public static String replaceUnicodeEscapes(@NonNull String s) {
        // Handle unicode escapes
        if (s.indexOf("\\u") != -1) { //$NON-NLS-1$
            StringBuilder sb = new StringBuilder(s.length());
            for (int i = 0, n = s.length(); i < n; i++) {
                char c = s.charAt(i);
                if (c == '\\' && i < n - 1) {
                    char next = s.charAt(i + 1);
                    if (next == 'u' && i < n - 5) { // case sensitive
                        String hex = s.substring(i + 2, i + 6);
                        try {
                            int unicodeValue = Integer.parseInt(hex, 16);
                            sb.append((char) unicodeValue);
                            i += 5;
                            continue;
                        } catch (NumberFormatException nufe) {
                            // Invalid escape: Just proceed to literally transcribe it
                            sb.append(c);
                        }
                    } else {
                        sb.append(c);
                        sb.append(next);
                        i++;
                        continue;
                    }
                } else {
                    sb.append(c);
                }
            }
            s = sb.toString();
        }

        return s;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ContextPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ContextPullParser.java
//Synthetic comment -- index 0eee47a..c30574b 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import com.android.SdkConstants;
import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.IProjectCallback;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutMetadata;

import org.kxml2.io.KXmlParser;
//Synthetic comment -- @@ -118,6 +119,11 @@
return VALUE_FILL_PARENT;
}

        // Handle unicode escapes
        if (value.indexOf('\\') != -1) {
            value = AdtUtils.replaceUnicodeEscapes(value);
        }

return value;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java
//Synthetic comment -- index 53f1e6b..de09d00 100644

//Synthetic comment -- @@ -29,6 +29,7 @@

import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.FragmentMenu;
//Synthetic comment -- @@ -393,6 +394,11 @@
return VALUE_FILL_PARENT;
}

                // Handle unicode escapes
                if (value.indexOf('\\') != -1) {
                    value = AdtUtils.replaceUnicodeEscapes(value);
                }

return value;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/AdtUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/AdtUtilsTest.java
//Synthetic comment -- index e0ebdbc..0d4e02e 100644

//Synthetic comment -- @@ -174,4 +174,17 @@
Locale.setDefault(originalDefaultLocale);
}
}

    public void testEscapeUnicodeChars() throws Exception {
        assertEquals("", AdtUtils.replaceUnicodeEscapes(""));
        assertEquals("foo bar", AdtUtils.replaceUnicodeEscapes("foo bar"));
        assertEquals("\u25C0", AdtUtils.replaceUnicodeEscapes("\\u25C0"));
        assertEquals("!\u25C0\u25C1!", AdtUtils.replaceUnicodeEscapes("!\\u25C0\\u25C1!"));
        assertEquals("\u1234\\", AdtUtils.replaceUnicodeEscapes("\\u1234\\"));

        assertEquals("\\U25C0", AdtUtils.replaceUnicodeEscapes("\\U25C0")); // no unicode expand
        assertEquals("\\u25C", AdtUtils.replaceUnicodeEscapes("\\u25C")); // no unicode expand
        assertEquals("\\\\u25C0", AdtUtils.replaceUnicodeEscapes("\\\\u25C0")); // escaped
        assertEquals("\\u123\\", AdtUtils.replaceUnicodeEscapes("\\u123\\")); // broken
    }
}







