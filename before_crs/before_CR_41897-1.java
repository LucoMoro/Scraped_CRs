/*Don't ask about Hirarchical Parent Activity in new projects

This changeset hides the "Hierarchical Parent" setting from
the activity panel in the New Project Wizard.

It also adds "Optional" as a text field hint (where SWT supports
it) on this field when it's present (and on any other text fields
whose tempalte metadata states that they allow empty strings).

Change-Id:Id3334357b62fdaedcc8eb2ac08dac9a1f005efc6*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java
//Synthetic comment -- index d5526a7..84f4cf6 100644

//Synthetic comment -- @@ -55,6 +55,7 @@
* Wizard for creating new projects
*/
public class NewProjectWizard extends TemplateWizard {
private static final String IS_LAUNCHER = "isLauncher";        //$NON-NLS-1$
static final String ATTR_COPY_ICONS = "copyIcons";             //$NON-NLS-1$
static final String ATTR_TARGET_API = "targetApi";             //$NON-NLS-1$
//Synthetic comment -- @@ -155,6 +156,9 @@
hidden.add(ATTR_TARGET_API);
hidden.add(ATTR_BUILD_API);
hidden.add(IS_LAUNCHER);

mTemplatePage = new NewTemplatePage(activityValues, false);
addPage(mTemplatePage);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplatePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplatePage.java
//Synthetic comment -- index 36d4770..660b2bb 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectChooserHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectChooserHelper.ProjectCombo;
import com.android.ide.eclipse.adt.internal.wizards.templates.Parameter.Type;
import com.android.tools.lint.detector.api.LintUtils;
import com.google.common.collect.Lists;
//Synthetic comment -- @@ -277,7 +278,7 @@
text.setData(parameter);
parameter.control = text;

                        if (parameter.constraints.contains(Parameter.Constraint.EXISTS)) {
text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
1, 1));

//Synthetic comment -- @@ -290,11 +291,22 @@
2, 1));
}

if (value instanceof String) {
                            text.setText((String) value);
mValues.parameters.put(id, value);
}

text.addModifyListener(this);
text.addFocusListener(this);








