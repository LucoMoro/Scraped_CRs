//<Beginning of snippet n. 0>


import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;

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
}

/**
 * Returns the destination filename or an empty string.
 */
public String getFileName() {
        String filename = mFileNameTextField == null ? "" : mFileNameTextField.getText(); //$NON-NLS-1$
        if (!filename.endsWith(".xml")) {
            if (filename.trim().isEmpty()) {
                return "";
            }
            filename += ".xml";
        }
        return filename;
    }

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
import org.eclipse.jface.wizard.Wizard;

public class NewXmlFileWizard extends Wizard implements INewWizard {

    private static final String PROJECT_LOGO_LARGE = "android_large"; //$NON-NLS-1$
    
    protected static final String MAIN_PAGE_NAME = "newAndroidXmlFilePage"; //$NON-NLS-1$

    private NewXmlFileCreationPage mMainPage;
    
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        mMainPage = new NewXmlFileCreationPage();
        mMainPage.setDescription("Creates a new Android XML file.");
        mMainPage.setInitialSelection(selection);
    }

    private IFile createXmlFile() {
        IFile file = mMainPage.getDestinationFile();
        String name = file.getFullPath().toString();
        
        try {
            TypeInfo type = mMainPage.getSelectedType();
            if (type == null) {
                return null;
            }
            
            StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");   //$NON-NLS-1$
            sb.append('<').append(root);
            String xmlns = SdkConstants.NS_RESOURCES;
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
            if (need_delete) {
                file.delete(IFile.KEEP_HISTORY | IFile.FORCE, null /*monitor*/);
            }
            file.create(stream, true /*force*/, null /*progress*/);
            return file;
        } catch (UnsupportedEncodingException e) {
            return null;
        } catch (CoreException e) {
            // Handle exception securely without printing stack trace
            return null;
        }
    }

    private boolean createWsParentDirectory(IContainer wsPath) {
        if (wsPath.getType() == IContainer.FOLDER) {
            if (wsPath == null || wsPath.exists()) {
                return true;
            }
        }
        return false;
    }

//<End of snippet n. 1>