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
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -76,6 +77,16 @@
*/
class NewXmlFileCreationPage extends WizardPage {

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        // Ensure the initial focus is in the Name field; you usually don't need
        // to edit the default text field (the project name)
        if (visible && mFileNameTextField != null) {
            mFileNameTextField.setFocus();
        }
    }

/**
* Information on one type of resource that can be created (e.g. menu, pref, layout, etc.)
*/
//Synthetic comment -- @@ -183,13 +194,17 @@
/**
* When not null, this represent extra attributes that must be specified in the
* root element of the generated XML file. When null, no extra attributes are inserted.
         *
         * @param project the project to get the attributes for
*/
        String getDefaultAttrs(IProject project) {
return mDefaultAttrs;
}

/**
* The minimum API level required by the current SDK target to support this feature.
         *
         * @return the minimum API level
*/
public int getTargetApiLevel() {
return mTargetApiLevel;
//Synthetic comment -- @@ -207,10 +222,28 @@
AndroidTargetData.DESCRIPTOR_LAYOUT,                        // root seed
"LinearLayout",                                             // default root
SdkConstants.NS_RESOURCES,                                  // xmlns
                "",                                                         // not used, see below
1                                                           // target API level
                ) {
                // The default attributes must be determined dynamically since whether
                // we use match_parent or fill_parent depends on the API level of the
                // project
                @Override
                String getDefaultAttrs(IProject project) {
                    Sdk currentSdk = Sdk.getCurrent();
                    if (currentSdk != null) {
                        IAndroidTarget target = currentSdk.getTarget(project);
                        // fill_parent was renamed match_parent in API level 8
                        if (target != null && target.getVersion().getApiLevel() >= 8) {
                            return "android:layout_width=\"match_parent\"\n"    //$NON-NLS-1$
                                    + "android:layout_height=\"match_parent\""; //$NON-NLS-1$
                        }
                    }

                    return "android:layout_width=\"fill_parent\"\n"    //$NON-NLS-1$
                            + "android:layout_height=\"fill_parent\""; //$NON-NLS-1$
                }
        },
new TypeInfo("Values",                                              // UI name
"An XML file with simple values: colors, strings, dimensions, etc.", // tooltip
ResourceFolderType.VALUES,                                  // folder type
//Synthetic comment -- @@ -389,9 +422,21 @@

/**
* Returns the destination filename or an empty string.
     *
     * @return the filename, never null.
*/
public String getFileName() {
        String fileName;
        if (mFileNameTextField == null) {
            fileName = ""; //$NON-NLS-1$
        } else {
            fileName = mFileNameTextField.getText().trim();
            if (fileName.length() > 0 && fileName.indexOf('.') == -1) {
                fileName = fileName + AndroidConstants.DOT_XML;
            }
        }

        return fileName;
}

/**
//Synthetic comment -- @@ -718,6 +763,7 @@

IPath wsFolderPath = null;
String fileName = null;
                assert res != null; // Eclipse incorrectly thinks res could be null, so tell it no
if (res.getType() == IResource.FOLDER) {
wsFolderPath = res.getProjectRelativePath();
} else if (res.getType() == IResource.FILE) {
//Synthetic comment -- @@ -821,7 +867,7 @@

if (data == null) {
// We should have both a target and its data.
                        // However if the wizard is invoked while the platform is still being
// loaded we can end up in a weird case where we have a target but it
// doesn't have any data yet.
// Lets log a warning and silently ignore this root.
//Synthetic comment -- @@ -1012,7 +1058,7 @@
*/
private void onRadioTypeUpdated(Button typeWidget) {
// Do nothing if this is an internal modification or if the widget has been
        // deselected.
if (mInternalTypeUpdate || !typeWidget.getSelection()) {
return;
}
//Synthetic comment -- @@ -1253,9 +1299,9 @@
// -- update UI & enable finish if there's no error
setPageComplete(error == null);
if (error != null) {
            setMessage(error, IMessageProvider.ERROR);
} else if (warning != null) {
            setMessage(warning, IMessageProvider.WARNING);
} else {
setErrorMessage(null);
setMessage(null);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileWizard.java
//Synthetic comment -- index 9f4a518..2d40cf9 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
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

        String attrs = type.getDefaultAttrs(mMainPage.getProject());
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
                file.delete(IResource.KEEP_HISTORY | IResource.FORCE, null /*monitor*/);
}
            file.create(stream, true /*force*/, null /*progress*/);
return file;
} catch (UnsupportedEncodingException e) {
error = e.getMessage();
//Synthetic comment -- @@ -196,7 +197,7 @@
}

private boolean createWsParentDirectory(IContainer wsPath) {
        if (wsPath.getType() == IResource.FOLDER) {
if (wsPath == null || wsPath.exists()) {
return true;
}
//Synthetic comment -- @@ -211,7 +212,7 @@
e.printStackTrace();
}
}

return false;
}








