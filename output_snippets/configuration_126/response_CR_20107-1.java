//<Beginning of snippet n. 0>


import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

class NewXmlFileCreationPage extends WizardPage {

    private String mDefaultAttrs;
    private int mTargetApiLevel;
    private Text mFileNameTextField;

    NewXmlFileCreationPage(String pageName) {
        super(pageName);
        setTitle("New XML File");
        setMessage("Create a new Android XML file.");
    }

    String getDefaultAttrs() {
        return mDefaultAttrs;
    }

    public int getTargetApiLevel() {
        return mTargetApiLevel;
    }

    public String getFileName() {
        String filename = mFileNameTextField == null ? "" : mFileNameTextField.getText();
        if (!filename.endsWith(".xml")) {
            filename += ".xml";
        }
        if (filename.contains(".")) {
            String[] parts = filename.split("\\.");
            if (parts.length > 1 && !parts[parts.length - 1].equals("xml")) {
                throw new IllegalArgumentException("Only .xml files are allowed.");
            }
        }
        return filename;
    }

    public void createControl(Composite parent) {
        mFileNameTextField = new Text(parent, SWT.BORDER);
        mFileNameTextField.setFocus();
        // Additional UI initialization...
        setControl(mFileNameTextField);
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

    public void addPages() {
        mMainPage = new NewXmlFileCreationPage(MAIN_PAGE_NAME);
        mMainPage.setDescription("Creates a new Android XML file.");
        addPage(mMainPage);
    }

    private IFile createXmlFile() {
        IFile file = mMainPage.getDestinationFile();
        String name = file.getFullPath().toString();
        
        try {
            TypeInfo type = mMainPage.getSelectedType();
            if (type == null) {
                throw new IllegalStateException("Expected a type to be selected.");
            }

            StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");   //$NON-NLS-1$
            sb.append('<').append(type.getRoot()).append(">\n");

            String attrs = type.getDefaultAttrs();
            if (attrs != null) {
                sb.append("\n  ").append(attrs.replace("\n", "\n  "));  //$NON-NLS-1$ //$NON-NLS-2$
            }

            sb.append("</").append(type.getRoot()).append(">\n");  //$NON-NLS-1$ //$NON-NLS-2$

            byte[] buf = sb.toString().getBytes("UTF8");
            InputStream stream = new ByteArrayInputStream(buf);
            createWsParentDirectory(file.getParent());
            if (file.exists()) {
                file.delete(IFile.KEEP_HISTORY | IFile.FORCE, null);
            }
            file.create(stream, true, null);
            return file;
        } catch (UnsupportedEncodingException e) {
            logError("Unsupported encoding: " + e.getMessage());
        } catch (CoreException e) {
            logError("Core exception while creating file: " + e.getMessage());
        } catch (IllegalStateException e) {
            logError("Error in file creation: " + e.getMessage());
        }
        return null;
    }

    private boolean createWsParentDirectory(IContainer wsPath) {
        if (wsPath.getType() == IContainer.FOLDER) {
            if (wsPath == null || wsPath.exists()) {
                return true;
            }
        }
        logError("Invalid workspace directory.");
        return false;
    }

    private void logError(String message) {
        // Implement structured logging framework instead of printStackTrace
        // Placeholder for actual logging implementation
        Logger.logError(message);
    }
}
//<End of snippet n. 1>