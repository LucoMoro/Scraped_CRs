/*Add working set to New Project Wizard

I have use the code from
org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne and adapt it.
Suppose it's acceptable.

The Android and Android Test can be added to the same or different
Working Set.
As in the Java Project wizard, it's possible add the project to
existing working set or create new working set.

Change-Id:Ib1be35a9221bbeaf448db29cfd53fb8f1bb37c28*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreationPage.java
//Synthetic comment -- index d17e3cf..295c49e 100644

//Synthetic comment -- @@ -31,8 +31,8 @@
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.internal.project.ProjectPropertiesWorkingCopy;
import com.android.sdklib.xml.AndroidManifest;
import com.android.sdklib.xml.ManifestData;
import com.android.sdklib.xml.ManifestData.Activity;
//Synthetic comment -- @@ -48,6 +48,11 @@
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
//Synthetic comment -- @@ -63,17 +68,22 @@
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkingSet;

import java.io.File;
import java.io.FileFilter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
//Synthetic comment -- @@ -159,7 +169,7 @@

private final ArrayList<String> mSamplesPaths = new ArrayList<String>();
private Combo mSamplesCombo;
    private WorkingSetGroup mWorkingSetGroup;


/**
//Synthetic comment -- @@ -170,6 +180,12 @@
setPageComplete(false);
setTitle("New Android Project");
setDescription("Creates a new Android Project resource.");
        mWorkingSetGroup = new WorkingSetGroup();
        setWorkingSets(new IWorkingSet[0]);
    }

    public void init(IStructuredSelection selection, IWorkbenchPart activePart) {
        setWorkingSets(NewProjectWizard.getSelectedWorkingSet(selection, activePart));
}

// --- Getters used by NewProjectWizard ---
//Synthetic comment -- @@ -212,6 +228,9 @@
public String getSourceFolder();
/** Returns the current sdk target or null if none has been selected yet. */
public IAndroidTarget getSdkTarget();
        /** Returns the current working sets or null if none has been selected yet. */
        public IWorkingSet[] getSelectedWorkingSets();

}


//Synthetic comment -- @@ -297,6 +316,11 @@
public IAndroidTarget getSdkTarget() {
return mSdkTargetSelector == null ? null : mSdkTargetSelector.getSelected();
}

        /** Returns the current sdk target or null if none has been selected yet. */
        public IWorkingSet[] getSelectedWorkingSets() {
            return getWorkingSets();
        }
}

/**
//Synthetic comment -- @@ -373,6 +397,9 @@
setMessage(null);
setControl(scrolledComposite);

        Control workingSetControl = mWorkingSetGroup.createControl(composite);;
        workingSetControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

// Validate. This will complain about the first empty field.
validatePageComplete();
}
//Synthetic comment -- @@ -1634,4 +1661,23 @@
return messageType;
}

    /**
     * Returns the working sets to which the new project should be added.
     *
     * @return the selected working sets to which the new project should be added
     */
    public IWorkingSet[] getWorkingSets() {
        return mWorkingSetGroup.getSelectedWorkingSets();
    }

    /**
     * Sets the working sets to which the new project should be added.
     *
     * @param workingSets the initial selected working sets
     */
    public void setWorkingSets(IWorkingSet[] workingSets) {
        assert workingSets != null;
        mWorkingSetGroup.setWorkingSets(workingSets);
    }

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectWizard.java
//Synthetic comment -- index 7c35ac8..d12830c 100644

//Synthetic comment -- @@ -51,14 +51,22 @@
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

import java.io.ByteArrayInputStream;
//Synthetic comment -- @@ -69,10 +77,13 @@
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
//Synthetic comment -- @@ -574,6 +585,21 @@
mainData.getDescription(),
mainData.getParameters(),
mainData.getDictionary());

                if (mainProject != null) {
                    final IJavaProject javaProject = JavaCore.create(mainProject);
                    Display.getDefault().syncExec(new Runnable() {

                        public void run() {
                            IWorkingSet[] workingSets = mMainPage.getWorkingSets();
                            if (workingSets.length > 0 && javaProject != null
                                    && javaProject.exists()) {
                                PlatformUI.getWorkbench().getWorkingSetManager()
                                        .addToWorkingSets(javaProject, workingSets);
                            }
                        }
                    });
                }
}

