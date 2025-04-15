/*Make resource chooser include resources from library projects

The resource chooser was only showing resources from the current
project. This changeset fixes this such that it will merge in
resources from library projects as well.

It also cleans up the ResourceChooser class such that it has a couple
of convenience factory methods, and simplified various call sites to
use the factory instead.

Change-Id:Id20d14e4d9c6d9133b827ef3a3854e47c65b73fb*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/FragmentMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/FragmentMenu.java
//Synthetic comment -- index 0dbd152..f7085fc 100644

//Synthetic comment -- @@ -16,28 +16,24 @@
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.SdkConstants.ANDROID_LAYOUT_RESOURCE_PREFIX;
import static com.android.SdkConstants.ATTR_CLASS;
import static com.android.SdkConstants.ATTR_NAME;
import static com.android.SdkConstants.LAYOUT_RESOURCE_PREFIX;
import static com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutMetadata.KEY_FRAGMENT_LAYOUT;


import com.android.SdkConstants;
import static com.android.SdkConstants.ANDROID_URI;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditorDelegate;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.resources.CyclicDependencyValidator;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.ui.ResourceChooser;
import com.android.resources.ResourceType;
import com.android.utils.Pair;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
//Synthetic comment -- @@ -46,10 +42,8 @@
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

//Synthetic comment -- @@ -292,33 +286,12 @@
@Override
public void run() {
LayoutEditorDelegate delegate = mCanvas.getEditorDelegate();
            IProject project = delegate.getEditor().getProject();
            // get the resource repository for this project and the system resources.
            ResourceRepository projectRepository = ResourceManager.getInstance()
                    .getProjectResources(project);
            Shell shell = mCanvas.getShell();

            AndroidTargetData data = delegate.getEditor().getTargetData();
            ResourceRepository systemRepository = data.getFrameworkResources();

            ResourceChooser dlg = new ResourceChooser(project,
                    ResourceType.LAYOUT, projectRepository,
                    systemRepository, shell);

            IInputValidator validator =
                CyclicDependencyValidator.create(delegate.getEditor().getInputFile());

            if (validator != null) {
                // Ensure wide enough to accommodate validator error message
                dlg.setSize(85, 10);
                dlg.setInputValidator(validator);
            }

            String layout = getSelectedLayout();
            if (layout != null) {
                dlg.setCurrentResource(layout);
            }

int result = dlg.open();
if (result == ResourceChooser.CLEAR_RETURN_CODE) {
setNewLayout(null);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ListViewTypeMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ListViewTypeMenu.java
//Synthetic comment -- index 076b11a..4577f8d 100644

//Synthetic comment -- @@ -23,24 +23,19 @@
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditorDelegate;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.resources.CyclicDependencyValidator;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.ui.ResourceChooser;
import com.android.resources.ResourceType;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Node;

/**
//Synthetic comment -- @@ -171,33 +166,12 @@
@Override
public void run() {
LayoutEditorDelegate delegate = mCanvas.getEditorDelegate();
            IProject project = delegate.getEditor().getProject();
            // get the resource repository for this project and the system resources.
            ResourceRepository projectRepository = ResourceManager.getInstance()
                    .getProjectResources(project);
            Shell shell = mCanvas.getShell();

            AndroidTargetData data = delegate.getEditor().getTargetData();
            ResourceRepository systemRepository = data.getFrameworkResources();

            ResourceChooser dlg = new ResourceChooser(project,
                    ResourceType.LAYOUT, projectRepository,
                    systemRepository, shell);

            IInputValidator validator =
                CyclicDependencyValidator.create(delegate.getEditor().getInputFile());

            if (validator != null) {
                // Ensure wide enough to accommodate validator error message
                dlg.setSize(85, 10);
                dlg.setInputValidator(validator);
            }

            String layout = getSelectedLayout();
            if (layout != null) {
                dlg.setCurrentResource(layout);
            }

int result = dlg.open();
if (result == ResourceChooser.CLEAR_RETURN_CODE) {
setNewType(mType, null);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java
//Synthetic comment -- index 1d87eb7..3c3ed54 100644

//Synthetic comment -- @@ -383,7 +383,10 @@
}

// Make sure we aren't removing the same nodes that are being added
                    assert !added.contains(child);
}
}
};








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiResourceAttributeNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiResourceAttributeNode.java
//Synthetic comment -- index bce3db4..43e9bee 100644

//Synthetic comment -- @@ -84,6 +84,13 @@
public class UiResourceAttributeNode extends UiTextAttributeNode {
private ResourceType mType;

public UiResourceAttributeNode(ResourceType type,
AttributeDescriptor attributeDescriptor, UiElementNode uiParent) {
super(attributeDescriptor, uiParent);
//Synthetic comment -- @@ -138,10 +145,15 @@
}

/**
     * Shows a dialog letting the user choose a set of enum, and returns a string
     * containing the result.
*/
    public String showDialog(Shell shell, String currentValue) {
// we need to get the project of the file being edited.
UiElementNode uiNode = getUiParent();
AndroidXmlEditor editor = uiNode.getEditor();
//Synthetic comment -- @@ -154,17 +166,8 @@
if (mType != null) {
// get the Target Data to get the system resources
AndroidTargetData data = editor.getTargetData();
                ResourceRepository frameworkRepository = data.getFrameworkResources();

                // open a resource chooser dialog for specified resource type.
                ResourceChooser dlg = new ResourceChooser(project,
                        mType,
                        projectRepository,
                        frameworkRepository,
                        shell);

                dlg.setCurrentResource(currentValue);

if (dlg.open() == Window.OK) {
return dlg.getCurrentResource();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/MarginChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/MarginChooser.java
//Synthetic comment -- index 066f4a1..c2a5f71 100644

//Synthetic comment -- @@ -15,14 +15,10 @@
*/
package com.android.ide.eclipse.adt.internal.ui;

import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.resources.ResourceType;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
//Synthetic comment -- @@ -219,19 +215,13 @@
// Button pressed - open resource chooser
if (event.widget instanceof Button) {
Button button = (Button) event.widget;

// Open a resource chooser dialog for specified resource type.
                IProject project = mEditor.getProject();
                ProjectResources projectRepository = ResourceManager.getInstance()
                        .getProjectResources(project);
                ResourceRepository frameworkRepository = mTargetData.getFrameworkResources();
                ResourceChooser dlg = new ResourceChooser(project, ResourceType.DIMEN,
                        projectRepository, frameworkRepository, getShell());
                dlg.setResourceResolver(mEditor.getResourceResolver());
                Text text = (Text) button.getData(PROP_TEXTFIELD);
                dlg.setCurrentResource(text.getText().trim());
                if (dlg.open() == Window.OK) {
                    text.setText(dlg.getCurrentResource());
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java
//Synthetic comment -- index 1291af8..6796e91 100644

//Synthetic comment -- @@ -28,16 +28,19 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.assetstudio.OpenCreateAssetSetWizardAction;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringRefactoring;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringWizard;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;
import com.android.ide.eclipse.adt.internal.resources.ResourceNameValidator;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.resources.ResourceType;
import com.android.utils.Pair;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
//Synthetic comment -- @@ -71,11 +74,13 @@
import org.eclipse.ui.dialogs.AbstractElementListSelectionDialog;
import org.eclipse.ui.dialogs.SelectionStatusDialog;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -90,7 +95,7 @@

private Pattern mProjectResourcePattern;
private ResourceType mResourceType;
    private final ResourceRepository mProjectResources;
private final ResourceRepository mFrameworkResources;
private Pattern mSystemResourcePattern;
private Button mProjectButton;
//Synthetic comment -- @@ -140,10 +145,12 @@
* @param frameworkResources The Framework resource repository
* @param parent the parent shell
*/
    public ResourceChooser(IProject project, ResourceType type,
            ResourceRepository projectResources,
            ResourceRepository frameworkResources,
            Shell parent) {
super(parent, new ResourceLabelProvider());
mProject = project;

//Synthetic comment -- @@ -163,14 +170,85 @@
}

/**
* Sets whether this dialog should show the value field as a separate text
* value (and take the resulting value of the dialog from this text field
* rather than from the selection)
*
* @param showValueText if true, show the value text field
*/
    public void setShowValueText(boolean showValueText) {
mShowValueText = showValueText;
}

/**
//Synthetic comment -- @@ -178,9 +256,12 @@
* selection
*
* @param resourceResolver the resource resolver to use
*/
    public void setResourceResolver(ResourceResolver resourceResolver) {
mResourceResolver = resourceResolver;
}

/**
//Synthetic comment -- @@ -188,9 +269,25 @@
* resources, if any
*
* @param previewHelper the helper to use
*/
    public void setPreviewHelper(ResourcePreviewHelper previewHelper) {
mPreviewHelper = previewHelper;
}

@Override
//Synthetic comment -- @@ -220,20 +317,42 @@
}
}

    public void setCurrentResource(String resource) {
mCurrentResource = resource;

if (mShowValueText && mEditValueText != null) {
mEditValueText.setText(resource);
}
}

public String getCurrentResource() {
return mCurrentResource;
}

    public void setInputValidator(IInputValidator inputValidator) {
mInputValidator = inputValidator;
}

@Override
//Synthetic comment -- @@ -328,6 +447,9 @@
}
}
});
}

