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
import static com.android.sdklib.SdkConstants.FD_DOCS;
import static com.android.sdklib.SdkConstants.FD_DOCS_REFERENCE;
import static com.android.sdklib.xml.AndroidManifest.ATTRIBUTE_NAME;
import static com.android.sdklib.xml.AndroidManifest.ATTRIBUTE_PACKAGE;
import static com.android.sdklib.xml.AndroidManifest.NODE_ACTIVITY;
//Synthetic comment -- @@ -37,6 +39,7 @@
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestEditor;
import com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.resources.manager.FolderTypeRelationship;
//Synthetic comment -- @@ -114,6 +117,8 @@
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
//Synthetic comment -- @@ -139,6 +144,8 @@
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
//Synthetic comment -- @@ -153,6 +160,14 @@
*/
@SuppressWarnings("restriction")
public class Hyperlinks {
    private static final String CATEGORY = "category";                            //$NON-NLS-1$
    private static final String ACTION = "action";                                //$NON-NLS-1$
    private static final String PERMISSION = "permission";                        //$NON-NLS-1$
    private static final String USES_PERMISSION = "uses-permission";              //$NON-NLS-1$
    private static final String CATEGORY_PKG_PREFIX = "android.intent.category."; //$NON-NLS-1$
    private static final String ACTION_PKG_PREFIX = "android.intent.action.";     //$NON-NLS-1$
    private static final String PERMISSION_PKG_PREFIX = "android.permission.";    //$NON-NLS-1$

private Hyperlinks() {
// Not instantiatable. This is a container class containing shared code
// for the various inner classes that are actual hyperlink resolvers.
//Synthetic comment -- @@ -187,8 +202,7 @@
return false;
}

