/*Add support for permissions and intents in declaration hyperlinks

Go to Declaration hyperlinks now work for builtin intents and
permission references, such as
  <uses-permission android:name="android.permission.CHANGE_CON...
and
  <action android:name="android.intent.action.MAIN" />
and
  <category android:name="android.intent.category.HOME" />

Jumping to the declaration will open a browser tab in the editor,
showing the relevant documentation for the given intent/permission.

If SDK documentation is installed with the SDK manager, then it will
locate the locally installed documentation, and otherwise it will
point to the corresponding page under developer.android.com.

This changeset also improves handling of references to services and
activities; in addition to supporting the short form (a dot followed
by the base name) it now also handles fully qualified names and just
basenames.

Change-Id:I2cb38023bbe16111aa1feee3a8df10112aca774b*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java
//Synthetic comment -- index 29545d5..1d12e65 100644

//Synthetic comment -- @@ -27,6 +27,8 @@
import static com.android.ide.eclipse.adt.AndroidConstants.FN_RESOURCE_CLASS;
import static com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors.NAME_ATTR;
import static com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors.ROOT_ELEMENT;
import static com.android.sdklib.xml.AndroidManifest.ATTRIBUTE_NAME;
import static com.android.sdklib.xml.AndroidManifest.ATTRIBUTE_PACKAGE;
import static com.android.sdklib.xml.AndroidManifest.NODE_ACTIVITY;
//Synthetic comment -- @@ -37,6 +39,7 @@
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.resources.manager.FolderTypeRelationship;
//Synthetic comment -- @@ -114,6 +117,8 @@
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
//Synthetic comment -- @@ -139,6 +144,8 @@
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
//Synthetic comment -- @@ -153,6 +160,14 @@
*/
@SuppressWarnings("restriction")
public class Hyperlinks {
private Hyperlinks() {
// Not instantiatable. This is a container class containing shared code
// for the various inner classes that are actual hyperlink resolvers.
//Synthetic comment -- @@ -187,8 +202,7 @@
return false;
}

        if (isClassAttribute(context) || isOnClickAttribute(context) || isActivity(context)
                || isService(context)) {
return true;
}

//Synthetic comment -- @@ -237,6 +251,73 @@
return false;
}

/** Returns true if this represents a {@code <view class="foo.bar.Baz">} class attribute */
private static boolean isClassAttribute(XmlContext context) {
Attr attribute = context.getAttribute();
//Synthetic comment -- @@ -298,6 +379,80 @@
}

/**
* Returns the fully qualified class name of an activity referenced by the given
* AndroidManifest.xml node
*/
//Synthetic comment -- @@ -307,8 +462,17 @@
StringBuilder sb = new StringBuilder();
Element root = node.getOwnerDocument().getDocumentElement();
String pkg = root.getAttribute(ATTRIBUTE_PACKAGE);
        sb.append(pkg);
String className = attribute.getValue();
sb.append(className);
return sb.toString();
}
//Synthetic comment -- @@ -369,12 +533,8 @@
return false;
}

        if (isActivity(context)) {
            String fqcn = getActivityClassFqcn(context);
            return openJavaClass(project, fqcn);
        } else if (isService(context)) {
            String fqcn = getServiceClassFqcn(context);
            return openJavaClass(project, fqcn);
} else if (isClassElement(context) || isClassAttribute(context)) {
return openJavaClass(project, getClassFqcn(context));
} else if (isOnClickAttribute(context)) {







