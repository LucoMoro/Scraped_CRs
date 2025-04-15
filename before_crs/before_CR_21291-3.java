/*Properly parse mipmap names.

The extension was not removed making the references not found.

Change-Id:Ie420ea26df5bb87b86bd5b156597962b8c49252f*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/SingleResourceFile.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/SingleResourceFile.java
//Synthetic comment -- index 953e57a..22bdc24 100644

//Synthetic comment -- @@ -24,8 +24,6 @@

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParserFactory;

//Synthetic comment -- @@ -42,16 +40,6 @@
sParserFactory.setNamespaceAware(true);
}

    private final static Pattern sXmlPattern = Pattern.compile("^(.+)\\.xml", //$NON-NLS-1$
            Pattern.CASE_INSENSITIVE);

    private final static Pattern[] sDrawablePattern = new Pattern[] {
        Pattern.compile("^(.+)\\.9\\.png", Pattern.CASE_INSENSITIVE), //$NON-NLS-1$
        Pattern.compile("^(.+)\\.png", Pattern.CASE_INSENSITIVE), //$NON-NLS-1$
        Pattern.compile("^(.+)\\.jpg", Pattern.CASE_INSENSITIVE), //$NON-NLS-1$
        Pattern.compile("^(.+)\\.gif", Pattern.CASE_INSENSITIVE), //$NON-NLS-1$
    };

private String mResourceName;
private ResourceType mType;
private ResourceValue mValue;
//Synthetic comment -- @@ -133,31 +121,11 @@
// get the name from the filename.
String name = getFile().getName();

        if (type == ResourceType.ANIM ||
                type == ResourceType.ANIMATOR ||
                type == ResourceType.COLOR ||
                type == ResourceType.INTERPOLATOR ||
                type == ResourceType.LAYOUT ||
                type == ResourceType.MENU ||
                type == ResourceType.XML) {
            Matcher m = sXmlPattern.matcher(name);
            if (m.matches()) {
                return m.group(1);
            }
        } else if (type == ResourceType.DRAWABLE) {
            for (Pattern p : sDrawablePattern) {
                Matcher m = p.matcher(name);
                if (m.matches()) {
                    return m.group(1);
                }
            }

            // also try the Xml pattern for selector/shape based drawable.
            Matcher m = sXmlPattern.matcher(name);
            if (m.matches()) {
                return m.group(1);
            }
}
return name;
}
}







