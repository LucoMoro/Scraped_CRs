/*Mark the default activity in new projects as launchable

Change-Id:I04b8785ce88e1c65385b20793755cafab9458e4f*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java
//Synthetic comment -- index 54432bb..5c1ab96 100644

//Synthetic comment -- @@ -55,6 +55,7 @@
* Wizard for creating new projects
*/
public class NewProjectWizard extends TemplateWizard {
static final String ATTR_COPY_ICONS = "copyIcons";             //$NON-NLS-1$
static final String ATTR_TARGET_API = "targetApi";             //$NON-NLS-1$
static final String ATTR_MIN_API = "minApi";                   //$NON-NLS-1$
//Synthetic comment -- @@ -153,6 +154,7 @@
hidden.add(ATTR_MIN_API_LEVEL);
hidden.add(ATTR_TARGET_API);
hidden.add(ATTR_BUILD_API);

mTemplatePage = new NewTemplatePage(activityValues, false);
addPage(mTemplatePage);
//Synthetic comment -- @@ -351,6 +353,10 @@

addProjectInfo(parameters);

TemplateHandler activityTemplate = activityValues.getTemplateHandler();
activityTemplate.setBackupMergedFiles(false);
List<Change> changes = activityTemplate.render(project, parameters);







