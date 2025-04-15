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

        if (isFirstTime()) {
            showWelcomeWizard();
            // Usage statistics are sent after the wizard has run asynchronously (provided the
            // user opted in)
        } else if (mStore.isPingOptIn()) {
sendUsageStats();
}

//Synthetic comment -- @@ -122,13 +125,6 @@
}

private boolean isFirstTime() {
        // If we already have a known SDK location in our workspace then we know this
        // is not the first time this user is running ADT.
        String osSdkFolder = AdtPrefs.getPrefs().getOsSdkFolder();
        if (osSdkFolder != null && osSdkFolder.length() > 0) {
            return false;
        }

for (int i = 0; i < 2; i++) {
String osSdkPath = null;

//Synthetic comment -- @@ -258,14 +254,16 @@
});
}

    private void showWelcomeWizard() {
final IWorkbench workbench = PlatformUI.getWorkbench();
workbench.getDisplay().asyncExec(new Runnable() {
@Override
public void run() {
IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
if (window != null) {
                    WelcomeWizard wizard = new WelcomeWizard(mStore);
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

/**
* Creates a new {@link WelcomeWizard}
*
* @param store preferences for usage statistics collection etc
*/
    public WelcomeWizard(DdmsPreferenceStore store) {
mStore = store;

setWindowTitle("Welcome to Android Development");
ImageDescriptor image = AdtPlugin.getImageDescriptor("icons/android-64.png"); //$NON-NLS-1$
//Synthetic comment -- @@ -59,13 +68,15 @@

@Override
public void addPages() {
        mWelcomePage = new WelcomeWizardPage();
        addPage(mWelcomePage);

// It's possible that the user has already run the command line tools
// such as ddms and has agreed to usage statistics collection, but has never
// run ADT which is why the wizard was opened. No need to ask again.
        if (!mStore.isPingOptIn()) {
mUsagePage = new UsagePermissionPage();
addPage(mUsagePage);
}
//Synthetic comment -- @@ -96,37 +107,40 @@
store.setPingOptIn(isUsageCollectionApproved);
}

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
                        AdtPlugin.logAndPrintError(e, "ADT Welcome Wizard", "Installation failed");
}
                }

                // Set SDK path after installation since this will trigger a SDK refresh.
                AdtPrefs.getPrefs().setSdkLocation(path);
            }
        });

// The wizard always succeeds, even if installation fails or is aborted
return true;







