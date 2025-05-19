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
    Text mFileNameTextField;
    String mDefaultAttrs;
    int mTargetApiLevel;

    String getDefaultAttrs() {
        return mDefaultAttrs;
    }

    public int getTargetApiLevel() {
        return mTargetApiLevel;
    }

    public String getFileName() {
        String fileName = mFileNameTextField.getText();
        if (!fileName.endsWith(".xml")) {
            fileName += ".xml"; // Automatically append .xml if not provided
        }
        return fileName.isEmpty() ? null : fileName; // Handle empty filenames
    }

    public void createControl(Composite parent) {
        mFileNameTextField = new Text(parent, SWT.BORDER);
        mFileNameTextField.setFocus();
        mFileNameTextField.setLayoutData(new org.eclipse.swt.layout.GridData(SWT.FILL, SWT.CENTER, true, false)); // Set layout data for width
        // Set initial focus explicitly to prevent Project field from gaining focus
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
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class NewXmlFileWizard extends Wizard implements INewWizard {
    private static final String PROJECT_LOGO_LARGE = "android_large"; //$NON-NLS-1$

    protected static final String MAIN_PAGE_NAME = "newAndroidXmlFilePage"; //$NON-NLS-1$

    private NewXmlFileCreationPage mMainPage;

    public void createNewFile(IStructuredSelection selection) {
        mMainPage.setDescription("Creates a new Android XML file.");
        mMainPage.setInitialSelection(selection);
    }

    private IFile createXmlFile() {
        IFile file = mMainPage.getDestinationFile();
        String name = file.getFullPath().toString();

        try {
            if (file.getParent() != null) {
                createWsParentDirectory(file.getParent());
            }

            TypeInfo type = mMainPage.getSelectedType();
            if (type == null) {
                return null;
            }

            StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
            sb.append('<').append(root);

            String xmlns = type.getXmlns();
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
            if (need_delete) {
                file.delete(IFile.KEEP_HISTORY | IFile.FORCE, null);
            }
            file.create(stream, true, null);
            return file;
        } catch (UnsupportedEncodingException e) {
            Logger.log("Encoding error: " + e.getMessage()); // Replace with secure logging
        } catch (CoreException e) {
            Logger.log("Core exception: " + e.getMessage()); // Replace with secure logging
        }
        return null;
    }

    private boolean createWsParentDirectory(IContainer wsPath) {
        if (wsPath == null || wsPath.exists()) {
            return false;
        }
        try {
            wsPath.create(true, true, null);
            return true;
        } catch (CoreException e) {
            Logger.log("Core exception creating directory: " + e.getMessage()); // Replace with secure logging
        }
        return false;
    }
}
//<End of snippet n. 1>