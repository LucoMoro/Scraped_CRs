/*SDK Manager refactor: remove obsolete UpdaterPage

Change-Id:Ieffc0fd8c88705616197bdb971fc66cb9c7f536c*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterPage.java
deleted file mode 100755
//Synthetic comment -- index f136bfb..0000000

//Synthetic comment -- @@ -1,81 +0,0 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AvdManagerPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AvdManagerPage.java
//Synthetic comment -- index fc8e2bf..c691b77 100755

//Synthetic comment -- @@ -20,7 +20,6 @@
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.devices.DeviceManager.DevicesChangeListener;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.widgets.AvdSelector;
import com.android.sdkuilib.internal.widgets.AvdSelector.DisplayMode;
import com.android.sdkuilib.repository.ISdkChangeListener;
//Synthetic comment -- @@ -40,7 +39,7 @@
* thus composed of the {@link AvdManagerWindowImpl1} (the window shell itself) and this
* page displays the actually list of AVDs and various action buttons.
*/
public class AvdManagerPage extends Composite implements ISdkChangeListener, DevicesChangeListener {

private AvdSelector mAvdSelector;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPage.java
//Synthetic comment -- index d9d1744..566981d 100755

//Synthetic comment -- @@ -27,7 +27,6 @@
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.core.PackageLoader;
import com.android.sdkuilib.internal.repository.core.PackageLoader.ISourceLoadedCallback;
import com.android.sdkuilib.internal.repository.core.PackagesDiffLogic;
//Synthetic comment -- @@ -93,7 +92,7 @@
* remote available packages. This gives an overview of what is installed
* vs what is available and allows the user to update or install packages.
*/
public class PackagesPage extends Composite implements ISdkChangeListener {

public  static final String ICON_CAT_OTHER      = "pkgcat_other_16.png";    //$NON-NLS-1$
public  static final String ICON_CAT_PLATFORM   = "pkgcat_16.png";          //$NON-NLS-1$







