/*Refactor WorkingSet function.

- Refactor working set helper methods out of the main NPW class.
  They were not used by the main NPW class, just by the pages
  and were adding warnings due to *.internal.* class accesses.

- Respect the UI creation structure (one method for each top level
  composite).

- Fix: proper toggle the working set composite in the Test Project
  page based on the main "create test project" toggle.

- Hack around the fact the "Add working set" checkbox wasn't
  initialized properly in the Test Project page.

Change-Id:I5be81edcb45898b214b4e983a12e080d508b6c18*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreationPage.java
//Synthetic comment -- index 295c49e..027b94e 100644

//Synthetic comment -- @@ -31,8 +31,8 @@
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.internal.project.ProjectPropertiesWorkingCopy;
import com.android.sdklib.xml.AndroidManifest;
import com.android.sdklib.xml.ManifestData;
import com.android.sdklib.xml.ManifestData.Activity;
//Synthetic comment -- @@ -48,11 +48,7 @@
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerPart;
import org.eclipse.jdt.internal.ui.workingsets.IWorkingSetIDs;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.TextProcessor;
import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -68,7 +64,6 @@
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
//Synthetic comment -- @@ -82,8 +77,6 @@
import java.io.FileFilter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
//Synthetic comment -- @@ -185,7 +178,7 @@
}

public void init(IStructuredSelection selection, IWorkbenchPart activePart) {
        setWorkingSets(NewProjectWizard.getSelectedWorkingSet(selection, activePart));
}

// --- Getters used by NewProjectWizard ---
//Synthetic comment -- @@ -378,6 +371,7 @@
createLocationGroup(composite);
createTargetGroup(composite);
createPropertiesGroup(composite);

// Update state the first time
enableLocationWidgets();
//Synthetic comment -- @@ -397,9 +391,6 @@
setMessage(null);
setControl(scrolledComposite);

        Control workingSetControl = mWorkingSetGroup.createControl(composite);;
        workingSetControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

// Validate. This will complain about the first empty field.
validatePageComplete();
}
//Synthetic comment -- @@ -710,6 +701,11 @@
});
}


//--- Internal getters & setters ------------------









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectWizard.java
//Synthetic comment -- index d12830c..6e2a677 100644

//Synthetic comment -- @@ -51,20 +51,15 @@
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerPart;
import org.eclipse.jdt.internal.ui.workingsets.IWorkingSetIDs;
import org.eclipse.jdt.ui.actions.OpenJavaPerspectiveAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
//Synthetic comment -- @@ -77,13 +72,10 @@
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
* A "New Android Project" Wizard.
//Synthetic comment -- @@ -91,8 +83,8 @@
* Note: this class is public so that it can be accessed from unit tests.
* It is however an internal class. Its API may change without notice.
* It should semantically be considered as a private final class.
* Do not derive from this class.

*/
public class NewProjectWizard extends Wizard implements INewWizard {

//Synthetic comment -- @@ -1086,7 +1078,7 @@
* Adds the given folder to the project's class path.
*
* @param javaProject The Java Project to update.
     * @param sourceFolder Template Parameters.
* @param monitor An existing monitor.
* @throws JavaModelException if the classpath could not be set.
*/
//Synthetic comment -- @@ -1210,103 +1202,4 @@

return str;
}

    /*
     * Copied from org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne
     */

    private static final IWorkingSet[] EMPTY_WORKING_SET_ARRAY = new IWorkingSet[0];

    public static IWorkingSet[] getSelectedWorkingSet(IStructuredSelection selection,
            IWorkbenchPart activePart) {
        IWorkingSet[] selected= getSelectedWorkingSet(selection);
        if (selected != null && selected.length > 0) {
            for (int i= 0; i < selected.length; i++) {
                if (!isValidWorkingSet(selected[i]))
                    return EMPTY_WORKING_SET_ARRAY;
            }
            return selected;
        }

        if (!(activePart instanceof PackageExplorerPart))
            return EMPTY_WORKING_SET_ARRAY;

        PackageExplorerPart explorerPart= (PackageExplorerPart) activePart;
        if (explorerPart.getRootMode() == PackageExplorerPart.PROJECTS_AS_ROOTS) {
            //Get active filter
            IWorkingSet filterWorkingSet= explorerPart.getFilterWorkingSet();
            if (filterWorkingSet == null)
                return EMPTY_WORKING_SET_ARRAY;

            if (!isValidWorkingSet(filterWorkingSet))
                return EMPTY_WORKING_SET_ARRAY;

            return new IWorkingSet[] {filterWorkingSet};
        } else {
            //If we have been gone into a working set return the working set
            Object input= explorerPart.getViewPartInput();
            if (!(input instanceof IWorkingSet))
                return EMPTY_WORKING_SET_ARRAY;

            IWorkingSet workingSet= (IWorkingSet)input;
            if (!isValidWorkingSet(workingSet))
                return EMPTY_WORKING_SET_ARRAY;

            return new IWorkingSet[] {workingSet};
        }
    }

    public static IWorkingSet[] getSelectedWorkingSet(IStructuredSelection selection) {
        if (!(selection instanceof ITreeSelection))
            return EMPTY_WORKING_SET_ARRAY;

        ITreeSelection treeSelection= (ITreeSelection) selection;
        if (treeSelection.isEmpty())
            return EMPTY_WORKING_SET_ARRAY;

        List elements= treeSelection.toList();
        if (elements.size() == 1) {
            Object element= elements.get(0);
            TreePath[] paths= treeSelection.getPathsFor(element);
            if (paths.length != 1)
                return EMPTY_WORKING_SET_ARRAY;

            TreePath path= paths[0];
            if (path.getSegmentCount() == 0)
                return EMPTY_WORKING_SET_ARRAY;

            Object candidate= path.getSegment(0);
            if (!(candidate instanceof IWorkingSet))
                return EMPTY_WORKING_SET_ARRAY;

            IWorkingSet workingSetCandidate= (IWorkingSet) candidate;
            if (isValidWorkingSet(workingSetCandidate))
                return new IWorkingSet[] { workingSetCandidate };

            return EMPTY_WORKING_SET_ARRAY;
        }

        ArrayList result= new ArrayList();
        for (Iterator iterator= elements.iterator(); iterator.hasNext();) {
            Object element= iterator.next();
            if (element instanceof IWorkingSet && isValidWorkingSet((IWorkingSet) element)) {
                result.add(element);
            }
        }
        return (IWorkingSet[]) result.toArray(new IWorkingSet[result.size()]);
    }


    private static boolean isValidWorkingSet(IWorkingSet workingSet) {
        String id= workingSet.getId();
        if (!IWorkingSetIDs.JAVA.equals(id) && !IWorkingSetIDs.RESOURCE.equals(id))
            return false;

        if (workingSet.isAggregateWorkingSet())
            return false;

        return true;
    }


}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewTestProjectCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewTestProjectCreationPage.java
//Synthetic comment -- index d7c4048..dfdd72e 100755

