/*XML code completion improvements

First, fix code completion for @android: resources such that if you
type @android: the various resource types (@android:drawable/,
@android:layout/, etc) are offered.

Second, fix completion for the @android: token itself such that if you
type "@a" then "@android:" is offered as a completion.

Finally, make resource name completion work even for attributes that
aren't marked in the metadata as allowing resource references.  This
will not be done for empty completion context, but if the user
-explicitly- types a "@" in the value field, then resource completion
will work. This is necessary for some attributes where our metadata is
wrong, such as android:minHeight, where code completion currently
refuses to complete a @dimen/ completion prefix.

Change-Id:I175c8f7230d56987b9a945a2b791a2eb3e018a7c*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java
//Synthetic comment -- index 0309dc2..4b6df8f 100644

//Synthetic comment -- @@ -29,6 +29,7 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiFlagAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiResourceAttributeNode;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.sdklib.SdkConstants;

//Synthetic comment -- @@ -62,8 +63,6 @@
*/
public abstract class AndroidContentAssist implements IContentAssistProcessor {

/** Regexp to detect a full attribute after an element tag.
* <pre>Syntax:
*    name = "..." quoted string with all but < and "
//Synthetic comment -- @@ -355,6 +354,17 @@
}
}

            if (choices == null && value.startsWith("@")) { //$NON-NLS-1$
                // Special case: If the attribute value looks like a reference to a
                // resource, offer to complete it, since in many cases our metadata
                // does not correctly state whether a resource value is allowed. We don't
                // offer these for an empty completion context, but if the user has
                // actually typed "@", in that case list resource matches.
                // For example, for android:minHeight this makes completion on @dimen/
                // possible.
                choices = UiResourceAttributeNode.computeResourceStringMatches(mEditor, value);
            }

if (choices == null) {
// fallback on the older descriptor-only based lookup.









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiResourceAttributeNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiResourceAttributeNode.java
//Synthetic comment -- index 1af04a8..015a2f7 100644

//Synthetic comment -- @@ -198,12 +198,16 @@
*/
@Override
public String[] getPossibleValues(String prefix) {
UiElementNode uiNode = getUiParent();
AndroidXmlEditor editor = uiNode.getEditor();

        return computeResourceStringMatches(editor, prefix);
    }

    public static String[] computeResourceStringMatches(AndroidXmlEditor editor, String prefix) {
        ResourceRepository repository = null;
        boolean isSystem = false;

if (prefix == null || !prefix.contains(ANDROID_NS_NAME_PREFIX)) {
IProject project = editor.getProject();
if (project != null) {
//Synthetic comment -- @@ -242,12 +246,23 @@
// resource types.

for (ResourceType resType : resTypes) {
                if (isSystem) {
                    results.add("@" + ANDROID_NS_NAME_PREFIX + resType.getName() + "/"); //$NON-NLS-1$ //$NON-NLS-2$
                } else {
                    results.add("@" + resType.getName() + "/");         //$NON-NLS-1$ //$NON-NLS-2$
                }
if (resType == ResourceType.ID) {
// Also offer the + version to create an id from scratch
results.add("@+" + resType.getName() + "/");    //$NON-NLS-1$ //$NON-NLS-2$
}
}

            // Also add in @android: prefix to completion such that if user has typed
            // "@an" we offer to complete it.
            if (prefix == null ||
                    ANDROID_NS_NAME_PREFIX.regionMatches(0, prefix, 1, prefix.length() - 1)) {
                results.add("@" + ANDROID_NS_NAME_PREFIX); //$NON-NLS-1$
            }
} else if (repository != null) {
// We have a style name and a repository. Find all resources that match this
// type and recreate suggestions out of them.







