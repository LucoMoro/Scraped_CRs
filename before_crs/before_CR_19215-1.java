/*ADT string refactoring: UI options to replace in files.

Change-Id:Ifeef5fd444c2c18b9c071955b8e8567d6515ea95*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringInputPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringInputPage.java
//Synthetic comment -- index cbcd581..b05318c 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.ide.eclipse.adt.internal.ui.ConfigurationSelector.SelectorMode;
import com.android.sdklib.SdkConstants;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
//Synthetic comment -- @@ -38,6 +39,7 @@
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
//Synthetic comment -- @@ -70,6 +72,10 @@
private ConfigurationSelector mConfigSelector;
/** The combo to display the existing XML files or enter a new one. */
private Combo mResFileCombo;

/** Regex pattern to read a valid res XML file path. It checks that the are 2 folders and
*  a leaf file name ending with .xml */
//Synthetic comment -- @@ -86,6 +92,8 @@

private XmlStringFileHelper mXmlHelper = new XmlStringFileHelper();


public ExtractStringInputPage(IProject project) {
super("ExtractStringInputPage");  //$NON-NLS-1$
//Synthetic comment -- @@ -97,17 +105,21 @@
* <p/>
* Note that at that point the initial conditions have been checked in
* {@link ExtractStringRefactoring}.
*/
public void createControl(Composite parent) {
Composite content = new Composite(parent, SWT.NONE);
GridLayout layout = new GridLayout();
        layout.numColumns = 1;
content.setLayout(layout);

createStringGroup(content);
createResFileGroup(content);

        validatePage();
setControl(content);
}

//Synthetic comment -- @@ -123,10 +135,9 @@

Group group = new Group(content, SWT.NONE);
group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
if (ref.getMode() == ExtractStringRefactoring.Mode.EDIT_SOURCE) {
group.setText("String Replacement");
        } else {
            group.setText("New String");
}

GridLayout layout = new GridLayout();
//Synthetic comment -- @@ -152,19 +163,14 @@
}
});


        // TODO provide an option to replace all occurences of this string instead of
        // just the one.

// line : Textfield for new ID

label = new Label(group, SWT.NONE);
if (ref.getMode() == ExtractStringRefactoring.Mode.EDIT_SOURCE) {
label.setText("&Replace by R.string.");
} else if (ref.getMode() == ExtractStringRefactoring.Mode.SELECT_NEW_ID) {
label.setText("New &R.string.");
        } else {
            label.setText("ID &R.string.");
}

mStringIdCombo = new Combo(group, SWT.SINGLE | SWT.LEFT | SWT.BORDER | SWT.DROP_DOWN);
//Synthetic comment -- @@ -196,7 +202,9 @@
private void createResFileGroup(Composite content) {

Group group = new Group(content, SWT.NONE);
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
group.setText("XML resource to edit");

GridLayout layout = new GridLayout();
//Synthetic comment -- @@ -215,8 +223,7 @@
gd.widthHint = ConfigurationSelector.WIDTH_HINT;
gd.heightHint = ConfigurationSelector.HEIGHT_HINT;
mConfigSelector.setLayoutData(gd);
        OnConfigSelectorUpdated onConfigSelectorUpdated = new OnConfigSelectorUpdated();
        mConfigSelector.setOnChangeListener(onConfigSelectorUpdated);

// line: selection of the output file

//Synthetic comment -- @@ -226,15 +233,49 @@
mResFileCombo = new Combo(group, SWT.DROP_DOWN);
mResFileCombo.select(0);
mResFileCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        mResFileCombo.addModifyListener(onConfigSelectorUpdated);

// set output file name to the last one used

String projPath = mProject.getFullPath().toPortableString();
String filePath = sLastResFilePath.get(projPath);

mResFileCombo.setText(filePath != null ? filePath : DEFAULT_RES_FILE_PATH);
        onConfigSelectorUpdated.run();
}

/**
//Synthetic comment -- @@ -278,6 +319,9 @@

ExtractStringRefactoring ref = getOurRefactoring();

// Analyze fatal errors.

String text = mStringIdCombo.getText().trim();
//Synthetic comment -- @@ -372,7 +416,7 @@
}
}

    public class OnConfigSelectorUpdated implements Runnable, ModifyListener {

/** Regex pattern to parse a valid res path: it reads (/res/folder-name/)+(filename). */
private final Pattern mPathRegex = Pattern.compile(
//Synthetic comment -- @@ -422,9 +466,12 @@
mConfigSelector.getConfiguration(mTempConfig);
StringBuffer sb = new StringBuffer(RES_FOLDER_ABS);
sb.append(mTempConfig.getFolderName(ResourceFolderType.VALUES));
            sb.append('/');

String newPath = sb.toString();
if (newPath.equals(currPath) && newPath.equals(mLastFolderUsedInCombo)) {
// Path has not changed. No need to reload.
return;
//Synthetic comment -- @@ -493,6 +540,53 @@
}

/**
* Callback invoked when {@link ExtractStringInputPage#mResFileCombo} has been
* modified.
*/
//Synthetic comment -- @@ -546,4 +640,7 @@
}
}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoring.java
//Synthetic comment -- index 2ae9d23..aea65a4 100644

//Synthetic comment -- @@ -178,6 +178,11 @@
/** The path of the XML file that will define {@link #mXmlStringId}, selected by the user
*  in the wizard. */
private String mTargetXmlFileWsPath;

/** The list of changes computed by {@link #checkFinalConditions(IProgressMonitor)} and
*  used by {@link #createChange(IProgressMonitor)}. */
//Synthetic comment -- @@ -1569,4 +1574,12 @@
mTargetXmlFileWsPath = targetXmlFileWsPath;
}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFolderType.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFolderType.java
//Synthetic comment -- index 0a56ff5..735a23c 100644

//Synthetic comment -- @@ -38,10 +38,13 @@
mName = name;
}

public String getName() {
return mName;
}
    
/**
* Returns the enum by name.
* @param name The enum string value.
//Synthetic comment -- @@ -55,7 +58,7 @@
}
return null;
}
    
/**
* Returns the {@link ResourceFolderType} from the folder name
* @param folderName The name of the folder. This must be a valid folder name in the format