//Synthetic comment -- @@ -89,6 +89,7 @@
* Note: this class is public so that it can be accessed from unit tests.
* It is however an internal class. Its API may change without notice.
* It should semantically be considered as a private final class.
* Do not derive from this class.
*/
public class NewTestProjectCreationPage extends WizardPage {
//Synthetic comment -- @@ -174,7 +175,7 @@
}

public void init(IStructuredSelection selection, IWorkbenchPart activePart) {
        setWorkingSets(NewProjectWizard.getSelectedWorkingSet(selection, activePart));
}

// --- Getters used by NewProjectWizard ---
//Synthetic comment -- @@ -308,6 +309,7 @@
createTestTargetGroup(composite);
createTargetGroup(composite);
createPropertiesGroup(composite);

// Update state the first time
enableLocationWidgets();
//Synthetic comment -- @@ -325,9 +327,6 @@
setMessage(null);
setControl(scrolledComposite);

        Control workingSetControl = mWorkingSetGroup.createControl(composite);
        workingSetControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

// Validate. This will complain about the first empty field.
validatePageComplete();
}
//Synthetic comment -- @@ -706,6 +705,12 @@
});
}


//--- Internal getters & setters ------------------

//Synthetic comment -- @@ -759,10 +764,8 @@
*/
private void useMainProjectInformation() {
if (mInfo.isTestingMain() && mMainInfo != null) {
            IWorkingSet[] workingSets = mMainInfo.getSelectedWorkingSets();
            if (workingSets != null && workingSets.length > 0) {
                mWorkingSetGroup.setWorkingSets(workingSets);
            }
String projName = String.format("%1$sTest", mMainInfo.getProjectName());
String appName = String.format("%1$sTest", mMainInfo.getApplicationName());

//Synthetic comment -- @@ -806,6 +809,17 @@
}
}

/**
* Callback invoked when the user edits the project text field.
*/








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/WorkingSetGroup.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/WorkingSetGroup.java
//Synthetic comment -- index 33ed218..8192361 100644

//Synthetic comment -- @@ -1,18 +1,13 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
*
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.internal.wizards.newproject;

//Synthetic comment -- @@ -21,6 +16,7 @@
import org.eclipse.jdt.internal.ui.workingsets.IWorkingSetIDs;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
//Synthetic comment -- @@ -34,9 +30,11 @@
* Creates the working set group with controls that allow
* the selection of working sets
*/
public class WorkingSetGroup {

private WorkingSetConfigurationBlock fWorkingSetBlock;

public WorkingSetGroup() {
String[] workingSetIds = new String[] {
//Synthetic comment -- @@ -46,7 +44,7 @@
.getDialogSettings());
}

    public Control createControl(Composite composite) {
Group workingSetGroup = new Group(composite, SWT.NONE);
workingSetGroup.setFont(composite.getFont());
workingSetGroup.setText(NewWizardMessages.NewJavaProjectWizardPageOne_WorkingSets_group);
//Synthetic comment -- @@ -54,6 +52,15 @@

fWorkingSetBlock.createContent(workingSetGroup);

return workingSetGroup;
}

//Synthetic comment -- @@ -64,4 +71,33 @@
public IWorkingSet[] getSelectedWorkingSets() {
return fWorkingSetBlock.getSelectedWorkingSets();
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/WorkingSetHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/WorkingSetHelper.java
new file mode 100755
//Synthetic comment -- index 0000000..428bfd3

//Synthetic comment -- @@ -0,0 +1,130 @@