        if (isClassAttribute(context) || isOnClickAttribute(context) || isManifestName(context)) {
return true;
}

//Synthetic comment -- @@ -237,6 +251,73 @@
return false;
}

    /**
     * Returns true if this node/attribute pair corresponds to a manifest android:name reference
     */
    private static boolean isManifestName(XmlContext context) {
        Attr attribute = context.getAttribute();
        if (ATTRIBUTE_NAME.equals(attribute.getLocalName())
                && ANDROID_URI.equals(attribute.getNamespaceURI())) {
            if (getEditor() instanceof ManifestEditor) {
                return true;
            }
        }

        return false;
    }

    /**
     * Opens the declaration corresponding to an android:name reference in the
     * AndroidManifest.xml file
     */
    private static boolean openManifestName(IProject project, XmlContext context) {
        if (isActivity(context)) {
            String fqcn = getActivityClassFqcn(context);
            return openJavaClass(project, fqcn);
        } else if (isService(context)) {
            String fqcn = getServiceClassFqcn(context);
            return openJavaClass(project, fqcn);
        } else if (isBuiltinPermission(context)) {
            String permission = context.getAttribute().getValue();
            // Mutate something like android.permission.ACCESS_CHECKIN_PROPERTIES
            // into relative doc url android/Manifest.permission.html#ACCESS_CHECKIN_PROPERTIES
            assert permission.startsWith(PERMISSION_PKG_PREFIX);
            String relative = "android/Manifest.permission.html#" //$NON-NLS-1$
                    + permission.substring(PERMISSION_PKG_PREFIX.length());

            URL url = getDocUrl(relative);
            if (url != null) {
                openUrl(url);
                return true;
            } else {
                return false;
            }
        } else if (isBuiltinIntent(context)) {
            String intent = context.getAttribute().getValue();
            // Mutate something like android.intent.action.MAIN into
            // into relative doc url android/content/Intent.html#ACTION_MAIN
            String relative;
            if (intent.startsWith(ACTION_PKG_PREFIX)) {
                relative = "android/content/Intent.html#ACTION_" //$NON-NLS-1$
                        + intent.substring(ACTION_PKG_PREFIX.length());
            } else if (intent.startsWith(CATEGORY_PKG_PREFIX)) {
                relative = "android/content/Intent.html#CATEGORY_" //$NON-NLS-1$
                        + intent.substring(CATEGORY_PKG_PREFIX.length());
            } else {
                return false;
            }
            URL url = getDocUrl(relative);
            if (url != null) {
                openUrl(url);
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

/** Returns true if this represents a {@code <view class="foo.bar.Baz">} class attribute */
private static boolean isClassAttribute(XmlContext context) {
Attr attribute = context.getAttribute();
//Synthetic comment -- @@ -298,6 +379,80 @@
}

/**
     * Returns a URL pointing to the Android reference documentation, either installed
     * locally or the one on android.com
     *
     * @param relative a relative url to append to the root url
     * @return a URL pointing to the documentation
     */
    private static URL getDocUrl(String relative) {
        // First try to find locally installed documentation
        File sdkLocation = new File(Sdk.getCurrent().getSdkLocation());
        File docs = new File(sdkLocation, FD_DOCS + File.separator + FD_DOCS_REFERENCE);
        try {
            if (docs.exists()) {
                return new URL(docs.toURI().toURL().toExternalForm() + '/' + relative);
            }
            // If not, fallback to the online documentation
            return new URL("http://developer.android.com/reference/" + relative); //$NON-NLS-1$
        } catch (MalformedURLException e) {
            AdtPlugin.log(e, "Can't create URL for %1$s", docs);
            return null;
        }
    }

    /** Opens the given URL in a browser tab */
    private static void openUrl(URL url) {
        IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
        IWebBrowser browser;
        try {
            browser = support.createBrowser(AdtPlugin.PLUGIN_ID);
            browser.openURL(url);
        } catch (PartInitException e) {
            AdtPlugin.log(e, null);
        }
    }

    /** Returns true if the context is pointing to a permission name reference */
    private static boolean isBuiltinPermission(XmlContext context) {
        Attr attribute = context.getAttribute();
        Element node = context.getElement();

        // Is this an <activity> or <service> in an AndroidManifest.xml file? If so, jump to it
        String nodeName = node.getNodeName();
        if ((USES_PERMISSION.equals(nodeName) || PERMISSION.equals(nodeName))
                && ATTRIBUTE_NAME.equals(attribute.getLocalName())
                && ANDROID_URI.equals(attribute.getNamespaceURI())) {
            String value = attribute.getValue();
            if (value.startsWith(PERMISSION_PKG_PREFIX)) {
                return true;
            }
        }

        return false;
    }

    /** Returns true if the context is pointing to an intent reference */
    private static boolean isBuiltinIntent(XmlContext context) {
        Attr attribute = context.getAttribute();
        Element node = context.getElement();

        // Is this an <activity> or <service> in an AndroidManifest.xml file? If so, jump to it
        String nodeName = node.getNodeName();
        if ((ACTION.equals(nodeName) || CATEGORY.equals(nodeName))
                && ATTRIBUTE_NAME.equals(attribute.getLocalName())
                && ANDROID_URI.equals(attribute.getNamespaceURI())) {
            String value = attribute.getValue();
            if (value.startsWith(ACTION_PKG_PREFIX) || value.startsWith(CATEGORY_PKG_PREFIX)) {
                return true;
            }
        }

        return false;
    }


    /**
* Returns the fully qualified class name of an activity referenced by the given
* AndroidManifest.xml node
*/
//Synthetic comment -- @@ -307,8 +462,17 @@
StringBuilder sb = new StringBuilder();
Element root = node.getOwnerDocument().getDocumentElement();
String pkg = root.getAttribute(ATTRIBUTE_PACKAGE);
String className = attribute.getValue();
        if (className.startsWith(".")) { //$NON-NLS-1$
            sb.append(pkg);
        } else if (className.indexOf('.') == -1) {
            // According to the <activity> manifest element documentation, this is not
            // valid ( http://developer.android.com/guide/topics/manifest/activity-element.html )
            // but it appears in manifest files and appears to be supported by the runtime
            // so handle this in code as well:
            sb.append(pkg);
            sb.append('.');
        } // else: the class name is already a fully qualified class name
sb.append(className);
return sb.toString();
}
//Synthetic comment -- @@ -369,12 +533,8 @@
return false;
}

        if (isManifestName(context)) {
            return openManifestName(project, context);
} else if (isClassElement(context) || isClassAttribute(context)) {
return openJavaClass(project, getClassFqcn(context));
} else if (isOnClickAttribute(context)) {







