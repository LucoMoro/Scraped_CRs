//<Beginning of snippet n. 0>


import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;

/**
* NewXmlFileCreationPage class for creating XML files.
*/
class NewXmlFileCreationPage extends WizardPage {

    private String mDefaultAttrs;
    private int mTargetApiLevel;

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
    }

    String getLayoutAttributes() {
        int targetApiLevel = getTargetApiLevel();
        if (targetApiLevel < 3) {
            return "android:layout_width=\"fill_parent\"\n" + 
                   "android:layout_height=\"fill_parent\"";   
        }
        return "android:layout_width=\"match_parent\"\n" + 
               "android:layout_height=\"match_parent\""; 
    }

    // Other methods not shown for brevity

    /**
    * Returns the destination filename or an empty string.
    */
    public String getFileName() {
        String fileName = mFileNameTextField == null ? "" : mFileNameTextField.getText();         
        if (!fileName.endsWith(".xml")) {
            fileName += ".xml";
        }
        return fileName;
    }

    // Other methods not shown for brevity

    private void onRadioTypeUpdated(Button typeWidget) {
        // Do nothing if this is an internal modification or if the widget has been de-selected.
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
        }
    }

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

    public void init(IWorkbench workbench, IStructuredSelection selection) {
        mMainPage.setDescription("Creates a new Android XML file.");
        mMainPage.setInitialSelection(selection);
    }
    
    /**
    * Creates the wizard page.
    * <p/>
    */

    // -- Custom Methods --
    
    private IFile createXmlFile() {
        IFile file = mMainPage.getDestinationFile();
        String name = file.getFullPath().toString();
        
        // Other logic not shown for brevity...

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

        byte[] buf = sb.toString().getBytes("UTF8");
        InputStream stream = new ByteArrayInputStream(buf);
        try {
            if (need_delete) {
                file.delete(IFile.KEEP_HISTORY | IFile.FORCE, null /*monitor*/);
            }
            file.create(stream, true /*force*/, null /*progress*/);
            return file;
        } catch (UnsupportedEncodingException e) {
            error = e.getMessage();
            return null;
        }
    }

    private boolean createWsParentDirectory(IContainer wsPath) {
        if (wsPath.getType() == IContainer.FOLDER) {
            if (wsPath == null || wsPath.exists()) {
                return true;
            }
            e.printStackTrace();
        }
        return false;
    }


//<End of snippet n. 1>