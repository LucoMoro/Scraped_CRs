//<Beginning of snippet n. 0>


import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

class NewXmlFileCreationPage extends WizardPage {
    private Text mFileNameTextField;
    private int mTargetApiLevel;
    private String mDefaultAttrs;

    public NewXmlFileCreationPage(String pageName) {
        super(pageName);
        setTitle("New XML File");
        setDescription("Create a new XML file");
    }

    String getDefaultAttrs() {
        return mDefaultAttrs;
    }

    public int getTargetApiLevel() {
        return mTargetApiLevel;
    }

    public String getFileName() {
        String fileName = mFileNameTextField.getText().trim();
        if (!fileName.endsWith(".xml") && !fileName.isEmpty()) {
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
    
    @Override
    public void createControl(Composite parent) {
        // Layout and controls initialization
        setControl(parent);
        mFileNameTextField.setFocus();
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
import org.eclipse.swt.widgets.Button;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

public class NewXmlFileWizard extends Wizard implements INewWizard {
    private static final String PROJECT_LOGO_LARGE = "android_large"; 
    protected static final String MAIN_PAGE_NAME = "newAndroidXmlFilePage"; 
    private NewXmlFileCreationPage mMainPage;
    private static final Logger LOGGER = Logger.getLogger(NewXmlFileWizard.class.getName());

    private IFile createXmlFile() {
        IFile file = mMainPage.getDestinationFile();
        String name = file.getFullPath().toString();
        try {
            StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"); 
            sb.append('<').append(root);
            if (xmlns != null) {
                sb.append('\n').append("  xmlns:android=\"").append(xmlns).append("\"");  
            }
            String attrs = type.getDefaultAttrs();
            if (attrs != null) {
                sb.append("\n  ");                       
                sb.append(attrs.replace("\n", "\n  "));  
            }
            sb.append(">\n");                            
            sb.append("</").append(root).append(">\n");  

            byte[] buf = sb.toString().getBytes("UTF8");
            InputStream stream = new ByteArrayInputStream(buf);
            file.create(stream, true, null);
            return file;
        } catch (UnsupportedEncodingException e) {
            LOGGER.warning("Encoding error: " + e.getMessage());
            return null;
        } catch (CoreException e) {
            LOGGER.warning("Core Exception: " + e.getMessage());
            return null;
        }
    }

    private boolean createWsParentDirectory(IContainer wsPath) {
        if (wsPath != null && wsPath.getType() == IContainer.FOLDER) {
            if (!wsPath.exists()) {
                LOGGER.warning("Directory does not exist: " + wsPath.getName());
                return false;
            }
            return true;
        }
        return false;
    }
}
//<End of snippet n. 1>