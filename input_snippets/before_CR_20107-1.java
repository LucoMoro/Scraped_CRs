
//<Beginning of snippet n. 0>


import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
*/
class NewXmlFileCreationPage extends WizardPage {

/**
* Information on one type of resource that can be created (e.g. menu, pref, layout, etc.)
*/
/**
* When not null, this represent extra attributes that must be specified in the
* root element of the generated XML file. When null, no extra attributes are inserted.
*/
        String getDefaultAttrs() {
return mDefaultAttrs;
}

/**
* The minimum API level required by the current SDK target to support this feature.
*/
public int getTargetApiLevel() {
return mTargetApiLevel;
AndroidTargetData.DESCRIPTOR_LAYOUT,                        // root seed
"LinearLayout",                                             // default root
SdkConstants.NS_RESOURCES,                                  // xmlns
                "android:layout_width=\"fill_parent\"\n" + //$NON-NLS-1$    // default attributes
                "android:layout_height=\"fill_parent\"",   //$NON-NLS-1$
1                                                           // target API level
                ),
new TypeInfo("Values",                                              // UI name
"An XML file with simple values: colors, strings, dimensions, etc.", // tooltip
ResourceFolderType.VALUES,                                  // folder type

/**
* Returns the destination filename or an empty string.
*/
public String getFileName() {
        return mFileNameTextField == null ? "" : mFileNameTextField.getText();         //$NON-NLS-1$
}

/**

IPath wsFolderPath = null;
String fileName = null;
if (res.getType() == IResource.FOLDER) {
wsFolderPath = res.getProjectRelativePath();
} else if (res.getType() == IResource.FILE) {

if (data == null) {
// We should have both a target and its data.
                        // However if the wizard is invoked whilst the platform is still being
// loaded we can end up in a weird case where we have a target but it
// doesn't have any data yet.
// Lets log a warning and silently ignore this root.
*/
private void onRadioTypeUpdated(Button typeWidget) {
// Do nothing if this is an internal modification or if the widget has been
        // de-selected.
if (mInternalTypeUpdate || !typeWidget.getSelection()) {
return;
}
// -- update UI & enable finish if there's no error
setPageComplete(error == null);
if (error != null) {
            setMessage(error, WizardPage.ERROR);
} else if (warning != null) {
            setMessage(warning, WizardPage.WARNING);
} else {
setErrorMessage(null);
setMessage(null);

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.resource.ImageDescriptor;
public class NewXmlFileWizard extends Wizard implements INewWizard {

private static final String PROJECT_LOGO_LARGE = "android_large"; //$NON-NLS-1$
    
protected static final String MAIN_PAGE_NAME = "newAndroidXmlFilePage"; //$NON-NLS-1$

private NewXmlFileCreationPage mMainPage;
mMainPage.setDescription("Creates a new Android XML file.");
mMainPage.setInitialSelection(selection);
}
    
/**
* Creates the wizard page.
* <p/>
}

// -- Custom Methods --
    
private IFile createXmlFile() {
IFile file = mMainPage.getDestinationFile();
String name = file.getFullPath().toString();
} else {
createWsParentDirectory(file.getParent());
}
        
TypeInfo type = mMainPage.getSelectedType();
if (type == null) {
// this is not expected to happen
file.toString());
return null;
}
        
StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");   //$NON-NLS-1$

sb.append('<').append(root);
if (xmlns != null) {
sb.append('\n').append("  xmlns:android=\"").append(xmlns).append("\"");  //$NON-NLS-1$ //$NON-NLS-2$
}
        
        String attrs = type.getDefaultAttrs();
if (attrs != null) {
sb.append("\n  ");                       //$NON-NLS-1$
sb.append(attrs.replace("\n", "\n  "));  //$NON-NLS-1$ //$NON-NLS-2$
}
        
sb.append(">\n");                            //$NON-NLS-1$
sb.append("</").append(root).append(">\n");  //$NON-NLS-1$ //$NON-NLS-2$

byte[] buf = result.getBytes("UTF8");
InputStream stream = new ByteArrayInputStream(buf);
if (need_delete) {
                file.delete(IFile.KEEP_HISTORY | IFile.FORCE, null /*monitor*/);
}
            file.create(stream, true /*force*/, null /*progres*/);
return file;
} catch (UnsupportedEncodingException e) {
error = e.getMessage();
}

private boolean createWsParentDirectory(IContainer wsPath) {
        if (wsPath.getType() == IContainer.FOLDER) {
if (wsPath == null || wsPath.exists()) {
return true;
}
e.printStackTrace();
}
}
        
return false;
}


//<End of snippet n. 1>








