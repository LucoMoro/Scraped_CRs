/*Make resource chooser include resources from library projects

The resource chooser was only showing resources from the current
project. This changeset fixes this such that it will merge in
resources from library projects as well.

It also cleans up the ResourceChooser class such that it has a couple
of convenience factory methods, and simplified various call sites to
use the factory instead.

Finally, it similarly fixes the Go To Declaration hyperlink resolver
to also look at library projects when resolving source locations.

Change-Id:Id20d14e4d9c6d9133b827ef3a3854e47c65b73fb*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/Hyperlinks.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/Hyperlinks.java
//Synthetic comment -- index 18135aa..148fa62 100644

//Synthetic comment -- @@ -61,6 +61,7 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.ide.eclipse.adt.io.IFolderWrapper;
//Synthetic comment -- @@ -1141,6 +1142,22 @@
}
List<ResourceFile> sourceFiles = resources.getSourceFiles(type, name,
null /*configuration*/);
        if (sourceFiles == null) {
            ProjectState projectState = Sdk.getProjectState(project);
            if (projectState != null) {
                List<IProject> libraries = projectState.getFullLibraryProjects();
                if (libraries != null && !libraries.isEmpty()) {
                    for (IProject library : libraries) {
                        resources = ResourceManager.getInstance().getProjectResources(library);
                        sourceFiles = resources.getSourceFiles(type, name, null /*configuration*/);
                        if (sourceFiles != null && !sourceFiles.isEmpty()) {
                            break;
                        }
                    }
                }
            }
        }

