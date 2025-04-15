/*Support activity parameter types

If one of the template parameters is of type "activity", and it also
has the constraint "exists", the associated wizard controls will show
a chooser button (...), which invokes the Eclipse Type selector
showing the available class names.

Change-Id:Ifc0f5226dc2c737a59391fa1d2b48636683dc9d5*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ClientRulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ClientRulesEngine.java
//Synthetic comment -- index 83a4e42..8337007 100644

//Synthetic comment -- @@ -226,7 +226,7 @@
private ResourceNameValidator mValidator;

@Override
            public String validate(@NonNull String text) {
if (mValidator == null) {
ResourceType type = ResourceType.getEnum(resourceTypeName);
if (uniqueInLayout) {
//Synthetic comment -- @@ -277,7 +277,7 @@
}

@Override
    public String displayReferenceInput(@Nullable String currentValue) {
GraphicalEditorPart graphicalEditor = mRulesEngine.getEditor();
LayoutEditorDelegate delegate = graphicalEditor.getEditorDelegate();
IProject project = delegate.getEditor().getProject();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/BaseProjectHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/BaseProjectHelper.java
//Synthetic comment -- index 6266ea6..3be2f96 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.project;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.sdklib.SdkConstants;
//Synthetic comment -- @@ -393,7 +395,7 @@
* @param filter an optional filter to control which android project are returned. Can be null.
* @return an array of IJavaProject, which can be empty if no projects match.
*/
    public static @NonNull IJavaProject[] getAndroidProjects(@Nullable IProjectFilter filter) {
IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
IJavaModel javaModel = JavaCore.create(workspaceRoot);

//Synthetic comment -- @@ -408,7 +410,9 @@
* @param filter an optional filter to control which android project are returned. Can be null.
* @return an array of IJavaProject, which can be empty if no projects match.
*/
    @NonNull
    public static IJavaProject[] getAndroidProjects(@NonNull IJavaModel javaModel,
            @Nullable IProjectFilter filter) {
// get the java projects
IJavaProject[] javaProjectList = null;
try {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java
//Synthetic comment -- index c23cedc..5e359a4 100644

//Synthetic comment -- @@ -41,6 +41,7 @@
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

//Synthetic comment -- @@ -214,6 +215,10 @@
@Override
public boolean performFinish() {
try {
            Shell shell = getShell();
            if (shell != null) {
                shell.setVisible(false);
            }
IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
String name = mValues.projectName;
final IProject newProject = root.getProject(name);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplatePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplatePage.java
//Synthetic comment -- index 517a5c8..1d6e73a 100644

//Synthetic comment -- @@ -20,21 +20,41 @@
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.ATTR_NAME;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.PREVIEW_PADDING;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.PREVIEW_WIDTH;
import static com.android.sdklib.SdkConstants.CLASS_ACTIVITY;

import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.ImageControl;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectChooserHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectChooserHelper.ProjectCombo;
import com.android.ide.eclipse.adt.internal.wizards.templates.Parameter.Type;
import com.android.tools.lint.detector.api.LintUtils;
import com.google.common.collect.Lists;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.dialogs.ITypeInfoFilterExtension;
import org.eclipse.jdt.ui.dialogs.ITypeInfoRequestor;
import org.eclipse.jdt.ui.dialogs.TypeSelectionExtension;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
//Synthetic comment -- @@ -54,7 +74,9 @@
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
//Synthetic comment -- @@ -66,6 +88,7 @@
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
* First wizard page in the "New Project From Template" wizard (which is parameterized
//Synthetic comment -- @@ -210,116 +233,144 @@
continue;
}

                switch (type) {
                    case STRING: {
                        // TODO: Look at the constraints to add validators here
                        // TODO: If I type.equals("layout") add resource validator for layout
                        // names
                        // TODO: If I type.equals("class") make class validator

                        // TODO: Handle package and id better later
                        Label label = new Label(container, SWT.NONE);
                        label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
                                1, 1));
                        label.setText(name);

                        Text text = new Text(container, SWT.BORDER);
                        text.setData(parameter);
                        parameter.control = text;

                        if (parameter.constraints.contains(Parameter.Constraint.EXISTS)) {
                            text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
                                    1, 1));

                            Button button = new Button(container, SWT.FLAT);
                            button.setData(parameter);
                            button.setText("...");
                            button.addSelectionListener(this);
                        } else {
                            text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
                                    2, 1));
}

                        if (value != null && !value.isEmpty()){
                            text.setText(value);
                            mValues.parameters.put(id, value);
                        }

                        text.addModifyListener(this);
                        text.addFocusListener(this);

                        if (mFirst == null) {
                            mFirst = text;
                        }

                        if (help != null && !help.isEmpty()) {
                            text.setToolTipText(help);
                            ControlDecoration decoration = createFieldDecoration(id, text, help);
                        }
                        break;
}
                    case BOOLEAN: {
                        Label label = new Label(container, SWT.NONE);
                        label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
                                1, 1));

                        Button checkBox = new Button(container, SWT.CHECK);
                        checkBox.setText(name);
                        checkBox.setData(parameter);
                        parameter.control = checkBox;
                        checkBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
                                2, 1));

                        if (value != null && !value.isEmpty()){
                            Boolean selected = Boolean.valueOf(value);
                            checkBox.setSelection(selected);
                            mValues.parameters.put(id, value);
                        }

                        checkBox.addSelectionListener(this);
                        checkBox.addFocusListener(this);

                        if (mFirst == null) {
                            mFirst = checkBox;
                        }

                        if (help != null && !help.isEmpty()) {
                            checkBox.setToolTipText(help);
                            ControlDecoration decoration = createFieldDecoration(id, checkBox,
                                    help);
                        }
                        break;
                    }
                    case ENUM: {
                        Label label = new Label(container, SWT.NONE);
                        label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
                                1, 1));
                        label.setText(name);

                        Combo combo = new Combo(container, SWT.READ_ONLY);
                        combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
                                2, 1));

                        List<Element> options = DomUtilities.getChildren(parameter.element);
                        assert options.size() > 0;
                        int selected = 0;
                        List<String> ids = Lists.newArrayList();
                        List<String> labels = Lists.newArrayList();
                        for (int i = 0, n = options.size(); i < n; i++) {
                            Element option = options.get(i);
                            String optionId = option.getAttribute(ATTR_ID);
                            assert optionId != null && !optionId.isEmpty() : ATTR_ID;
                            String isDefault = option.getAttribute(ATTR_DEFAULT);
                            if (isDefault != null && !isDefault.isEmpty() &&
                                    Boolean.valueOf(isDefault)) {
                                selected = i;
                            }
                            NodeList childNodes = option.getChildNodes();
                            assert childNodes.getLength() == 1 &&
                                    childNodes.item(0).getNodeType() == Node.TEXT_NODE;
                            String optionLabel = childNodes.item(0).getNodeValue().trim();
                            ids.add(optionId);
                            labels.add(optionLabel);
                        }
                        combo.setData(parameter);
                        parameter.control = combo;
                        combo.setData(ATTR_ID, ids.toArray(new String[ids.size()]));
                        assert labels.size() > 0;
                        combo.setItems(labels.toArray(new String[labels.size()]));
                        combo.select(selected);
                        mValues.parameters.put(id, ids.get(selected));

                        combo.addSelectionListener(this);
                        combo.addFocusListener(this);

                        if (mFirst == null) {
                            mFirst = combo;
                        }

                        if (help != null && !help.isEmpty()) {
                            combo.setToolTipText(help);
                            ControlDecoration decoration = createFieldDecoration(id, combo, help);
                        }
                        break;
                    }
                    case SEPARATOR:
                        // Already handled above
                        assert false : type;
                        break;
                    default:
                        assert false : type;
}
}
}
//Synthetic comment -- @@ -545,17 +596,102 @@
}
} else if (source instanceof Button) {
Button button = (Button) source;
            Parameter parameter = (Parameter) button.getData();
            if (parameter.type == Type.BOOLEAN) {
                // Checkbox parameter
                editParameter(button, button.getSelection());

                TemplateMetadata template = mValues.getTemplateHandler().getTemplate();
                if (template != null) {
                    setPreview(template.getThumbnailPath());
                }
            } else {
                // Choose button for some other parameter, usually a text
                String activity = chooseActivity();
                if (activity != null) {
                    setValue(parameter, activity);
                }
}
}

