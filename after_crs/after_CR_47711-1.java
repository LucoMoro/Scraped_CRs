/*Filter out non-launcher activity from new project activity list

Also show template title rather than template directory name
in the template list (issue 38955)

Change-Id:Iedd3949aa5c7fce879a4d7fd11e4fa73660d6451*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/ActivityPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/ActivityPage.java
//Synthetic comment -- index 9d2dd9a..ba4aedc 100644

//Synthetic comment -- @@ -17,11 +17,13 @@

import static com.android.ide.eclipse.adt.internal.wizards.templates.NewProjectWizard.CATEGORY_ACTIVITIES;
import static com.android.ide.eclipse.adt.internal.wizards.templates.NewProjectWizard.CATEGORY_OTHER;
import static com.android.ide.eclipse.adt.internal.wizards.templates.NewProjectWizard.IS_LAUNCHER;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.PREVIEW_PADDING;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.PREVIEW_WIDTH;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.ImageControl;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

import org.eclipse.core.runtime.IStatus;
//Synthetic comment -- @@ -61,6 +63,7 @@
private Label mDescription;
private boolean mOnlyActivities;
private boolean mAskCreate;
    private boolean mLauncherActivitiesOnly;

/**
* Create the wizard.
//Synthetic comment -- @@ -84,6 +87,11 @@
}
}

    /** Sets whether the activity page should only offer launcher activities */
    void setLauncherActivitiesOnly(boolean launcherActivitiesOnly) {
        mLauncherActivitiesOnly = launcherActivitiesOnly;
    }

@Override
public void createControl(Composite parent) {
Composite container = new Composite(parent, SWT.NULL);
//Synthetic comment -- @@ -108,18 +116,31 @@


TemplateManager manager = mValues.template.getManager();
        java.util.List<File> templates = manager.getTemplates(CATEGORY_ACTIVITIES);

if (!mOnlyActivities) {
            templates.addAll(manager.getTemplates(CATEGORY_OTHER));
}
        java.util.List<String> names = new ArrayList<String>(templates.size());
File current = mValues.activityValues.getTemplateLocation();
        mTemplates = Lists.newArrayListWithExpectedSize(templates.size());
int index = -1;
        for (int i = 0, n = templates.size(); i < n; i++) {
            File template = templates.get(i);
            TemplateMetadata metadata = manager.getTemplate(template);
            if (metadata == null) {
                continue;
            }
            if (mLauncherActivitiesOnly) {
                Parameter parameter = metadata.getParameter(IS_LAUNCHER);
                if (parameter == null) {
                    continue;
                }
            }
            mTemplates.add(template);
            names.add(metadata.getTitle());
if (template.equals(current)) {
                index = names.size();
}
}
String[] items = names.toArray(new String[names.size()]);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java
//Synthetic comment -- index fdb26b1..84de9a4 100644

//Synthetic comment -- @@ -59,7 +59,7 @@
public class NewProjectWizard extends TemplateWizard {
private static final String PARENT_ACTIVITY_CLASS = "parentActivityClass";  //$NON-NLS-1$
private static final String ACTIVITY_TITLE = "activityTitle";  //$NON-NLS-1$
    static final String IS_LAUNCHER = "isLauncher";                //$NON-NLS-1$
static final String IS_NEW_PROJECT = "isNewProject";           //$NON-NLS-1$
static final String IS_LIBRARY_PROJECT = "isLibraryProject";   //$NON-NLS-1$
static final String ATTR_COPY_ICONS = "copyIcons";             //$NON-NLS-1$
//Synthetic comment -- @@ -100,6 +100,7 @@
mContentsPage = new ProjectContentsPage(mValues);
mContentsPage.init(selection, AdtUtils.getActivePart());
mActivityPage = new ActivityPage(mValues, true, true);
        mActivityPage.setLauncherActivitiesOnly(true);
}

@Override