ResourceFile best = null;
if (configuration != null && sourceFiles != null && sourceFiles.size() > 0) {
List<ResourceFile> bestFiles = resources.getSourceFiles(type, name, configuration);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/FragmentMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/FragmentMenu.java
//Synthetic comment -- index 0dbd152..f7085fc 100644

//Synthetic comment -- @@ -16,28 +16,24 @@
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.SdkConstants.ANDROID_LAYOUT_RESOURCE_PREFIX;
import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.ATTR_CLASS;
import static com.android.SdkConstants.ATTR_NAME;
import static com.android.SdkConstants.LAYOUT_RESOURCE_PREFIX;
import static com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutMetadata.KEY_FRAGMENT_LAYOUT;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditorDelegate;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.resources.CyclicDependencyValidator;
import com.android.ide.eclipse.adt.internal.ui.ResourceChooser;
import com.android.resources.ResourceType;
import com.android.utils.Pair;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
//Synthetic comment -- @@ -46,10 +42,8 @@
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Menu;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

//Synthetic comment -- @@ -292,33 +286,12 @@
@Override
public void run() {
LayoutEditorDelegate delegate = mCanvas.getEditorDelegate();
            IFile file = delegate.getEditor().getInputFile();
            GraphicalEditorPart editor = delegate.getGraphicalEditor();
            ResourceChooser dlg = ResourceChooser.create(editor, ResourceType.LAYOUT)
                .setInputValidator(CyclicDependencyValidator.create(file))
                .setInitialSize(85, 10)
                .setCurrentResource(getSelectedLayout());
int result = dlg.open();
if (result == ResourceChooser.CLEAR_RETURN_CODE) {
setNewLayout(null);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ListViewTypeMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ListViewTypeMenu.java
//Synthetic comment -- index 076b11a..4577f8d 100644

//Synthetic comment -- @@ -23,24 +23,19 @@
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditorDelegate;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.resources.CyclicDependencyValidator;
import com.android.ide.eclipse.adt.internal.ui.ResourceChooser;
import com.android.resources.ResourceType;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Menu;
import org.w3c.dom.Node;

/**
//Synthetic comment -- @@ -171,33 +166,12 @@
@Override
public void run() {
LayoutEditorDelegate delegate = mCanvas.getEditorDelegate();
            IFile file = delegate.getEditor().getInputFile();
            GraphicalEditorPart editor = delegate.getGraphicalEditor();
            ResourceChooser dlg = ResourceChooser.create(editor, ResourceType.LAYOUT)
                .setInputValidator(CyclicDependencyValidator.create(file))
                .setInitialSize(85, 10)
                .setCurrentResource(getSelectedLayout());
int result = dlg.open();
if (result == ResourceChooser.CLEAR_RETURN_CODE) {
setNewType(mType, null);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java
//Synthetic comment -- index 1d87eb7..3c3ed54 100644

//Synthetic comment -- @@ -383,7 +383,10 @@
}

// Make sure we aren't removing the same nodes that are being added
                    // No, that can happen when canceling out of a drop handler such as
                    // when dropping an included layout, then canceling out of the
                    // resource chooser.
                    //assert !added.contains(child);
}
}
};








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiResourceAttributeNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiResourceAttributeNode.java
//Synthetic comment -- index bce3db4..43e9bee 100644

//Synthetic comment -- @@ -84,6 +84,13 @@
public class UiResourceAttributeNode extends UiTextAttributeNode {
private ResourceType mType;

    /**
     * Creates a new {@linkplain UiResourceAttributeNode}
     *
     * @param type the associated resource type
     * @param attributeDescriptor the attribute descriptor for this attribute
     * @param uiParent the parent ui node, if any
     */
public UiResourceAttributeNode(ResourceType type,
AttributeDescriptor attributeDescriptor, UiElementNode uiParent) {
super(attributeDescriptor, uiParent);
//Synthetic comment -- @@ -138,10 +145,15 @@
}

/**
     * Shows a dialog letting the user choose a set of enum, and returns a
     * string containing the result.
     *
     * @param shell the parent shell
     * @param currentValue an initial value, if any
     * @return the chosen string, or null
*/
    @Nullable
    public String showDialog(@NonNull Shell shell, @Nullable String currentValue) {
// we need to get the project of the file being edited.
UiElementNode uiNode = getUiParent();
AndroidXmlEditor editor = uiNode.getEditor();
//Synthetic comment -- @@ -154,17 +166,8 @@
if (mType != null) {
// get the Target Data to get the system resources
AndroidTargetData data = editor.getTargetData();
                ResourceChooser dlg = ResourceChooser.create(project, mType, data, shell)
                    .setCurrentResource(currentValue);
if (dlg.open() == Window.OK) {
return dlg.getCurrentResource();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/MarginChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/MarginChooser.java
//Synthetic comment -- index 066f4a1..c2a5f71 100644

//Synthetic comment -- @@ -15,14 +15,10 @@
*/
package com.android.ide.eclipse.adt.internal.ui;

import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.resources.ResourceType;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
//Synthetic comment -- @@ -219,19 +215,13 @@
// Button pressed - open resource chooser
if (event.widget instanceof Button) {
Button button = (Button) event.widget;
                Text text = (Text) button.getData(PROP_TEXTFIELD);

// Open a resource chooser dialog for specified resource type.
                ResourceChooser chooser = ResourceChooser.create(mEditor, ResourceType.DIMEN)
                        .setCurrentResource(text.getText().trim());
                if (chooser.open() == Window.OK) {
                    text.setText(chooser.getCurrentResource());
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java
//Synthetic comment -- index 1291af8..6796e91 100644

//Synthetic comment -- @@ -28,16 +28,19 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.assetstudio.OpenCreateAssetSetWizardAction;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringRefactoring;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringWizard;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;
import com.android.ide.eclipse.adt.internal.resources.ResourceNameValidator;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.resources.ResourceType;
import com.android.utils.Pair;
import com.google.common.collect.Maps;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
//Synthetic comment -- @@ -71,11 +74,13 @@
import org.eclipse.ui.dialogs.AbstractElementListSelectionDialog;
import org.eclipse.ui.dialogs.SelectionStatusDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -90,7 +95,7 @@

private Pattern mProjectResourcePattern;
private ResourceType mResourceType;
    private final List<ResourceRepository> mProjectResources;
private final ResourceRepository mFrameworkResources;
private Pattern mSystemResourcePattern;
private Button mProjectButton;
//Synthetic comment -- @@ -140,10 +145,12 @@
* @param frameworkResources The Framework resource repository
* @param parent the parent shell
*/
    private ResourceChooser(
            @NonNull IProject project,
            @NonNull ResourceType type,
            @NonNull List<ResourceRepository> projectResources,
            @Nullable ResourceRepository frameworkResources,
            @NonNull Shell parent) {
super(parent, new ResourceLabelProvider());
mProject = project;

//Synthetic comment -- @@ -163,14 +170,85 @@
}

/**
     * Creates a new {@link ResourceChooser}
     *
     * @param editor the associated layout editor
     * @param type the resource type to choose
     * @return a new {@link ResourceChooser}
     */
    @NonNull
    public static ResourceChooser create(
            @NonNull GraphicalEditorPart editor,
            @NonNull ResourceType type) {
        IProject project = editor.getProject();
        Shell parent = editor.getCanvasControl().getShell();
        AndroidTargetData targetData = editor.getEditorDelegate().getEditor().getTargetData();
        ResourceChooser chooser = create(project, type, targetData, parent);

        // When editing Strings, allow editing the value text directly. When we
        // get inline editing support (where values entered directly into the
        // textual widget are translated automatically into a resource) this can
        // go away.
        if (type == ResourceType.STRING) {
            chooser.setResourceResolver(editor.getResourceResolver());
            chooser.setShowValueText(true);
        } else if (type == ResourceType.DIMEN || type == ResourceType.INTEGER) {
            chooser.setResourceResolver(editor.getResourceResolver());
        }

        chooser.setPreviewHelper(new ResourcePreviewHelper(chooser, editor));
        return chooser;
    }

    /**
     * Creates a new {@link ResourceChooser}
     *
     * @param project the associated project
     * @param type the resource type to choose
     * @param targetData the associated framework target data
     * @param parent the target shell
     * @return a new {@link ResourceChooser}
     */
    @NonNull
    public static ResourceChooser create(
            @NonNull IProject project,
            @NonNull ResourceType type,
            @Nullable AndroidTargetData targetData,
            @NonNull Shell parent) {
        ResourceManager manager = ResourceManager.getInstance();

        List<ResourceRepository> projectResources = new ArrayList<ResourceRepository>();
        ProjectResources resources = manager.getProjectResources(project);
        projectResources.add(resources);

        // Add in library project resources
        ProjectState projectState = Sdk.getProjectState(project);
        if (projectState != null) {
            List<IProject> libraries = projectState.getFullLibraryProjects();
            if (libraries != null && !libraries.isEmpty()) {
                for (IProject library : libraries) {
                    projectResources.add(manager.getProjectResources(library));
                }
            }
        }

        ResourceRepository frameworkResources =
                targetData != null ? targetData.getFrameworkResources() : null;
        return new ResourceChooser(project, type, projectResources, frameworkResources, parent);
    }

    /**
* Sets whether this dialog should show the value field as a separate text
* value (and take the resulting value of the dialog from this text field
* rather than from the selection)
*
* @param showValueText if true, show the value text field
     * @return this, for constructor chaining
*/
    public ResourceChooser setShowValueText(boolean showValueText) {
mShowValueText = showValueText;

        return this;
}

/**
//Synthetic comment -- @@ -178,9 +256,12 @@
* selection
*
* @param resourceResolver the resource resolver to use
     * @return this, for constructor chaining
*/
    public ResourceChooser setResourceResolver(ResourceResolver resourceResolver) {
mResourceResolver = resourceResolver;

        return this;
}

/**
//Synthetic comment -- @@ -188,9 +269,25 @@
* resources, if any
*
* @param previewHelper the helper to use
     * @return this, for constructor chaining
*/
    public ResourceChooser setPreviewHelper(ResourcePreviewHelper previewHelper) {
mPreviewHelper = previewHelper;

        return this;
    }

    /**
     * Sets the initial dialog size
     *
     * @param width the initial width
     * @param height the initial height
     * @return this, for constructor chaining
     */
    public ResourceChooser setInitialSize(int width, int height) {
        setSize(width, height);

        return this;
}

@Override
//Synthetic comment -- @@ -220,20 +317,42 @@
}
}

    /**
     * Sets the currently selected item
     *
     * @param resource the resource url for the currently selected item
     * @return this, for constructor chaining
     */
    public ResourceChooser setCurrentResource(@Nullable String resource) {
mCurrentResource = resource;

if (mShowValueText && mEditValueText != null) {
mEditValueText.setText(resource);
}

        return this;
}

    /**
     * Returns the currently selected url
     *
     * @return the currently selected url
     */
    @Nullable
public String getCurrentResource() {
return mCurrentResource;
}

    /**
     * Sets the input validator to use, if any
     *
     * @param inputValidator the validator
     * @return this, for constructor chaining
     */
    public ResourceChooser setInputValidator(@Nullable IInputValidator inputValidator) {
mInputValidator = inputValidator;

        return this;
}

@Override
//Synthetic comment -- @@ -328,6 +447,9 @@
}
}
});
        if (mFrameworkResources == null) {
            mSystemButton.setVisible(false);
        }
}

/**
//Synthetic comment -- @@ -593,7 +715,19 @@
private ResourceItem[] setupResourceList() {
Collection<ResourceItem> items = null;
if (mProjectButton.getSelection()) {
            if (mProjectResources.size() == 1) {
                items = mProjectResources.get(0).getResourceItemsOfType(mResourceType);
            } else {
                Map<String, ResourceItem> merged = Maps.newHashMapWithExpectedSize(200);
                for (ResourceRepository repository : mProjectResources) {
                    for (ResourceItem item : repository.getResourceItemsOfType(mResourceType)) {
                        if (!merged.containsKey(item.getName())) {
                            merged.put(item.getName(), item);
                        }
                    }
                }
                items = merged.values();
            }
} else if (mSystemButton.getSelection()) {
items = mFrameworkResources.getResourceItemsOfType(mResourceType);
}
//Synthetic comment -- @@ -799,50 +933,18 @@
@NonNull GraphicalEditorPart graphicalEditor,
@NonNull ResourceType type,
String currentValue, IInputValidator validator) {
        ResourceChooser chooser = create(graphicalEditor, type).
                setCurrentResource(currentValue);
        if (validator != null) {
            // Ensure wide enough to accommodate validator error message
            chooser.setSize(85, 10);
            chooser.setInputValidator(validator);
        }
        int result = chooser.open();
        if (result == ResourceChooser.CLEAR_RETURN_CODE) {
            return ""; //$NON-NLS-1$
        } else if (result == Window.OK) {
            return chooser.getCurrentResource();
}

return null;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/HyperlinksTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/HyperlinksTest.java
//Synthetic comment -- index 7dcf661..4c81bc9 100644

//Synthetic comment -- @@ -44,7 +44,9 @@
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@SuppressWarnings({
        "restriction", "javadoc"
})
public class HyperlinksTest extends AdtProjectTest {
@Override
protected boolean testCaseNeedsUniqueProject() {







