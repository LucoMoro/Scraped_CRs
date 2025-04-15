/*Custom view class completion
TODO: Handle class= attribute completion as well (this should be generic). Also this code should all go into the layout subclass of the content assist.

Change-Id:I0889db75944e22ba52f5a979b70954105940baae*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java
//Synthetic comment -- index 46af01e..0d00b76 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextValueDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.XmlnsAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
//Synthetic comment -- @@ -41,6 +42,9 @@
import com.android.util.Pair;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
//Synthetic comment -- @@ -60,6 +64,7 @@

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
//Synthetic comment -- @@ -190,11 +195,6 @@
// We are still editing the element's tag name, not the attributes
// (the element's tag name may not even be complete)

            Object[] choices = getChoicesForElement(parent, currentNode);
            if (choices == null || choices.length == 0) {
                return;
            }

int selectionLength = getSelectionLength(viewer);
int replaceLength = parent.length() - wordPrefix.length();
boolean isNew = replaceLength == 0 && nextNonspaceChar(viewer, offset) == '<';
//Synthetic comment -- @@ -211,6 +211,14 @@
// Per XML Spec, there's no whitespace between "<" or "</" and the tag name.
char needTag = computeElementNeedTag(viewer, offset, wordPrefix);

addMatchingProposals(proposals, choices, offset,
parentNode != null ? parentNode : null, wordPrefix, needTag,
false /* isAttribute */, isNew, false /*isComplete*/,
//Synthetic comment -- @@ -218,6 +226,46 @@
}
}

private int getSelectionLength(ITextViewer viewer) {
// get the selection length
int selectionLength = 0;
//Synthetic comment -- @@ -1045,6 +1093,7 @@
// Remove the leading element name. By spec, it must be after the < without
// any whitespace. If there's nothing left, no attribute has been defined yet.
// Be sure to keep any whitespace after the initial word if any, as it matters.
text = sFirstElementWord.matcher(text).replaceFirst("");  //$NON-NLS-1$

// There MUST be space after the element name. If not, the cursor is still








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CustomViewFinder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CustomViewFinder.java
//Synthetic comment -- index 3226a02..ab4b57f 100644

//Synthetic comment -- @@ -115,10 +115,14 @@
}

public void refresh() {
        refresh(null);
}

public void refresh(final Listener listener) {
// Add this listener to the list of listeners which should be notified when the
// search is done. (There could be more than one since multiple requests could
// arrive for a slow search since the search is run in a different thread).
//Synthetic comment -- @@ -139,6 +143,13 @@

FindViewsJob job = new FindViewsJob();
job.schedule();
}

public Collection<String> getCustomViews() {







