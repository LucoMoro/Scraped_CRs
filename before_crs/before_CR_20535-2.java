/*Parse min/targetSdkVersion and pass it to layoutlib.

Change-Id:I9865682e9ab54d476c631051ef7336e44b4cbfa7*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 15d46e9..91e970b 100644

//Synthetic comment -- @@ -67,8 +67,13 @@
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.Sdk.ITargetChangeListener;
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdkuilib.internal.widgets.ResolutionChooserDialog;

import org.eclipse.core.resources.IFile;
//Synthetic comment -- @@ -138,6 +143,8 @@
import java.util.Map;
import java.util.Set;

/**
* Graphical layout editor part, version 2.
* <p/>
//Synthetic comment -- @@ -1634,6 +1641,28 @@
configuredProjectRes, frameworkResources,
theme, isProjectTheme);

Params params = new Params(
topParser,
iProject /* projectKey */,








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifest.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifest.java
//Synthetic comment -- index 4012300..bc469e4 100644

//Synthetic comment -- @@ -208,6 +208,32 @@
}
}


/**
* Combines a java package, with a class value from the manifest to make a fully qualified







