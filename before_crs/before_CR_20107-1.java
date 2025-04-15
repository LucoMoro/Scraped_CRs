/*New XML Wizard fixes

1. Make the LinearLayout default width and height values be
match_parent for Android 2.x and up, fill_parent for older.  This
fixes issue 13481: ADT creates layout resources with fill_parent
instead of match_parent

2. Give the Name field initial focus rather than the Project field,
since you usually don't want to edit the Project field and more than
once I've accidentally edited the project name instead.

3. Don't require the user to manually type ".xml" as a suffix since
it's required anyway. If you type "foo", it will create "foo.xml". You
can however type "foo.xml" (and you cannot type any other extensions.)

Change-Id:Ibd03bb73e283b02991662de6ab889f647554b7d6*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java
//Synthetic comment -- index 492cae4..e48cbaa 100644

//Synthetic comment -- @@ -48,6 +48,7 @@
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -76,6 +77,16 @@
*/
class NewXmlFileCreationPage extends WizardPage {

/**
* Information on one type of resource that can be created (e.g. menu, pref, layout, etc.)
*/
//Synthetic comment -- @@ -183,13 +194,17 @@
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
//Synthetic comment -- @@ -207,10 +222,28 @@
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
//Synthetic comment -- @@ -389,9 +422,21 @@

/**
* Returns the destination filename or an empty string.
*/
public String getFileName() {
        return mFileNameTextField == null ? "" : mFileNameTextField.getText();         //$NON-NLS-1$
}

/**
//Synthetic comment -- @@ -718,6 +763,7 @@

IPath wsFolderPath = null;
String fileName = null;
if (res.getType() == IResource.FOLDER) {
wsFolderPath = res.getProjectRelativePath();
} else if (res.getType() == IResource.FILE) {
//Synthetic comment -- @@ -821,7 +867,7 @@

if (data == null) {
// We should have both a target and its data.
                        // However if the wizard is invoked whilst the platform is still being
// loaded we can end up in a weird case where we have a target but it
// doesn't have any data yet.
// Lets log a warning and silently ignore this root.
//Synthetic comment -- @@ -1012,7 +1058,7 @@
*/
private void onRadioTypeUpdated(Button typeWidget) {
// Do nothing if this is an internal modification or if the widget has been
        // de-selected.
if (mInternalTypeUpdate || !typeWidget.getSelection()) {
return;
}
//Synthetic comment -- @@ -1253,9 +1299,9 @@
// -- update UI & enable finish if there's no error
setPageComplete(error == null);
if (error != null) {
            setMessage(error, WizardPage.ERROR);
} else if (warning != null) {
            setMessage(warning, WizardPage.WARNING);
} else {
setErrorMessage(null);
setMessage(null);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileWizard.java
//Synthetic comment -- index 9f4a518..2d40cf9 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.resource.ImageDescriptor;
//Synthetic comment -- @@ -52,7 +53,7 @@
public class NewXmlFileWizard extends Wizard implements INewWizard {

private static final String PROJECT_LOGO_LARGE = "android_large"; //$NON-NLS-1$
    
protected static final String MAIN_PAGE_NAME = "newAndroidXmlFilePage"; //$NON-NLS-1$

private NewXmlFileCreationPage mMainPage;
//Synthetic comment -- @@ -67,7 +68,7 @@
mMainPage.setDescription("Creates a new Android XML file.");
mMainPage.setInitialSelection(selection);
}
    
/**
* Creates the wizard page.
* <p/>
//Synthetic comment -- @@ -126,7 +127,7 @@
}

// -- Custom Methods --
    
private IFile createXmlFile() {
IFile file = mMainPage.getDestinationFile();
String name = file.getFullPath().toString();
//Synthetic comment -- @@ -142,7 +143,7 @@
} else {
createWsParentDirectory(file.getParent());
}
        
TypeInfo type = mMainPage.getSelectedType();
if (type == null) {
// this is not expected to happen
//Synthetic comment -- @@ -157,20 +158,20 @@
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

//Synthetic comment -- @@ -180,9 +181,9 @@
byte[] buf = result.getBytes("UTF8");
InputStream stream = new ByteArrayInputStream(buf);
if (need_delete) {
                file.delete(IFile.KEEP_HISTORY | IFile.FORCE, null /*monitor*/);
}
            file.create(stream, true /*force*/, null /*progres*/);
return file;
} catch (UnsupportedEncodingException e) {
error = e.getMessage();
//Synthetic comment -- @@ -196,7 +197,7 @@
}

private boolean createWsParentDirectory(IContainer wsPath) {
        if (wsPath.getType() == IContainer.FOLDER) {
if (wsPath == null || wsPath.exists()) {
return true;
}
//Synthetic comment -- @@ -211,7 +212,7 @@
e.printStackTrace();
}
}
        
return false;
}