validatePage();
}

    private String chooseActivity() {
        try {
            // Compute a search scope: We need to merge all the subclasses
            // android.app.Fragment and android.support.v4.app.Fragment
            IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
            IProject project = mValues.project;
            IJavaProject javaProject = BaseProjectHelper.getJavaProject(project);
            IType activityType = null;

            if (javaProject != null) {
                activityType = javaProject.findType(CLASS_ACTIVITY);
            }
            if (activityType == null) {
                IJavaProject[] projects = BaseProjectHelper.getAndroidProjects(null);
                for (IJavaProject p : projects) {
                    activityType = p.findType(CLASS_ACTIVITY);
                    if (activityType != null) {
                        break;
                    }
                }
            }
            if (activityType != null) {
                NullProgressMonitor monitor = new NullProgressMonitor();
                ITypeHierarchy hierarchy = activityType.newTypeHierarchy(monitor);
                IType[] classes = hierarchy.getAllSubtypes(activityType);
                scope = SearchEngine.createJavaSearchScope(classes, IJavaSearchScope.SOURCES);
            }

            Shell parent = AdtPlugin.getDisplay().getActiveShell();
            final AtomicReference<SelectionDialog> dialogHolder =
                new AtomicReference<SelectionDialog>();
            final SelectionDialog dialog = JavaUI.createTypeDialog(
                    parent,
                    new ProgressMonitorDialog(parent),
                    scope,
                    IJavaElementSearchConstants.CONSIDER_CLASSES, false,
                    // Use ? as a default filter to fill dialog with matches
                    "?", //$NON-NLS-1$
                    new TypeSelectionExtension() {
                        @Override
                        public ITypeInfoFilterExtension getFilterExtension() {
                            return new ITypeInfoFilterExtension() {
                                @Override
                                public boolean select(ITypeInfoRequestor typeInfoRequestor) {
                                    int modifiers = typeInfoRequestor.getModifiers();
                                    if (!Flags.isPublic(modifiers)
                                            || Flags.isInterface(modifiers)
                                            || Flags.isEnum(modifiers)) {
                                        return false;
                                    }
                                    return true;
                                }
                            };
                        }
                    });
            dialogHolder.set(dialog);

            dialog.setTitle("Choose Activity Class");
            dialog.setMessage("Select an Activity class (? = any character, * = any string):");
            if (dialog.open() == IDialogConstants.CANCEL_ID) {
                return null;
            }

            Object[] types = dialog.getResult();
            if (types != null && types.length > 0) {
                return ((IType) types[0]).getFullyQualifiedName();
            }
        } catch (JavaModelException e) {
            AdtPlugin.log(e, null);
        } catch (CoreException e) {
            AdtPlugin.log(e, null);
        }
        return null;
    }

