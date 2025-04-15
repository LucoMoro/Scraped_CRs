/*Plug in about/settings for SDK Manager 2.

Refactor SDK Updater pages.

Change-Id:I84357e34c66e8976843cbfb4a5413ac28b899279*/
//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index 5ac5f4c..d3e3235 100644

//Synthetic comment -- @@ -41,6 +41,7 @@
import com.android.sdkmanager.internal.repository.SettingsPage;
import com.android.sdkuilib.internal.repository.PackagesPage;
import com.android.sdkuilib.internal.repository.UpdateNoWindow;
import com.android.sdkuilib.internal.widgets.MessageBoxLog;
import com.android.sdkuilib.repository.IUpdaterWindow;
import com.android.sdkuilib.repository.UpdaterWindow;
//Synthetic comment -- @@ -315,8 +316,8 @@
null /* parentShell */,
errorLogger,
mOsSdkFolder);
            window.registerPage("Settings", SettingsPage.class);
            window.registerPage("About", AboutPage.class);
if (autoUpdate) {
window.setInitialPage(PackagesPage.class);
window.setRequestAutoUpdate(true);








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/internal/repository/AboutPage.java b/sdkmanager/app/src/com/android/sdkmanager/internal/repository/AboutPage.java
//Synthetic comment -- index 4c72e1e..ac4a1ea 100755

//Synthetic comment -- @@ -22,6 +22,8 @@
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdkmanager.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
//Synthetic comment -- @@ -36,7 +38,7 @@
import java.io.InputStream;
import java.util.Properties;

public class AboutPage extends Composite {

private Label mLabel;

//Synthetic comment -- @@ -44,14 +46,19 @@
* Create the composite.
* @param parent The parent of the composite.
*/
    public AboutPage(Composite parent) {
        super(parent, SWT.BORDER);

createContents(this);

postCreate();  //$hide$
}

private void createContents(Composite parent) {
parent.setLayout(new GridLayout(2, false));









//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/internal/repository/SettingsPage.java b/sdkmanager/app/src/com/android/sdkmanager/internal/repository/SettingsPage.java
//Synthetic comment -- index eac7515..3a92870 100755

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.sdkmanager.internal.repository;

import com.android.sdkuilib.internal.repository.ISettingsPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
//Synthetic comment -- @@ -34,7 +35,7 @@
import java.util.Properties;


public class SettingsPage extends Composite implements ISettingsPage {

// data members
private SettingsChangedCallback mSettingsChangedCallback;
//Synthetic comment -- @@ -66,10 +67,20 @@
* Create the composite.
* @param parent The parent of the composite.
*/
    public SettingsPage(Composite parent) {
        super(parent, SWT.BORDER);

createContents(this);

mProxySettingsGroup = new Group(this, SWT.NONE);
mProxySettingsGroup.setText("Proxy Settings");
//Synthetic comment -- @@ -118,12 +129,6 @@
mAskAdbRestartCheck.setToolTipText("When checked, the user will be asked for permission " +
"to restart ADB after updating an addon-on package or a tool package.");
mAskAdbRestartCheck.addSelectionListener(mApplyOnSelected);

        postCreate();  //$hide$
    }

    private void createContents(Composite parent) {
        parent.setLayout(new GridLayout(1, false));
}

@Override
//Synthetic comment -- @@ -172,7 +177,8 @@
}

/**
     * Callback invoked when user presses the "Save and Apply" button.
* Notify the application that settings have changed.
*/
private void applyNewSettings() {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AddonSitesDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AddonSitesDialog.java
//Synthetic comment -- index d9d4111..7927ec6 100755

//Synthetic comment -- @@ -16,10 +16,12 @@

package com.android.sdkuilib.internal.repository;

import com.android.sdklib.internal.repository.SdkAddonSource;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.SdkSourceCategory;
import com.android.sdklib.internal.repository.SdkSources;
import com.android.sdkuilib.ui.SwtBaseDialog;

import org.eclipse.jface.dialogs.IInputValidator;
//Synthetic comment -- @@ -86,10 +88,11 @@
Shell shell = getShell();
shell.setMinimumSize(new Point(450, 300));
shell.setSize(450, 300);

        GridLayout gl_shell = new GridLayout();
        gl_shell.numColumns = 2;
        shell.setLayout(gl_shell);

mlabel = new Label(shell, SWT.NONE);
mlabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
//Synthetic comment -- @@ -167,6 +170,25 @@
}

/**
* Adds a listener to adjust the column width when the parent is resized.
*/
private void adjustColumnsWidth(final Table table, final TableColumn column0) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AvdManagerPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AvdManagerPage.java
//Synthetic comment -- index 7c78983..b2062f0 100755

//Synthetic comment -- @@ -27,7 +27,7 @@
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class AvdManagerPage extends Composite implements ISdkChangeListener {

private AvdSelector mAvdSelector;

//Synthetic comment -- @@ -38,8 +38,8 @@
* @param parent The parent of the composite.
* @param updaterData An instance of {@link UpdaterData}.
*/
    public AvdManagerPage(Composite parent, UpdaterData updaterData) {
        super(parent, SWT.BORDER);

mUpdaterData = updaterData;
mUpdaterData.addListeners(this);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/LocalPackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/LocalPackagesPage.java
//Synthetic comment -- index d2824ea..d7ac4aa 100755

//Synthetic comment -- @@ -45,7 +45,7 @@
/**
* Page that displays all locally installed packages from the current SDK.
*/
public class LocalPackagesPage extends Composite implements ISdkChangeListener {

private final UpdaterData mUpdaterData;

//Synthetic comment -- @@ -68,8 +68,8 @@
* @param parent The parent of the composite.
* @param updaterData An instance of {@link UpdaterData}.
*/
    public LocalPackagesPage(Composite parent, UpdaterData updaterData) {
        super(parent, SWT.BORDER);

mUpdaterData = updaterData;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java
//Synthetic comment -- index 708c2d6..b761510 100755

//Synthetic comment -- @@ -82,7 +82,7 @@
* remote available packages. This gives an overview of what is installed
* vs what is available and allows the user to update or install packages.
*/
public class PackagesPage extends Composite
implements ISdkChangeListener, IPageListener {

private static final String ICON_CAT_OTHER      = "pkgcat_other_16.png";    //$NON-NLS-1$
//Synthetic comment -- @@ -150,8 +150,8 @@
private Font mTreeFontItalic;
private TreeColumn mTreeColumnName;

    public PackagesPage(Composite parent, UpdaterData updaterData) {
        super(parent, SWT.NONE);

mUpdaterData = updaterData;
createContents(this);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RemotePackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RemotePackagesPage.java
//Synthetic comment -- index 0a70162..1dc31bc 100755

//Synthetic comment -- @@ -56,7 +56,7 @@
* Page that displays remote repository & add-ons sources and let the user
* select packages for installation.
*/
public class RemotePackagesPage extends Composite implements ISdkChangeListener {

private final UpdaterData mUpdaterData;

//Synthetic comment -- @@ -79,8 +79,8 @@
* @param parent The parent of the composite.
* @param updaterData An instance of {@link UpdaterData}.
*/
    RemotePackagesPage(Composite parent, UpdaterData updaterData) {
        super(parent, SWT.BORDER);

mUpdaterData = updaterData;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java
//Synthetic comment -- index 009e1e5..fde9b28 100755

//Synthetic comment -- @@ -42,6 +42,7 @@

private final Properties mProperties = new Properties();

private ISettingsPage mSettingsPage;

private final UpdaterData mUpdaterData;
//Synthetic comment -- @@ -135,20 +136,26 @@

/**
* Associate the given {@link ISettingsPage} with this {@link SettingsController}.
     *
* This loads the current properties into the setting page UI.
* It then associates the SettingsChanged callback with this controller.
*/
public void setSettingsPage(ISettingsPage settingsPage) {

mSettingsPage = settingsPage;
        mSettingsPage.loadSettings(mProperties);

        settingsPage.setOnSettingsChanged(new ISettingsPage.SettingsChangedCallback() {
            public void onSettingsChanged(ISettingsPage page) {
                SettingsController.this.onSettingsChanged();
            }
        });
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterPage.java
new file mode 100755
//Synthetic comment -- index 0000000..9064f91

//Synthetic comment -- @@ -0,0 +1,83 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java
//Synthetic comment -- index 7514dea..b148b4e 100755

//Synthetic comment -- @@ -19,10 +19,12 @@

import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.tasks.ProgressTaskFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.IUpdaterWindow;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
//Synthetic comment -- @@ -39,7 +41,6 @@
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
//Synthetic comment -- @@ -61,7 +62,7 @@
private boolean mInternalPageChange;
/** A list of extra pages to instantiate. Each entry is an object array with 2 elements:
*  the string title and the Composite class to instantiate to create the page. */
    private ArrayList<Object[]> mExtraPages;
/** A factory to create progress task dialogs. */
private ProgressTaskFactory mTaskFactory;
/** The initial page to display. If null or not a know class, the first page will be displayed.
//Synthetic comment -- @@ -180,14 +181,17 @@
* <p/>
* All pages must be registered before the call to {@link #open()}.
*
     * @param title The title of the page.
* @param pageClass The {@link Composite}-derived class that will implement the page.
*/
    public void registerPage(String title, Class<? extends Composite> pageClass) {
if (mExtraPages == null) {
            mExtraPages = new ArrayList<Object[]>();
}
        mExtraPages.add(new Object[]{ title, pageClass });
}

/**
//Synthetic comment -- @@ -230,10 +234,10 @@
* displayed in the window.
*/
protected void createPages() {
        mAvdManagerPage = new AvdManagerPage(mPagesRootComposite, mUpdaterData);

        mLocalPackagePage = new LocalPackagesPage(mPagesRootComposite, mUpdaterData);
        mRemotePackagesPage = new RemotePackagesPage(mPagesRootComposite, mUpdaterData);

addPage(mAvdManagerPage, "Virtual devices");

//Synthetic comment -- @@ -261,7 +265,7 @@
private void setWindowImage(Shell androidSdkUpdater) {
String imageName = "android_icon_16.png"; //$NON-NLS-1$
if (SdkConstants.currentPlatform() == SdkConstants.PLATFORM_DARWIN) {
            imageName = "android_icon_128.png"; //$NON-NLS-1$
}

if (mUpdaterData != null) {
//Synthetic comment -- @@ -327,6 +331,10 @@
* {@link Composite#getData()} field.
*/
protected void addPage(Composite page, String title) {
page.setData(title);
mPages.add(page);
if (mPageList != null) {
//Synthetic comment -- @@ -339,34 +347,20 @@
* using the constructor that takes a single {@link Composite} argument and then adds it
* to the page list.
*/
    @SuppressWarnings("unchecked")
protected void addExtraPages() {
if (mExtraPages == null) {
return;
}

        for (Object[] extraPage : mExtraPages) {
            String title = (String) extraPage[0];
            Class<? extends Composite> clazz = (Class<? extends Composite>) extraPage[1];

            // We want the constructor that takes a single Composite as parameter
            Constructor<? extends Composite> cons;
            try {
                cons = clazz.getConstructor(new Class<?>[] { Composite.class });
                Composite instance = cons.newInstance(new Object[] { mPagesRootComposite });
                addPage(instance, title);

            } catch (NoSuchMethodException e) {
                // There is no such constructor.
                mUpdaterData.getSdkLog().error(e,
                        "Failed to add extra page %1$s. Constructor args must be (Composite parent).",  //$NON-NLS-1$
                        clazz.getSimpleName());

            } catch (Exception e) {
                // Log this instead of crashing the whole app.
                mUpdaterData.getSdkLog().error(e,
                        "Failed to add extra page %1$s.",  //$NON-NLS-1$
                        clazz.getSimpleName());
}
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java
//Synthetic comment -- index 6b3cd66..43ec7cc 100755

//Synthetic comment -- @@ -22,13 +22,17 @@
import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdkuilib.internal.repository.PackagesPage.MenuAction;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.tasks.ProgressView;
import com.android.sdkuilib.internal.tasks.ProgressViewFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.IUpdaterWindow;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
//Synthetic comment -- @@ -36,10 +40,13 @@
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
//Synthetic comment -- @@ -66,7 +73,7 @@
private final UpdaterData mUpdaterData;
/** A list of extra pages to instantiate. Each entry is an object array with 2 elements:
*  the string title and the Composite class to instantiate to create the page. */
    private ArrayList<Object[]> mExtraPages;
/** Sets whether the auto-update wizard will be shown when opening the window. */
private boolean mRequestAutoUpdate;

//Synthetic comment -- @@ -78,6 +85,7 @@
private Label mStatusText;
private ImgDisabledButton mButtonStop;
private ToggleButton mButtonDetails;

/**
* Creates a new window. Caller must call open(), which will block.
//Synthetic comment -- @@ -141,7 +149,7 @@

private void createContents() {

        mPkgPage = new PackagesPage(mShell, mUpdaterData);
mPkgPage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

Composite composite1 = new Composite(mShell, SWT.NONE);
//Synthetic comment -- @@ -259,13 +267,11 @@

MenuBarEnhancer.setupMenu(APP_NAME, menuTools, new IMenuBarCallback() {
public void onPreferencesMenuSelected() {
                // TODO: plug settings page here
                MessageDialog.openInformation(mShell, "test", "on prefs");
}

public void onAboutMenuSelected() {
                // TODO: plug about page here
                MessageDialog.openInformation(mShell, "test", "on about");
}

public void printError(String format, Object... args) {
//Synthetic comment -- @@ -304,14 +310,17 @@
* <p/>
* All pages must be registered before the call to {@link #open()}.
*
     * @param title The title of the page.
* @param pageClass The {@link Composite}-derived class that will implement the page.
*/
    public void registerPage(String title, Class<? extends Composite> pageClass) {
if (mExtraPages == null) {
            mExtraPages = new ArrayList<Object[]>();
}
        mExtraPages.add(new Object[]{ title, pageClass });
}

/**
//Synthetic comment -- @@ -397,8 +406,10 @@

/**
* Creates the icon of the window shell.
*/
    private void setWindowImage(Shell androidSdkUpdater) {
String imageName = "android_icon_16.png"; //$NON-NLS-1$
if (SdkConstants.currentPlatform() == SdkConstants.PLATFORM_DARWIN) {
imageName = "android_icon_128.png"; //$NON-NLS-1$
//Synthetic comment -- @@ -407,7 +418,7 @@
if (mUpdaterData != null) {
ImageFactory imgFactory = mUpdaterData.getImageFactory();
if (imgFactory != null) {
                mShell.setImage(imgFactory.getImageByName(imageName));
}
}
}
//Synthetic comment -- @@ -445,12 +456,9 @@
* and use it to load and apply these settings.
*/
private void initializeSettings() {
        SettingsController c = mUpdaterData.getSettingsController();
        c.loadSettings();
        c.applySettings();

        // TODO give access to a settings dialog somehow (+about dialog)
        // TODO c.setSettingsPage(settingsPage);
}

private void onToggleDetails() {
//Synthetic comment -- @@ -461,6 +469,26 @@
// TODO
}

// End of hiding from SWT Designer
//$hide<<$

//Synthetic comment -- @@ -583,4 +611,77 @@
return (isDisposed() || !isEnabled()) ? 1 : 0;
}
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/IUpdaterWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/IUpdaterWindow.java
//Synthetic comment -- index a771e53..def5de0 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdkuilib.repository;

import org.eclipse.swt.widgets.Composite;

public interface IUpdaterWindow {
//Synthetic comment -- @@ -28,11 +30,11 @@
* <p/>
* All pages must be registered before the call to {@link #open()}.
*
     * @param title The title of the page.
* @param pageClass The {@link Composite}-derived class that will implement the page.
*/
    public abstract void registerPage(String title,
            Class<? extends Composite> pageClass);

/**
* Indicate the initial page that should be selected when the window opens.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java
//Synthetic comment -- index d49b072..0fd0db2 100755

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.sdkuilib.repository;

import com.android.sdklib.ISdkLog;
import com.android.sdkuilib.internal.repository.UpdaterWindowImpl;
import com.android.sdkuilib.internal.repository.UpdaterWindowImpl2;

//Synthetic comment -- @@ -57,11 +58,12 @@
* <p/>
* All pages must be registered before the call to {@link #open()}.
*
     * @param title The title of the page.
* @param pageClass The {@link Composite}-derived class that will implement the page.
*/
    public void registerPage(String title, Class<? extends Composite> pageClass) {
        mWindow.registerPage(title, pageClass);
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/ui/SwtBaseDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/ui/SwtBaseDialog.java
//Synthetic comment -- index 524c7b5..f731baf 100755

//Synthetic comment -- @@ -70,11 +70,13 @@
* Create the dialog.
*
* @param parent The parent's shell
     * @param title The dialog title. Must not be null.
*/
public SwtBaseDialog(Shell parent, int swtStyle, String title) {
super(parent, swtStyle);
        setText(title);
}

/**
//Synthetic comment -- @@ -110,7 +112,9 @@
mShell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
mShell.setMinimumSize(new Point(450, 300));
mShell.setSize(450, 300);
        mShell.setText(getText());
mShell.addDisposeListener(new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
saveSize();







