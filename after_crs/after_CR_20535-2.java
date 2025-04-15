/*Parse min/targetSdkVersion and pass it to layoutlib.

Change-Id:I9865682e9ab54d476c631051ef7336e44b4cbfa7*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 15d46e9..91e970b 100644

//Synthetic comment -- @@ -67,8 +67,13 @@
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.Sdk.ITargetChangeListener;
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.ide.eclipse.adt.io.IFolderWrapper;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.io.IAbstractFile;
import com.android.sdklib.io.StreamException;
import com.android.sdklib.xml.AndroidManifest;
import com.android.sdklib.xml.AndroidManifestParser;
import com.android.sdkuilib.internal.widgets.ResolutionChooserDialog;

import org.eclipse.core.resources.IFile;
//Synthetic comment -- @@ -138,6 +143,8 @@
import java.util.Map;
import java.util.Set;

import javax.xml.xpath.XPathExpressionException;

/**
* Graphical layout editor part, version 2.
* <p/>
//Synthetic comment -- @@ -1634,6 +1641,28 @@
configuredProjectRes, frameworkResources,
theme, isProjectTheme);

        // FIXME cache and only look up when the manifest changes.
        IAbstractFile manifestFile = AndroidManifestParser.getManifest(
                new IFolderWrapper(iProject));
        int minSdkVersion = 1;
        int targetSdkVersion = 1;
        if (manifestFile != null) {
            try {
                minSdkVersion = AndroidManifest.getMinSdkVersion(manifestFile);
                if (minSdkVersion == -1) {
                    minSdkVersion = 1; // don't support codename.
                }
                targetSdkVersion = AndroidManifest.getTargetSdkVersion(manifestFile);
                if (targetSdkVersion <= 1) {
                    targetSdkVersion = minSdkVersion;
                }
            } catch (XPathExpressionException e) {
                // do nothing we'll use 1 below.
            } catch (StreamException e) {
                // do nothing we'll use 1 below.
            }
        }

Params params = new Params(
topParser,
iProject /* projectKey */,








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifest.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifest.java
//Synthetic comment -- index 4012300..bc469e4 100644

//Synthetic comment -- @@ -208,6 +208,32 @@
}
}

    /**
     * Returns the value of the targetSdkVersion attribute (defaults to 1 if the attribute is
     * not set), or -1 if the value is a codename.
     * @param manifestFile the manifest file to read the attribute from.
     * @return the integer value or -1 if not set.
     * @throws XPathExpressionException
     * @throws StreamException If any error happens when reading the manifest.
     */
    public static int getTargetSdkVersion(IAbstractFile manifestFile)
            throws XPathExpressionException, StreamException {
        XPath xPath = AndroidXPathFactory.newXPath();

        String result = xPath.evaluate(
                "/"  + NODE_MANIFEST +
                "/"  + NODE_USES_SDK +
                "/@" + AndroidXPathFactory.DEFAULT_NS_PREFIX +
                ":"  + ATTRIBUTE_TARGET_SDK_VERSION,
                new InputSource(manifestFile.getContents()));

        try {
            return Integer.parseInt(result);
        } catch (NumberFormatException e) {
            return result.length() > 0 ? -1 : 1;
        }
    }


/**
* Combines a java package, with a class value from the manifest to make a fully qualified







