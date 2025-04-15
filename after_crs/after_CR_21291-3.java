/*Properly parse mipmap names.

The extension was not removed making the references not found.

Change-Id:Ie420ea26df5bb87b86bd5b156597962b8c49252f*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/SingleResourceFile.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/SingleResourceFile.java
//Synthetic comment -- index 953e57a..22bdc24 100644

//Synthetic comment -- @@ -24,8 +24,6 @@

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.SAXParserFactory;

//Synthetic comment -- @@ -42,16 +40,6 @@
sParserFactory.setNamespaceAware(true);
}

private String mResourceName;
private ResourceType mType;
private ResourceValue mValue;
//Synthetic comment -- @@ -133,31 +121,11 @@
// get the name from the filename.
String name = getFile().getName();

        int pos = name.indexOf('.');
        if (pos != -1) {
            name = name.substring(0, pos);
}

return name;
}
}