if (testData != null) {
//Synthetic comment -- @@ -583,14 +609,27 @@
parameters.put(PARAM_REFERENCE_PROJECT, mainProject);
}

                IProject testProject = createEclipseProject(
new SubProgressMonitor(monitor, 50),
testData.getProject(),
testData.getDescription(),
parameters,
testData.getDictionary());
                if (testProject != null) {
                    final IJavaProject javaProject = JavaCore.create(testProject);
                    Display.getDefault().syncExec(new Runnable() {

                        public void run() {
                            IWorkingSet[] workingSets = mTestPage.getWorkingSets();
                            if (workingSets.length > 0 && javaProject != null
                                    && javaProject.exists()) {
                                PlatformUI.getWorkbench().getWorkingSetManager()
                                        .addToWorkingSets(javaProject, workingSets);
                            }
                        }
                    });
                }
            }
} catch (CoreException e) {
throw new InvocationTargetException(e);
} catch (IOException e) {
//Synthetic comment -- @@ -1171,4 +1210,103 @@

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
//Synthetic comment -- index fe24553..da3a550 100755

//Synthetic comment -- @@ -45,6 +45,7 @@
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.TextProcessor;
import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -67,6 +68,8 @@
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkingSet;

import java.io.File;
import java.net.URI;
//Synthetic comment -- @@ -155,6 +158,7 @@
private Label mTestTargetPackageLabel;

private String mLastExistingPackageName;
    private WorkingSetGroup mWorkingSetGroup;


/**
//Synthetic comment -- @@ -165,6 +169,12 @@
setPageComplete(false);
setTitle("New Android Test Project");
setDescription("Creates a new Android Test Project resource.");
        mWorkingSetGroup= new WorkingSetGroup();
        setWorkingSets(new IWorkingSet[0]);
    }

    public void init(IStructuredSelection selection, IWorkbenchPart activePart) {
        setWorkingSets(NewProjectWizard.getSelectedWorkingSet(selection, activePart));
}

// --- Getters used by NewProjectWizard ---
//Synthetic comment -- @@ -315,6 +325,9 @@
setMessage(null);
setControl(scrolledComposite);

        Control workingSetControl = mWorkingSetGroup.createControl(composite);;
        workingSetControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

// Validate. This will complain about the first empty field.
validatePageComplete();
}
//Synthetic comment -- @@ -746,7 +759,10 @@
*/
private void useMainProjectInformation() {
if (mInfo.isTestingMain() && mMainInfo != null) {
            IWorkingSet[] workingSets = mMainInfo.getSelectedWorkingSets();
            if (workingSets != null && workingSets.length > 0) {
                mWorkingSetGroup.setWorkingSets(workingSets);
            }
String projName = String.format("%1$sTest", mMainInfo.getProjectName());
String appName = String.format("%1$sTest", mMainInfo.getApplicationName());

//Synthetic comment -- @@ -1342,4 +1358,24 @@
return messageType;
}

    /**
     * Returns the working sets to which the new project should be added.
     *
     * @return the selected working sets to which the new project should be added
     */
    public IWorkingSet[] getWorkingSets() {
        return mWorkingSetGroup.getSelectedWorkingSets();
    }

    /**
     * Sets the working sets to which the new project should be added.
     *
     * @param workingSets the initial selected working sets
     */
    public void setWorkingSets(IWorkingSet[] workingSets) {
        assert workingSets != null;
        mWorkingSetGroup.setWorkingSets(workingSets);
    }


}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/WorkingSetGroup.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/WorkingSetGroup.java
new file mode 100644
//Synthetic comment -- index 0000000..790d8d8

//Synthetic comment -- @@ -0,0 +1,64 @@
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

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.internal.ui.workingsets.IWorkingSetIDs;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.dialogs.WorkingSetConfigurationBlock;

/**
 * Copied from
 * org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne$WorkingSetGroup
 */
public class WorkingSetGroup {

    private WorkingSetConfigurationBlock fWorkingSetBlock;

    public WorkingSetGroup() {
        String[] workingSetIds = new String[] {
                IWorkingSetIDs.JAVA, IWorkingSetIDs.RESOURCE
        };
        fWorkingSetBlock = new WorkingSetConfigurationBlock(workingSetIds, JavaPlugin.getDefault()
                .getDialogSettings());
    }

    public Control createControl(Composite composite) {
        Group workingSetGroup = new Group(composite, SWT.NONE);
        workingSetGroup.setFont(composite.getFont());
        workingSetGroup.setText(NewWizardMessages.NewJavaProjectWizardPageOne_WorkingSets_group);
        workingSetGroup.setLayout(new GridLayout(1, false));

        fWorkingSetBlock.createContent(workingSetGroup);

        return workingSetGroup;
    }

    public void setWorkingSets(IWorkingSet[] workingSets) {
        fWorkingSetBlock.setWorkingSets(workingSets);
    }

    public IWorkingSet[] getSelectedWorkingSets() {
        return fWorkingSetBlock.getSelectedWorkingSets();
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/wizards/newproject/StubProjectCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/wizards/newproject/StubProjectCreationPage.java
//Synthetic comment -- index 0ed9ed4..f524d7f 100644

//Synthetic comment -- @@ -1,12 +1,12 @@
/*
* Copyright (C) 2008 The Android Open Source Project
 *
* Licensed under the Eclipse Public License, Version 1.0 (the "License"); you
* may not use this file except in compliance with the License. You may obtain a
* copy of the License at
 *
*      http://www.eclipse.org/org/documents/epl-v10.php
 *
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
//Synthetic comment -- @@ -20,6 +20,7 @@

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IWorkingSet;

/**
* Stub class for project creation page.
//Synthetic comment -- @@ -87,6 +88,10 @@
public boolean useDefaultLocation() {
return false;
}

            public IWorkingSet[] getSelectedWorkingSets() {
                return new IWorkingSet[0];
            }
};
}
}