/**
//Synthetic comment -- @@ -593,7 +715,19 @@
private ResourceItem[] setupResourceList() {
Collection<ResourceItem> items = null;
if (mProjectButton.getSelection()) {
            items = mProjectResources.getResourceItemsOfType(mResourceType);
} else if (mSystemButton.getSelection()) {
items = mFrameworkResources.getResourceItemsOfType(mResourceType);
}
//Synthetic comment -- @@ -799,50 +933,18 @@
@NonNull GraphicalEditorPart graphicalEditor,
@NonNull ResourceType type,
String currentValue, IInputValidator validator) {
        AndroidXmlEditor editor = graphicalEditor.getEditorDelegate().getEditor();
        IProject project = editor.getProject();
        if (project != null) {
            // get the resource repository for this project and the system resources.
            ResourceRepository projectRepository = ResourceManager.getInstance()
                    .getProjectResources(project);
            Shell shell = AdtPlugin.getDisplay().getActiveShell();
            if (shell == null) {
                return null;
            }

            AndroidTargetData data = editor.getTargetData();
            ResourceRepository systemRepository = data.getFrameworkResources();

            // open a resource chooser dialog for specified resource type.
            ResourceChooser dlg = new ResourceChooser(project, type, projectRepository,
                    systemRepository, shell);
            dlg.setPreviewHelper(new ResourcePreviewHelper(dlg, graphicalEditor));

            // When editing Strings, allow editing the value text directly. When we
            // get inline editing support (where values entered directly into the
            // textual widget are translated automatically into a resource) this can
            // go away.
            if (type == ResourceType.STRING) {
                dlg.setResourceResolver(graphicalEditor.getResourceResolver());
                dlg.setShowValueText(true);
            } else if (type == ResourceType.DIMEN || type == ResourceType.INTEGER) {
                dlg.setResourceResolver(graphicalEditor.getResourceResolver());
            }

            if (validator != null) {
                // Ensure wide enough to accommodate validator error message
                dlg.setSize(85, 10);
                dlg.setInputValidator(validator);
            }

            dlg.setCurrentResource(currentValue);

            int result = dlg.open();
            if (result == ResourceChooser.CLEAR_RETURN_CODE) {
                return ""; //$NON-NLS-1$
            } else if (result == Window.OK) {
                return dlg.getCurrentResource();
            }
}

return null;







