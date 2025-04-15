/*ADT XML Wizard: auto-select single android project.

When opening the XML creation wizard, we try to auto-select
the project based on the selection. If the selection is empty
but there's only one accessible (i.e. open) Android project,
just auto-select it.

Change-Id:Ibdcabd7ed7e4f96602c5cb65c999db0645f9fad4*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java
//Synthetic comment -- index cb68ec2..f41a240 100644

//Synthetic comment -- @@ -24,7 +24,9 @@
import com.android.ide.eclipse.adt.internal.editors.descriptors.IDescriptorProvider;
import com.android.ide.eclipse.adt.internal.editors.menu.descriptors.MenuDescriptors;
import com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectChooserHelper;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper.IProjectFilter;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.resources.configurations.ResourceQualifier;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolderType;
//Synthetic comment -- @@ -745,6 +747,23 @@
}
}

        if (targetProject == null) {
            // If we didn't find a default project based on the selection, check how many
            // open Android projects we can find in the current workspace. If there's only
            // one, we'll just select it by default.

            IJavaProject[] projects = BaseProjectHelper.getAndroidProjects(new IProjectFilter() {
                public boolean accept(IProject project) {
                    return project.isAccessible();
                }
            });

            if (projects != null && projects.length == 1) {
                targetScore = 1;
                targetProject = projects[0].getProject();
            }
        }

// Now set the UI accordingly
if (targetScore > 0) {
mProject = targetProject;







