/*Underline the error range for aapt errors

Currently, aapt errors only show up as an error icon in the left hand
margin, and the user has to figure out from the error message where on
the line the error occurred.

This changeset uses information in the error message to identify the
specific range of text on the line that is affected, and underlines
it.

Change-Id:I322a8193af4e90bb972276aee80124771b6a46cd*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AaptParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AaptParser.java
//Synthetic comment -- index acf150a..bc96c09 100644

//Synthetic comment -- @@ -17,12 +17,17 @@
package com.android.ide.eclipse.adt.internal.build;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IRegion;

import java.io.File;
import java.util.List;
//Synthetic comment -- @@ -118,6 +123,17 @@
"^(invalid resource directory name): (.*)$"); //$NON-NLS-1$

/**
     * Portion of the error message which states the context in which the error occurred,
     * such as which property was being processed and what the string value was that
     * caused the error.
     * <p>
     * Example:
     * Error: No resource found that matches the given name (at 'text' with value '@string/foo')
     */
    private static final Pattern sValueRangePattern =
        Pattern.compile("\\(at '(.+)' with value '(.+)'\\)"); //$NON-NLS-1$

    /**
* 2 line aapt error<br>
* "ERROR: Invalid configuration: foo"<br>
* "                              ^^^"<br>
//Synthetic comment -- @@ -392,12 +408,53 @@
}
}

        // Attempt to determine the exact range of characters affected by this error.
        // This will look up the actual text of the file, go to the particular error line
        // and scan for the specific string mentioned in the error.
        int startOffset = -1;
        int endOffset = -1;
        if (f2 instanceof IFile) {
            Matcher matcher = sValueRangePattern.matcher(message);
            if (matcher.find()) {
                String property = matcher.group(1);
                String value = matcher.group(2);

                String fileContents = AdtPlugin.readFile((IFile) f2);
                if (fileContents != null && fileContents.length() > 0) {
                    Document document = new Document(fileContents);
                    try {
                        IRegion lineInfo = document.getLineInformation(line - 1);
                        String text = document.get(lineInfo.getOffset(), lineInfo.getLength());
                        int propertyIndex = text.indexOf(property);
                        int valueIndex = text.indexOf(value, propertyIndex + 1);
                        if (valueIndex != -1) {
                            startOffset = lineInfo.getOffset() + valueIndex;
                            endOffset = startOffset + value.length();
                        }
                    } catch (BadLocationException e) {
                        AdtPlugin.log(e, "Can't find range information for %1$s", location);
                    }
                }
            }
        }

// check if there's a similar marker already, since aapt is launched twice
boolean markerAlreadyExists = false;
try {
IMarker[] markers = f2.findMarkers(markerId, true, IResource.DEPTH_ZERO);

for (IMarker marker : markers) {
                if (startOffset != -1) {
                    int tmpBegin = marker.getAttribute(IMarker.CHAR_START, -1);
                    if (tmpBegin != startOffset) {
                        break;
                    }
                    int tmpEnd = marker.getAttribute(IMarker.CHAR_END, -1);
                    if (tmpEnd != startOffset) {
                        break;
                    }
                }

int tmpLine = marker.getAttribute(IMarker.LINE_NUMBER, -1);
if (tmpLine != line) {
break;
//Synthetic comment -- @@ -425,7 +482,8 @@
}

if (markerAlreadyExists == false) {
            BaseProjectHelper.markResource(f2, markerId, message, line,
                    startOffset, endOffset, severity);
}

return true;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/BaseProjectHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/BaseProjectHelper.java
//Synthetic comment -- index 7d00830..733b4a7 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.eclipse.adt.internal.project;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
//Synthetic comment -- @@ -140,6 +140,37 @@
}

/**
     * Adds a marker to a file on a specific line, for a specific range of text. This
     * methods catches thrown {@link CoreException}, and returns null instead.
     *
     * @param resource the resource to be marked
     * @param markerId The id of the marker to add.
     * @param message the message associated with the mark
     * @param lineNumber the line number where to put the mark. If line is < 1, it puts
     *            the marker on line 1,
     * @param startOffset the beginning offset of the marker (relative to the beginning of
     *            the document, not the line), or -1 for no range
     * @param endOffset the ending offset of the marker
     * @param severity the severity of the marker.
     * @return the IMarker that was added or null if it failed to add one.
     */
    public final static IMarker markResource(IResource resource, String markerId,
            String message, int lineNumber, int startOffset, int endOffset, int severity) {
        IMarker marker = markResource(resource, markerId, message, lineNumber, severity);
        if (startOffset != -1) {
            try {
                marker.setAttribute(IMarker.CHAR_START, startOffset);
                marker.setAttribute(IMarker.CHAR_END, endOffset);
            } catch (CoreException e) {
                AdtPlugin.log(e, "Failed to add marker '%1$s' to '%2$s'", //$NON-NLS-1$
                        markerId, resource.getFullPath());
            }
        }

        return marker;
    }

    /**
* Adds a marker to a resource. This methods catches thrown {@link CoreException},
* and returns null instead.
* @param resource the file to be marked







