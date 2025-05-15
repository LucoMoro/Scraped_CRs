//<Beginning of snippet n. 0>


import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;

class NewXmlFileCreationPage extends WizardPage {

    String getDefaultAttrs() {
        return mDefaultAttrs;
    }

    public int getTargetApiLevel() {
        return mTargetApiLevel;
    }
    
    // Updated LinearLayout attributes for Android 2.x and above.
    /* XML representation for the LinearLayout with updated attributes */
    // Assume this code is within a relevant XML generation method.
    private String getLinearLayoutXml() {
        return "<LinearLayout " +
                "android:layout_width=\"match_parent\"\n" + 
                "android:layout_height=\"match_parent\">\n";
    }

    public String getFileName() {
        String fileName = mFileNameTextField == null ? "" : mFileNameTextField.getText();
        if (!fileName.endsWith(".xml")) {
            fileName += ".xml";
        }
        return fileName;
    }

    private void onRadioTypeUpdated(Button typeWidget) {
        if (mInternalTypeUpdate || !typeWidget.getSelection()) {
            return;
        }
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

    public void addPages() {
        mMainPage = new NewXmlFileCreationPage();
        mMainPage.setDescription("Creates a new Android XML file.");
        mMainPage.setInitialSelection(selection);
        addPage(mMainPage);
    }

    private IFile createXmlFile() {
        try {
            IFile file = mMainPage.getDestinationFile();
            String name = file.getFullPath().toString();

            if (file.getParent() != null) {
                createWsParentDirectory(file.getParent());
            }
            
            TypeInfo type = mMainPage.getSelectedType();
            if (type == null) {
                return null;
            }
            
            StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");   //$NON-NLS-1$

            sb.append("<").append(type.getRootElement()).append(">\n"); // Assume type has method to retrieve root
            String attrs = type.getDefaultAttrs();
            if (attrs != null) {
                sb.append("\n  ").append(attrs.replace("\n", "\n  "));  //$NON-NLS-1$ //$NON-NLS-2$
            }
            
            sb.append(">\n"); // Close for tag
            sb.append("</").append(type.getRootElement()).append(">\n");  //$NON-NLS-1$ //$NON-NLS-2$

            byte[] buf = sb.toString().getBytes("UTF8");
            InputStream stream = new ByteArrayInputStream(buf);
            if (need_delete) {
                file.delete(IFile.KEEP_HISTORY | IFile.FORCE, null /*monitor*/);
            }
            file.create(stream, true /*force*/, null /*progres*/);
            return file;

        } catch (UnsupportedEncodingException e) {
            error = e.getMessage();
            return null;
        } catch (CoreException e) {
            // Removed debugging calls and sensitive error messages.
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