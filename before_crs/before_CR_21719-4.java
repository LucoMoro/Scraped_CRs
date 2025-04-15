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
//Synthetic comment -- index 0309dc2..9bc09ec 100644

//Synthetic comment -- @@ -29,6 +29,7 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiFlagAttributeNode;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.sdklib.SdkConstants;

//Synthetic comment -- @@ -62,8 +63,6 @@
*/
public abstract class AndroidContentAssist implements IContentAssistProcessor {

    private static final String ATTRIBUTE_ICON_FILENAME = "attribute";

/** Regexp to detect a full attribute after an element tag.
* <pre>Syntax:
*    name = "..." quoted string with all but < and "
//Synthetic comment -- @@ -353,6 +352,18 @@
}
}
}
}

if (choices == null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java
//Synthetic comment -- index b969d30..93b6baf 100644

//Synthetic comment -- @@ -1546,7 +1546,7 @@
*              e.g. SdkConstants.NS_RESOURCES
* @return The first prefix declared or the default "android" prefix.
*/
    private String lookupNamespacePrefix(Node node, String nsUri) {
// Note: Node.lookupPrefix is not implemented in wst/xml/core NodeImpl.java
// The following code emulates this simple call:
//   String prefix = node.lookupPrefix(SdkConstants.NS_RESOURCES);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiResourceAttributeNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiResourceAttributeNode.java
//Synthetic comment -- index 1af04a8..9032b46 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.eclipse.adt.internal.editors.uimodel;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_NS_NAME_PREFIX;

import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
//Synthetic comment -- @@ -198,13 +198,15 @@
*/
@Override
public String[] getPossibleValues(String prefix) {
ResourceRepository repository = null;
boolean isSystem = false;

        UiElementNode uiNode = getUiParent();
AndroidXmlEditor editor = uiNode.getEditor();

        if (prefix == null || !prefix.contains(ANDROID_NS_NAME_PREFIX)) {
IProject project = editor.getProject();
if (project != null) {
// get the resource repository for this project and the system resources.
//Synthetic comment -- @@ -242,12 +244,23 @@
// resource types.

for (ResourceType resType : resTypes) {
                results.add("@" + resType.getName() + "/");         //$NON-NLS-1$ //$NON-NLS-2$
if (resType == ResourceType.ID) {
// Also offer the + version to create an id from scratch
results.add("@+" + resType.getName() + "/");    //$NON-NLS-1$ //$NON-NLS-2$
}
}
} else if (repository != null) {
// We have a style name and a repository. Find all resources that match this
// type and recreate suggestions out of them.
//Synthetic comment -- @@ -261,7 +274,7 @@
}

if (isSystem) {
                    sb.append(ANDROID_NS_NAME_PREFIX);
}

sb.append(typeName).append('/');








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/manifest/model/UiElementNodeTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/manifest/model/UiElementNodeTest.java
//Synthetic comment -- index 30f709c..ccf4e83 100644

//Synthetic comment -- @@ -16,15 +16,24 @@

package com.android.ide.eclipse.adt.internal.editors.manifest.model;

import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor.Mandatory;
import com.android.ide.eclipse.adt.internal.editors.mock.MockXmlNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;

import org.w3c.dom.Node;

import java.util.Iterator;

import junit.framework.TestCase;

public class UiElementNodeTest extends TestCase {
//Synthetic comment -- @@ -252,4 +261,30 @@
}


}







