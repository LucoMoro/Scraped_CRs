/*Make welcome wizard a tiny bit configurable.

The welcome wizard has two steps right now: installing an SDK,
and opting in to data collection. This change allows the
constructor of the WelcomeWizard to select which among those
pages ought to be shown. This is useful in the case of the ADT
package installation where we know there is an SDK, and hence
we don't want to show the installation page.

Change-Id:I41a0489c1d0617a4bb2882516f51bd8cd6925c32*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/welcome/AdtStartup.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/welcome/AdtStartup.java
//Synthetic comment -- index aee0af3..65eee94 100644

//Synthetic comment -- @@ -78,11 +78,14 @@
}
}

        boolean showSdkInstallationPage = !isSdkSpecified() && isFirstTime();
        boolean showOptInDialogPage = !mStore.hasPingId();

        if (showSdkInstallationPage || showOptInDialogPage) {
            showWelcomeWizard(showSdkInstallationPage, showOptInDialogPage);
        }

        if (mStore.isPingOptIn()) {
sendUsageStats();
}

//Synthetic comment -- @@ -122,13 +125,6 @@
}

private boolean isFirstTime() {
for (int i = 0; i < 2; i++) {
String osSdkPath = null;

//Synthetic comment -- @@ -258,14 +254,16 @@
});
}

    private void showWelcomeWizard(final boolean showSdkInstallPage,
            final boolean showUsageOptInPage) {
final IWorkbench workbench = PlatformUI.getWorkbench();
workbench.getDisplay().asyncExec(new Runnable() {
@Override
public void run() {
IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
if (window != null) {
                    WelcomeWizard wizard = new WelcomeWizard(mStore, showSdkInstallPage,
                            showUsageOptInPage);
WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
dialog.open();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/welcome/WelcomeWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/welcome/WelcomeWizard.java
//Synthetic comment -- index a81082b..916924e 100644

//Synthetic comment -- @@ -41,16 +41,25 @@
*/
public class WelcomeWizard extends Wizard {
private final DdmsPreferenceStore mStore;

private WelcomeWizardPage mWelcomePage;
private UsagePermissionPage mUsagePage;

    private final boolean mShowWelcomePage;
    private final boolean mShowUsagePage;

/**
* Creates a new {@link WelcomeWizard}
*
* @param store preferences for usage statistics collection etc
     * @param showInstallSdkPage show page to install SDK's
     * @param showUsageOptinPage show page to get user consent for usage data collection
*/
    public WelcomeWizard(DdmsPreferenceStore store, boolean showInstallSdkPage,
            boolean showUsageOptinPage) {
mStore = store;
        mShowWelcomePage = showInstallSdkPage;
        mShowUsagePage = showUsageOptinPage;

setWindowTitle("Welcome to Android Development");
ImageDescriptor image = AdtPlugin.getImageDescriptor("icons/android-64.png"); //$NON-NLS-1$
//Synthetic comment -- @@ -59,13 +68,15 @@

@Override
public void addPages() {
        if (mShowWelcomePage) {
            mWelcomePage = new WelcomeWizardPage();
            addPage(mWelcomePage);
        }

// It's possible that the user has already run the command line tools
// such as ddms and has agreed to usage statistics collection, but has never
// run ADT which is why the wizard was opened. No need to ask again.
        if (mShowUsagePage && !mStore.hasPingId()) {
mUsagePage = new UsagePermissionPage();
addPage(mUsagePage);
}
//Synthetic comment -- @@ -96,37 +107,40 @@
store.setPingOptIn(isUsageCollectionApproved);
}

        if (mWelcomePage != null) {
            // Read out wizard settings immediately; we will perform the actual work
            // after the wizard window has been taken down and it's too late to read the
            // settings then
            final File path = mWelcomePage.getPath();
            final boolean installCommon = mWelcomePage.isInstallCommon();
            final boolean installLatest = mWelcomePage.isInstallLatest();
            final boolean createNew = mWelcomePage.isCreateNew();

            // Perform installation asynchronously since it takes a while.
            getShell().getDisplay().asyncExec(new Runnable() {
                @Override
                public void run() {
                    if (createNew) {
                        try {
                            Set<Integer> apiLevels = new HashSet<Integer>();
                            if (installCommon) {
                                apiLevels.add(8);
                            }
                            if (installLatest) {
                                apiLevels.add(AdtUpdateDialog.USE_MAX_REMOTE_API_LEVEL);
                            }
                            installSdk(path, apiLevels);
                        } catch (Exception e) {
                            AdtPlugin.logAndPrintError(e, "ADT Welcome Wizard",
                                    "Installation failed");
}
}

                    // Set SDK path after installation since this will trigger a SDK refresh.
                    AdtPrefs.getPrefs().setSdkLocation(path);
                }
            });
        }

// The wizard always succeeds, even if installation fails or is aborted
return true;







