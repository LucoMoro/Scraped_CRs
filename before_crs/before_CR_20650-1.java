/*Fixinghttp://code.google.com/p/android/issues/detail?id=4523I have use the code from
org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne and adapt it.
Suppose it's acceptable.

The Android and Android Test can be added to the same or different
Working Set.
As in the Java Project wizard, it's possible add the project to
existing working set or create new working set.

Change-Id:Ib1be35a9221bbeaf448db29cfd53fb8f1bb37c28*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreationPage.java
//Synthetic comment -- index d17e3cf..f512860 100644

//Synthetic comment -- @@ -31,8 +31,8 @@
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectPropertiesWorkingCopy;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.xml.AndroidManifest;
import com.android.sdklib.xml.ManifestData;
import com.android.sdklib.xml.ManifestData.Activity;
//Synthetic comment -- @@ -48,6 +48,11 @@
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.TextProcessor;
import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -63,17 +68,22 @@
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import java.io.File;
import java.io.FileFilter;
import java.net.URI;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
//Synthetic comment -- @@ -159,7 +169,7 @@

private final ArrayList<String> mSamplesPaths = new ArrayList<String>();
private Combo mSamplesCombo;



/**
//Synthetic comment -- @@ -170,6 +180,12 @@
setPageComplete(false);
setTitle("New Android Project");
setDescription("Creates a new Android Project resource.");
}

// --- Getters used by NewProjectWizard ---
//Synthetic comment -- @@ -212,6 +228,9 @@
public String getSourceFolder();
/** Returns the current sdk target or null if none has been selected yet. */
public IAndroidTarget getSdkTarget();
}


//Synthetic comment -- @@ -297,6 +316,11 @@
public IAndroidTarget getSdkTarget() {
return mSdkTargetSelector == null ? null : mSdkTargetSelector.getSelected();
}
}

/**
//Synthetic comment -- @@ -373,6 +397,9 @@
setMessage(null);
setControl(scrolledComposite);

// Validate. This will complain about the first empty field.
validatePageComplete();
}
//Synthetic comment -- @@ -1634,4 +1661,25 @@
return messageType;
}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectWizard.java
//Synthetic comment -- index 9cb746d..9cb9f04 100644

//Synthetic comment -- @@ -51,14 +51,22 @@
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.actions.OpenJavaPerspectiveAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

import java.io.ByteArrayInputStream;
//Synthetic comment -- @@ -69,10 +77,13 @@
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
* A "New Android Project" Wizard.
//Synthetic comment -- @@ -574,6 +585,21 @@
mainData.getDescription(),
mainData.getParameters(),
mainData.getDictionary());
}

if (testData != null) {
//Synthetic comment -- @@ -583,14 +609,27 @@
parameters.put(PARAM_REFERENCE_PROJECT, mainProject);
}

                createEclipseProject(
new SubProgressMonitor(monitor, 50),
testData.getProject(),
testData.getDescription(),
parameters,
testData.getDictionary());
            }

} catch (CoreException e) {
throw new InvocationTargetException(e);
} catch (IOException e) {
//Synthetic comment -- @@ -1171,4 +1210,98 @@

return str;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewTestProjectCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewTestProjectCreationPage.java
//Synthetic comment -- index fe24553..7bc2c8d 100755

//Synthetic comment -- @@ -45,6 +45,7 @@
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.TextProcessor;
import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -67,6 +68,8 @@
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import java.io.File;
import java.net.URI;
//Synthetic comment -- @@ -155,6 +158,7 @@
private Label mTestTargetPackageLabel;

private String mLastExistingPackageName;


/**
//Synthetic comment -- @@ -165,6 +169,12 @@
setPageComplete(false);
setTitle("New Android Test Project");
setDescription("Creates a new Android Test Project resource.");
}

// --- Getters used by NewProjectWizard ---
//Synthetic comment -- @@ -315,6 +325,9 @@
setMessage(null);
setControl(scrolledComposite);

// Validate. This will complain about the first empty field.
validatePageComplete();
}
//Synthetic comment -- @@ -926,6 +939,12 @@

if (enabled) {
useMainProjectInformation();
}
validatePageComplete();
}
//Synthetic comment -- @@ -1342,4 +1361,26 @@
return messageType;
}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/WorkingSetGroup.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/WorkingSetGroup.java
new file mode 100644
//Synthetic comment -- index 0000000..c35a4c8

//Synthetic comment -- @@ -0,0 +1,65 @@







