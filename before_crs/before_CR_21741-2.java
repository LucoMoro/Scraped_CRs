/*Underline the error range for aapt errors

Currently, aapt errors only show up as an error icon in the left hand
margin, and the user has to figure out from the error message where on
the line the error occurred.

This changeset uses information in the error message to identify the
specific range of text on the line that is affected, and underlines
it.

Change-Id:I322a8193af4e90bb972276aee80124771b6a46cd*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AaptParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AaptParser.java
//Synthetic comment -- index acf150a..f433570 100644

//Synthetic comment -- @@ -17,12 +17,18 @@
package com.android.ide.eclipse.adt.internal.build;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import java.io.File;
import java.util.List;
//Synthetic comment -- @@ -118,6 +124,17 @@
"^(invalid resource directory name): (.*)$"); //$NON-NLS-1$

/**
* 2 line aapt error<br>
* "ERROR: Invalid configuration: foo"<br>
* "                              ^^^"<br>
//Synthetic comment -- @@ -392,12 +409,56 @@
}
}

// check if there's a similar marker already, since aapt is launched twice
boolean markerAlreadyExists = false;
try {
IMarker[] markers = f2.findMarkers(markerId, true, IResource.DEPTH_ZERO);

for (IMarker marker : markers) {
int tmpLine = marker.getAttribute(IMarker.LINE_NUMBER, -1);
if (tmpLine != line) {
break;
//Synthetic comment -- @@ -425,7 +486,8 @@
}

if (markerAlreadyExists == false) {
            BaseProjectHelper.markResource(f2, markerId, message, line, severity);
}

return true;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/BaseProjectHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/BaseProjectHelper.java
//Synthetic comment -- index 7d00830..733b4a7 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.eclipse.adt.internal.project;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
//Synthetic comment -- @@ -140,6 +140,37 @@
}

/**
* Adds a marker to a resource. This methods catches thrown {@link CoreException},
* and returns null instead.
* @param resource the file to be marked







