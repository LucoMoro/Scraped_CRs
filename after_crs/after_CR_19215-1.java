/*ADT string refactoring: UI options to replace in files.

Change-Id:Ifeef5fd444c2c18b9c071955b8e8567d6515ea95*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringInputPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringInputPage.java
//Synthetic comment -- index cbcd581..b05318c 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.ide.eclipse.adt.internal.ui.ConfigurationSelector.SelectorMode;
import com.android.sdklib.SdkConstants;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
//Synthetic comment -- @@ -38,6 +39,7 @@
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
//Synthetic comment -- @@ -70,6 +72,10 @@
private ConfigurationSelector mConfigSelector;
/** The combo to display the existing XML files or enter a new one. */
private Combo mResFileCombo;
    /** Checkbox asking whether to replace in all Java files. */
    private Button mReplaceAllJava;
    /** Checkbox asking whether to replace in all XML files with same name but other res config */
    private Button mReplaceAllXml;

/** Regex pattern to read a valid res XML file path. It checks that the are 2 folders and
*  a leaf file name ending with .xml */
//Synthetic comment -- @@ -86,6 +92,8 @@

private XmlStringFileHelper mXmlHelper = new XmlStringFileHelper();

    private final OnConfigSelectorUpdated mOnConfigSelectorUpdated = new OnConfigSelectorUpdated();


public ExtractStringInputPage(IProject project) {
super("ExtractStringInputPage");  //$NON-NLS-1$
//Synthetic comment -- @@ -97,17 +105,21 @@
* <p/>
* Note that at that point the initial conditions have been checked in
* {@link ExtractStringRefactoring}.
     * <p/>
     *
     * Note: the special tag below defines this as the entry point for the WindowsDesigner Editor.
     * @wbp.parser.entryPoint
*/
public void createControl(Composite parent) {
Composite content = new Composite(parent, SWT.NONE);
GridLayout layout = new GridLayout();
content.setLayout(layout);

createStringGroup(content);
createResFileGroup(content);
        createOptionGroup(content);

        initUi();
setControl(content);
}

//Synthetic comment -- @@ -123,10 +135,9 @@

Group group = new Group(content, SWT.NONE);
group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        group.setText("New String");
if (ref.getMode() == ExtractStringRefactoring.Mode.EDIT_SOURCE) {
group.setText("String Replacement");
}

GridLayout layout = new GridLayout();
//Synthetic comment -- @@ -152,19 +163,14 @@
}
});

// line : Textfield for new ID

label = new Label(group, SWT.NONE);
        label.setText("ID &R.string.");
if (ref.getMode() == ExtractStringRefactoring.Mode.EDIT_SOURCE) {
label.setText("&Replace by R.string.");
} else if (ref.getMode() == ExtractStringRefactoring.Mode.SELECT_NEW_ID) {
label.setText("New &R.string.");
}

mStringIdCombo = new Combo(group, SWT.SINGLE | SWT.LEFT | SWT.BORDER | SWT.DROP_DOWN);
//Synthetic comment -- @@ -196,7 +202,9 @@
private void createResFileGroup(Composite content) {

Group group = new Group(content, SWT.NONE);
        GridData gd_group = new GridData(GridData.FILL_HORIZONTAL);
        gd_group.grabExcessVerticalSpace = true;
        group.setLayoutData(gd_group);
group.setText("XML resource to edit");

GridLayout layout = new GridLayout();
//Synthetic comment -- @@ -215,8 +223,7 @@
gd.widthHint = ConfigurationSelector.WIDTH_HINT;
gd.heightHint = ConfigurationSelector.HEIGHT_HINT;
mConfigSelector.setLayoutData(gd);
        mConfigSelector.setOnChangeListener(mOnConfigSelectorUpdated);

// line: selection of the output file

//Synthetic comment -- @@ -226,15 +233,49 @@
mResFileCombo = new Combo(group, SWT.DROP_DOWN);
mResFileCombo.select(0);
mResFileCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        mResFileCombo.addModifyListener(mOnConfigSelectorUpdated);
    }

    /**
     * Creates the bottom option groups with a few checkboxes.
     *
     * @param content A composite with a 1-column grid layout
     */
    private void createOptionGroup(Composite content) {
        Group options = new Group(content, SWT.NONE);
        options.setText("Options");
        GridData gd_Options = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_Options.widthHint = 77;
        options.setLayoutData(gd_Options);
        options.setLayout(new GridLayout(1, false));

        mReplaceAllJava = new Button(options, SWT.CHECK);
        mReplaceAllJava.setToolTipText("When checked, the exact same string literal will be replaced in all Java files.");
        mReplaceAllJava.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        mReplaceAllJava.setText("Replace in all &Java files");

        mReplaceAllXml = new Button(options, SWT.CHECK);
        mReplaceAllXml.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        mReplaceAllXml.setToolTipText("When checked, string literals will be replaced in other XML resources files having the same name but located in different resource configuration folders.");
        mReplaceAllXml.setText("Replace in all &XML files of different configuration");
        mReplaceAllXml.setEnabled(false);
    }

    // -- Start of internal part ----------
    // Hide everything down-below from WindowsDesigner Editor
    //$hide>>$

    /**
     * Init UI just after it has been created the first time.
     */
    private void initUi() {
// set output file name to the last one used
String projPath = mProject.getFullPath().toPortableString();
String filePath = sLastResFilePath.get(projPath);

mResFileCombo.setText(filePath != null ? filePath : DEFAULT_RES_FILE_PATH);
        mOnConfigSelectorUpdated.run();
        validatePage();
}

