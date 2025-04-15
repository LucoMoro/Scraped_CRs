/*Fix botched refactoring

Change-Id:I02fcbfd6db7338c125a2e57ca39fc6e849d1a6b0*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ClientRulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ClientRulesEngine.java
//Synthetic comment -- index 9c73e1c..e08bfc1 100644

//Synthetic comment -- @@ -248,56 +248,9 @@

private String displayResourceInput(String resourceTypeName, String currentValue,
IInputValidator validator) {
ResourceType type = ResourceType.getEnum(resourceTypeName);
        GraphicalEditorPart graphicalEditor = mRulesEngine.getEditor();
        return ResourceChooser.chooseResource(graphicalEditor, type, currentValue, validator);
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java
//Synthetic comment -- index 202e1cf..7b90633 100644

//Synthetic comment -- @@ -28,11 +28,14 @@
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
import com.android.util.Pair;

//Synthetic comment -- @@ -790,6 +793,52 @@
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
}
}







