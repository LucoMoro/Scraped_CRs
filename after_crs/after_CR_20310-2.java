/*Use ResourceChooser for Strings

When editing the text property (via "Edit Text..."), show the
ResourceChooser (assigned to ResourceType.STRING) instead of the more
generic ReferenceChooser.

Some minor tweaks to incorporate feedback on an earlier CL.

Change-Id:I935456075910acd59cedbfeb4cb43680038e412d*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/api/IClientRulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/api/IClientRulesEngine.java
//Synthetic comment -- index d6ff1bd..6a01e8a 100755

//Synthetic comment -- @@ -108,5 +108,15 @@
*/
String displayReferenceInput(String currentValue);

    /**
     * Displays an input dialog where the user can enter an Android resource name of the
     * given resource type ("id", "string", "drawable", and so on.)
     *
     * @param currentValue the current reference to select
     * @param resourceTypeName resource type, such as "id", "string", and so on (never
     *            null)
     * @return the resource selected by the user, or null
     */
    String displayResourceInput(String resourceTypeName, String currentValue);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java
//Synthetic comment -- index 131bf7a..3674633 100644

//Synthetic comment -- @@ -186,7 +186,7 @@
} else if (fullActionId.equals(EDIT_TEXT_ID)) {
String oldText = node.getStringAttr(ANDROID_URI, ATTR_TEXT);
oldText = ensureValidString(oldText);
                    String newText = mRulesEngine.displayResourceInput("string", oldText); //$NON-NLS-1$
if (newText != null) {
node.editXml("Change text", new PropertySettingNodeHandler(ANDROID_URI,
ATTR_TEXT, newText));
//Synthetic comment -- @@ -264,6 +264,20 @@
&& IAttributeInfo.Format.REFERENCE.in(attributeInfo.getFormats())) {
return mRulesEngine.displayReferenceInput(oldValue);
} else {
                    // A single resource type? If so use a resource chooser initialized
                    // to this specific type
                    /* This does not work well, because the metadata is a bit misleading:
                     * for example a Button's "text" property and a Button's "onClick" property
                     * both claim to be of type [string], but @string/ is NOT valid for
                     * onClick..
                    if (attributeInfo != null && attributeInfo.getFormats().length == 1) {
                        // Resource chooser
                        Format format = attributeInfo.getFormats()[0];
                        return mRulesEngine.displayResourceInput(format.name(), oldValue);
                    }
                    */

                    // Fallback: just edit the raw XML string
String message = String.format("New %1$s Value:", attribute);
return mRulesEngine.displayInput(message, oldValue, null);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java
//Synthetic comment -- index 63394ad..dd58b56 100644

//Synthetic comment -- @@ -677,48 +677,49 @@
* <p/>
* This does not override attributes which are not empty.
*/
    public static void setDefaultLayoutAttributes(UiElementNode uiNode, boolean updateLayout) {
        // if this uiNode is a layout and we're adding it to a document, use match_parent for
// both W/H. Otherwise default to wrap_layout.
        boolean fill = uiNode.getDescriptor().hasChildren() &&
                       uiNode.getUiParent() instanceof UiDocumentNode;
        uiNode.setAttributeValue(
ATTR_LAYOUT_WIDTH,
SdkConstants.NS_RESOURCES,
fill ? VALUE_FILL_PARENT : VALUE_WRAP_CONTENT,
false /* override */);
        uiNode.setAttributeValue(
ATTR_LAYOUT_HEIGHT,
SdkConstants.NS_RESOURCES,
fill ? VALUE_FILL_PARENT : VALUE_WRAP_CONTENT,
false /* override */);

        String widgetId = getFreeWidgetId(uiNode);
        if (widgetId != null) {
            uiNode.setAttributeValue(
ATTR_ID,
SdkConstants.NS_RESOURCES,
                    widgetId,
false /* override */);
}

        String widgetType = uiNode.getDescriptor().getUiName();
        uiNode.setAttributeValue(
ATTR_TEXT,
SdkConstants.NS_RESOURCES,
                widgetType,
false /*override*/);

if (updateLayout) {
            UiElementNode uiParent = uiNode.getUiParent();
            if (uiParent != null &&
                    uiParent.getDescriptor().getXmlLocalName().equals(
RELATIVE_LAYOUT)) {
                UiElementNode uiPrevious = uiNode.getUiPreviousSibling();
                if (uiPrevious != null) {
                    String id = uiPrevious.getAttributeValue(ATTR_ID);
if (id != null && id.length() > 0) {
id = id.replace("@+", "@");                     //$NON-NLS-1$ //$NON-NLS-2$
                        uiNode.setAttributeValue(
ATTR_LAYOUT_BELOW,
SdkConstants.NS_RESOURCES,
id,
//Synthetic comment -- @@ -807,6 +808,9 @@
prefix = prefix.replaceAll("[^a-zA-Z]", "");                //$NON-NLS-1$ $NON-NLS-2$
if (prefix.length() == 0) {
prefix = DEFAULT_WIDGET_PREFIX;
            } else {
                // Lowercase initial character
                prefix = Character.toLowerCase(prefix.charAt(0)) + prefix.substring(1);
}

do {
//Synthetic comment -- @@ -823,12 +827,12 @@
if (id != null) {
id = id.replace(NEW_ID_PREFIX, "");                            //$NON-NLS-1$
id = id.replace(ID_PREFIX, "");                                //$NON-NLS-1$
            if (map.add(id.toLowerCase()) && map.contains(generated.toLowerCase())) {

do {
num++;
generated = String.format("%1$s%2$02d", prefix, num);   //$NON-NLS-1$
                } while (map.contains(generated.toLowerCase()));

params[1] = num;
params[2] = generated;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index e5e3a13..f3c62ec 100755

//Synthetic comment -- @@ -30,7 +30,6 @@
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.XmlnsAttributeDescriptor;
//Synthetic comment -- @@ -723,9 +722,7 @@

// This doesn't apply to all, but doesn't seem to cause harm and makes for a
// better experience with text-oriented views like buttons and texts
            element.setAttributeNS(SdkConstants.NS_RESOURCES, ATTR_TEXT, desc.getUiName());

document.appendChild(element);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PlayAnimationMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PlayAnimationMenu.java
//Synthetic comment -- index b1bd79f..df41e67 100644

//Synthetic comment -- @@ -50,7 +50,7 @@
* "Play Animation" context menu which lists available animations in the project and in
* the framework, as well as a "Create Animation" shortcut, and allows the animation to be
* run on the selection
 * <p/>
* TODO: Add transport controls for play/rewind/pause/loop, and (if possible) scrubbing
*/
public class PlayAnimationMenu extends SubmenuAction {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index 24366da..2bcc32f 100755

//Synthetic comment -- @@ -36,11 +36,13 @@
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.SimpleElement;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.resources.IResourceRepository;
import com.android.ide.eclipse.adt.internal.resources.ResourceType;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.ui.ReferenceChooserDialog;
import com.android.ide.eclipse.adt.internal.ui.ResourceChooser;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.internal.project.ProjectProperties;

//Synthetic comment -- @@ -816,5 +818,36 @@

return null;
}

        public String displayResourceInput(String resourceTypeName, String currentValue) {
            AndroidXmlEditor editor = mEditor.getLayoutEditor();
            IProject project = editor.getProject();
            ResourceType type = ResourceType.valueOf(resourceTypeName.toUpperCase());
            if (project != null) {
                // get the resource repository for this project and the system resources.
                IResourceRepository projectRepository = ResourceManager.getInstance()
                        .getProjectResources(project);
                Shell shell = AdtPlugin.getDisplay().getActiveShell();
                if (shell == null) {
                    return null;
                }

                AndroidTargetData data = editor.getTargetData();
                IResourceRepository systemRepository = data.getSystemResources();

                // open a resource chooser dialog for specified resource type.
                ResourceChooser dlg = new ResourceChooser(project, type, projectRepository,
                        systemRepository, shell);

                dlg.setCurrentResource(currentValue);

                if (dlg.open() == Window.OK) {
                    return dlg.getCurrentResource();
                }
            }

            return null;
        }

}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java
//Synthetic comment -- index fe62e26..947b4f0 100644

//Synthetic comment -- @@ -728,8 +728,8 @@
* <li>The project name, valid if it's an android nature.</li>
* <li>The current folder, valid if it's a folder under /res</li>
* <li>An existing filename, in which case the user will be asked whether to override it.</li>
     * </ul>
     * <p/>
* The selection can also be set to a {@link Pair} of {@link IProject} and a workspace
* resource path (where the resource path does not have to exist yet, such as res/anim/).
*








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LayoutTestBase.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LayoutTestBase.java
//Synthetic comment -- index 4cdbad0..21e4597 100644

//Synthetic comment -- @@ -234,6 +234,10 @@
return null;
}

        public String displayResourceInput(String resourceTypeName, String currentValue) {
            fail("Not supported in tests yet");
            return null;
        }
}

public void testDummy() {