/**
//Synthetic comment -- @@ -278,6 +319,9 @@

ExtractStringRefactoring ref = getOurRefactoring();

        ref.setReplaceAllJava(mReplaceAllJava.getSelection());
        ref.setReplaceAllXml(mReplaceAllXml.isEnabled() && mReplaceAllXml.getSelection());

// Analyze fatal errors.

String text = mStringIdCombo.getText().trim();
//Synthetic comment -- @@ -372,7 +416,7 @@
}
}

    private class OnConfigSelectorUpdated implements Runnable, ModifyListener {

/** Regex pattern to parse a valid res path: it reads (/res/folder-name/)+(filename). */
private final Pattern mPathRegex = Pattern.compile(
//Synthetic comment -- @@ -422,9 +466,12 @@
mConfigSelector.getConfiguration(mTempConfig);
StringBuffer sb = new StringBuffer(RES_FOLDER_ABS);
sb.append(mTempConfig.getFolderName(ResourceFolderType.VALUES));
            sb.append(AndroidConstants.WS_SEP);

String newPath = sb.toString();

            enableReplaceAllXml(newPath);

if (newPath.equals(currPath) && newPath.equals(mLastFolderUsedInCombo)) {
// Path has not changed. No need to reload.
return;
//Synthetic comment -- @@ -493,6 +540,53 @@
}

/**
         * Given a new path such as /res/values-config for the XML target file, check whether
         * there is at least one other /res/values* folder that contains the same XML target
         * file name. If there is, enable the "Replace XML files" checkbox.
         */
        private void enableReplaceAllXml(String targetPath) {
            boolean hasMultiResFolder = false;

            try {
                String wsFolderPath = mResFileCombo.getText();
                String[] segments = wsFolderPath.split(AndroidConstants.WS_SEP);
                String xmlFileName = segments[segments.length - 1];

                IResource targetFolder = mProject.findMember(targetPath);
                IFolder resFolder = mProject.getFolder(RES_FOLDER_ABS);

                // Filter is /project/res/values (no dash or /)
                String wsPathFilter = resFolder.getFullPath().toString() +
                                      AndroidConstants.WS_SEP +
                                      ResourceFolderType.VALUES.getName();

                IResource[] valuesFolders = resFolder.members(IContainer.EXCLUDE_DERIVED |
                                                              IContainer.INCLUDE_PHANTOMS |
                                                              IContainer.INCLUDE_HIDDEN);
                if (valuesFolders != null && valuesFolders.length > 0) {
                    for (IResource f : valuesFolders) {
                        if (f instanceof IFolder &&
                                !f.equals(targetFolder) &&
                                f.getFullPath().toString().startsWith(wsPathFilter)) {

                            f = ((IFolder) f).findMember(xmlFileName);
                            if (f != null && f.exists()) {
                                hasMultiResFolder = true;
                                break;
                            }
                        }
                    }
                }
            } catch (NullPointerException e) {
                // ignore
            } catch (CoreException e) {
                // ignore
            }

            mReplaceAllXml.setEnabled(hasMultiResFolder);
        }

        /**
* Callback invoked when {@link ExtractStringInputPage#mResFileCombo} has been
* modified.
*/
//Synthetic comment -- @@ -546,4 +640,7 @@
}
}

    // End of hiding from SWT Designer
    //$hide<<$

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoring.java
//Synthetic comment -- index 2ae9d23..aea65a4 100644

//Synthetic comment -- @@ -178,6 +178,11 @@
/** The path of the XML file that will define {@link #mXmlStringId}, selected by the user
*  in the wizard. */
private String mTargetXmlFileWsPath;
    /** True if we should find & replace in all Java files. */
    private boolean mReplaceAllJava;
    /** True if we should find & replace in all XML files of the same name in other res configs
     * (other than the main {@link #mTargetXmlFileWsPath}.) */
    private boolean mReplaceAllXml;

/** The list of changes computed by {@link #checkFinalConditions(IProgressMonitor)} and
*  used by {@link #createChange(IProgressMonitor)}. */
//Synthetic comment -- @@ -1569,4 +1574,12 @@
mTargetXmlFileWsPath = targetXmlFileWsPath;
}

    public void setReplaceAllJava(boolean replaceAllJava) {
        mReplaceAllJava = replaceAllJava;
    }

    public void setReplaceAllXml(boolean replaceAllXml) {
        mReplaceAllXml = replaceAllXml;
    }

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFolderType.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFolderType.java
//Synthetic comment -- index 0a56ff5..735a23c 100644

//Synthetic comment -- @@ -38,10 +38,13 @@
mName = name;
}

    /**
     * Returns the folder name for this resource folder type.
     */
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







