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
            public String validate(String text) {
if (mValidator == null) {
ResourceType type = ResourceType.getEnum(resourceTypeName);
if (uniqueInLayout) {
//Synthetic comment -- @@ -277,7 +277,7 @@
}

@Override
    public String displayReferenceInput(String currentValue) {
GraphicalEditorPart graphicalEditor = mRulesEngine.getEditor();
LayoutEditorDelegate delegate = graphicalEditor.getEditorDelegate();
IProject project = delegate.getEditor().getProject();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/BaseProjectHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/BaseProjectHelper.java
//Synthetic comment -- index 6266ea6..3be2f96 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.project;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.sdklib.SdkConstants;
//Synthetic comment -- @@ -393,7 +395,7 @@
* @param filter an optional filter to control which android project are returned. Can be null.
* @return an array of IJavaProject, which can be empty if no projects match.
*/
    public static IJavaProject[] getAndroidProjects(IProjectFilter filter) {
IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
IJavaModel javaModel = JavaCore.create(workspaceRoot);

//Synthetic comment -- @@ -408,7 +410,9 @@
* @param filter an optional filter to control which android project are returned. Can be null.
* @return an array of IJavaProject, which can be empty if no projects match.
*/
    public static IJavaProject[] getAndroidProjects(IJavaModel javaModel, IProjectFilter filter) {
// get the java projects
IJavaProject[] javaProjectList = null;
try {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java
//Synthetic comment -- index c23cedc..5e359a4 100644

//Synthetic comment -- @@ -41,6 +41,7 @@
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

//Synthetic comment -- @@ -214,6 +215,10 @@
@Override
public boolean performFinish() {
try {
IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
String name = mValues.projectName;
final IProject newProject = root.getProject(name);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplatePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplatePage.java
//Synthetic comment -- index 517a5c8..1d6e73a 100644

//Synthetic comment -- @@ -20,21 +20,41 @@
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.ATTR_NAME;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.PREVIEW_PADDING;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.PREVIEW_WIDTH;

import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.ImageControl;
import com.android.ide.eclipse.adt.internal.project.ProjectChooserHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectChooserHelper.ProjectCombo;
import com.android.tools.lint.detector.api.LintUtils;
import com.google.common.collect.Lists;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
//Synthetic comment -- @@ -54,7 +74,9 @@
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
//Synthetic comment -- @@ -66,6 +88,7 @@
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* First wizard page in the "New Project From Template" wizard (which is parameterized
//Synthetic comment -- @@ -210,116 +233,144 @@
continue;
}

                if (type == Parameter.Type.STRING) {
                    // TODO: Look at the constraints to add validators here
                    // TODO: If I type.equals("layout") add resource validator for layout
                    // names
                    // TODO: If I type.equals("class") make class validator

                    // TODO: Handle package and id better later
                    Label label = new Label(container, SWT.NONE);
                    label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
                    label.setText(name);

                    Text text = new Text(container, SWT.BORDER);
                    text.setData(parameter);
                    parameter.control = text;
                    text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

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
                } else if (type == Parameter.Type.BOOLEAN) {
                    Label label = new Label(container, SWT.NONE);
                    label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

                    Button checkBox = new Button(container, SWT.CHECK);
                    checkBox.setText(name);
                    checkBox.setData(parameter);
                    parameter.control = checkBox;
                    checkBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

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
                        ControlDecoration decoration = createFieldDecoration(id, checkBox, help);
                    }

                } else if (type == Parameter.Type.ENUM) {
                    Label label = new Label(container, SWT.NONE);
                    label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
                    label.setText(name);

                    Combo combo = new Combo(container, SWT.READ_ONLY);
                    combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

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
                } else {
                    assert false : type;
}
}
}
//Synthetic comment -- @@ -545,17 +596,102 @@
}
} else if (source instanceof Button) {
Button button = (Button) source;
            editParameter(button, button.getSelection());

            TemplateMetadata template = mValues.getTemplateHandler().getTemplate();
            if (template != null) {
                setPreview(template.getThumbnailPath());
}
}

validatePage();
}

private void editParameter(Control control, Object value) {
Parameter parameter = getParameter(control);
if (parameter != null) {
//Synthetic comment -- @@ -580,25 +716,7 @@
}
String updated = mEvaluator.evaluate(p.suggest, mParameters);
if (updated != null && !updated.equals(p.value)) {
                        p.value = updated;
                        mValues.parameters.put(p.id, updated);

                        // Update form widgets
                        boolean prevIgnore = mIgnore;
                        try {
                            mIgnore = true;
                            if (p.control instanceof Text) {
                                ((Text) p.control).setText(updated);
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
} catch (Throwable t) {
// Pass: Ignore updating if something wrong happens
//Synthetic comment -- @@ -608,6 +726,28 @@
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

/** The associated value should represent an API level */
APILEVEL,

//Synthetic comment -- @@ -275,10 +278,19 @@
mValidator = ResourceNameValidator.create(false, ResourceFolderType.DRAWABLE);
}
return mValidator;
            } else if (constraints.contains(Constraint.CLASS)) {
mValidator = new IInputValidator() {
@Override
public String isValid(String newText) {
IStatus status = ApplicationInfoPage.validateActivity(newText.trim());
if (status != null && !status.isOK()) {
return status.getMessage();