private void editParameter(Control control, Object value) {
Parameter parameter = getParameter(control);
if (parameter != null) {
//Synthetic comment -- @@ -580,25 +716,7 @@
}
String updated = mEvaluator.evaluate(p.suggest, mParameters);
if (updated != null && !updated.equals(p.value)) {
                        setValue(p, updated);
}
} catch (Throwable t) {
// Pass: Ignore updating if something wrong happens
//Synthetic comment -- @@ -608,6 +726,28 @@
}
}

    private void setValue(Parameter p, String value) {
        p.value = value;
        mValues.parameters.put(p.id, value);

        // Update form widgets
        boolean prevIgnore = mIgnore;
        try {
            mIgnore = true;
            if (p.control instanceof Text) {
                ((Text) p.control).setText(value);
            } else if (p.control instanceof Button) {
                // TODO: Handle
            } else if (p.control instanceof Combo) {
                // TODO: Handle
            } else if (p.control != null) {
                assert false : p.control;
            }
        } finally {
            mIgnore = prevIgnore;
        }
    }

@Override
public void widgetDefaultSelected(SelectionEvent e) {
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/Parameter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/Parameter.java
//Synthetic comment -- index 0b8b952..7436a26 100644

//Synthetic comment -- @@ -97,6 +97,9 @@
/** The associated value must not be empty */
NONEMPTY,

        /** The associated value should represent a fully qualified activity class name */
        ACTIVITY,

/** The associated value should represent an API level */
APILEVEL,

//Synthetic comment -- @@ -275,10 +278,19 @@
mValidator = ResourceNameValidator.create(false, ResourceFolderType.DRAWABLE);
}
return mValidator;
            } else if (constraints.contains(Constraint.CLASS)
                    || constraints.contains(Constraint.ACTIVITY)) {
mValidator = new IInputValidator() {
@Override
public String isValid(String newText) {
                        // Deliberately allow empty strings for parameters that aren't required
                        if (newText.trim().isEmpty()
                                && !constraints.contains(Constraint.NONEMPTY)) {
                            return null;
                        }

                        // TODO: Don't use *activity* validator if it doesn't have to be one
                        // (e.g. is CLASS, not ACTIVITY)
IStatus status = ApplicationInfoPage.validateActivity(newText.trim());
if (status != null && !status.isOK()) {
return status.getMessage();







