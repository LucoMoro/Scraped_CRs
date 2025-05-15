
//<Beginning of snippet n. 0>


import com.android.ide.eclipse.adt.internal.assetstudio.AssetType;
import com.android.ide.eclipse.adt.internal.assetstudio.ConfigureAssetSetPage;
import com.android.ide.eclipse.adt.internal.assetstudio.CreateAssetSetWizardState;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectCreator;
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectCreator.ProjectPopulator;
import com.android.ide.eclipse.adt.internal.wizards.newxmlfile.NewXmlFileWizard;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstall2;
import org.eclipse.jdt.launching.IVMInstallType;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ltk.core.refactoring.Change;
NewProjectCreator.create(monitor, mProject, mValues.target, projectPopulator,
mValues.isLibrary, mValues.projectLocation);

            requireJdk6(mProject);

try {
mProject.refreshLocal(DEPTH_INFINITE, new NullProgressMonitor());
} catch (CoreException e) {
}

/**
     * Makes the given project use JDK 6 as the compilation target, if a JDK 6
     * install is found, even if JDK 5 (for example) is the default.
     */
    @SuppressWarnings("restriction") // JDT API for setting compliance options
    private static void requireJdk6(IProject project) throws CoreException {
        IJavaProject javaProject = BaseProjectHelper.getJavaProject(project);
        if (javaProject == null) {
            return;
        }

        boolean haveJdk6 = false;
        IVMInstallType[] types = JavaRuntime.getVMInstallTypes();
        for (int i = 0; i < types.length; i++) {
            IVMInstallType type = types[i];
            IVMInstall[] installs = type.getVMInstalls();
            for (int j = 0; j < installs.length; j++) {
                IVMInstall install = installs[j];
                if (install instanceof IVMInstall2) {
                    IVMInstall2 install2 = (IVMInstall2) install;
                    if (JavaCore.VERSION_1_6.equals(install2.getJavaVersion())) {
                        haveJdk6 = true;
                    }
                }
            }
        }
        if (haveJdk6) {
            Map<String, String> options = javaProject.getOptions(false);
            JavaModelUtil.setComplianceOptions(options, JavaCore.VERSION_1_6);
            JavaModelUtil.setDefaultClassfileOptions(options, JavaCore.VERSION_1_6);
            javaProject.setOptions(options);
        }
    }

    /**
* Generate custom icons into the project based on the asset studio wizard state
*/
private void generateIcons(final IProject newProject) {

//<End of snippet n. 0>








