/*Add menu to SdkMan2. Experimental. Do not submit.

Change-Id:I404d9ddca03c866ba5d5ea14085363060b768796*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java
//Synthetic comment -- index 83a86b2..3830eb7 100755

//Synthetic comment -- @@ -20,7 +20,6 @@
import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.tasks.ProgressView;
import com.android.sdkuilib.internal.tasks.ProgressViewFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;
//Synthetic comment -- @@ -42,6 +41,8 @@
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

//Synthetic comment -- @@ -72,6 +73,9 @@
private Label mStatusText;
private ImgDisabledButton mButtonStop;
private ToggleButton mButtonDetails;
    private Menu menu;
    private Menu menu_1;
    private Menu menu_2;

/**
* Creates a new window. Caller must call open(), which will block.
//Synthetic comment -- @@ -169,6 +173,62 @@
onToggleDetails();
}
});

        menu = new Menu(mShell, SWT.BAR);
        mShell.setMenuBar(menu);

        MenuItem menuPackages = new MenuItem(menu, SWT.CASCADE);
        menuPackages.setText("Packages");

        menu_2 = new Menu(menuPackages);
        menuPackages.setMenu(menu_2);

        MenuItem ShowUpdatesnew = new MenuItem(menu_2, SWT.NONE);
        ShowUpdatesnew.setText("Show Updates/New Packages");

        MenuItem ShowInstalled = new MenuItem(menu_2, SWT.NONE);
        ShowInstalled.setText("Show Installed Packages");

        MenuItem ShowObsoletePackages = new MenuItem(menu_2, SWT.NONE);
        ShowObsoletePackages.setText("Show Obsolete Packages");

        MenuItem ShowArchives = new MenuItem(menu_2, SWT.NONE);
        ShowArchives.setText("Show Archives");

        new MenuItem(menu_2, SWT.SEPARATOR);

        MenuItem SortByApi = new MenuItem(menu_2, SWT.NONE);
        SortByApi.setText("Sort by API Level");

        MenuItem SortBySource = new MenuItem(menu_2, SWT.NONE);
        SortBySource.setText("Sort by Source");

        new MenuItem(menu_2, SWT.SEPARATOR);

        MenuItem Reload = new MenuItem(menu_2, SWT.NONE);
        Reload.setText("Reload");

        MenuItem menuWindow = new MenuItem(menu, SWT.CASCADE);
        menuWindow.setText("Window");

        menu_1 = new Menu(menuWindow);
        menuWindow.setMenu(menu_1);

        MenuItem ManageAvds = new MenuItem(menu_1, SWT.NONE);
        ManageAvds.setText("Manage AVDs...");

        MenuItem ManageSources = new MenuItem(menu_1, SWT.NONE);
        ManageSources.setText("Manage Sources...");

        new MenuItem(menu_1, SWT.SEPARATOR);

        MenuItem Preferences = new MenuItem(menu_1, SWT.NONE);
        Preferences.setText("Preferences...");

        MenuItem File = new MenuItem(menu_1, SWT.NONE);
        File.setText("About...");


}

private Image getImage(String filename) {







