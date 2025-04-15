/*Force device list updates in the GLE to happen on the UI thread

Change-Id:Ia9624f02c0ca62891b2c35c7d6bfe78130141e1b*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 899e31e..b5869ce 100644

//Synthetic comment -- @@ -69,6 +69,7 @@
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.devices.State;
import com.android.sdklib.devices.DeviceManager.DevicesChangeListener;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.repository.PkgProps;
//Synthetic comment -- @@ -97,6 +98,7 @@
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
//Synthetic comment -- @@ -145,7 +147,8 @@
* - Target reload. This is when the target used by the project is the edited file has finished<br>
*   loading.<br>
*/
public class ConfigurationComposite extends Composite 
        implements SelectionListener, DevicesChangeListener {
public static final String ATTR_CONTEXT = "context";          //$NON-NLS-1$
private static final String ICON_SQUARE = "square";           //$NON-NLS-1$
private static final String ICON_LANDSCAPE = "landscape";     //$NON-NLS-1$
//Synthetic comment -- @@ -2669,13 +2672,7 @@
if (sdk != null) {
mDeviceList = sdk.getDevices();
DeviceManager manager = sdk.getDeviceManager();
            manager.registerListener(this);
} else {
mDeviceList = new ArrayList<Device>();
}
//Synthetic comment -- @@ -2690,6 +2687,18 @@
selectDevice(null);
}
}
    
    @Override
    public void onDevicesChange() {
        final Sdk sdk = Sdk.getCurrent();
        mDeviceList = sdk.getDevices();
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                addDeviceMenuListener(mDeviceCombo);
            }
        });
    }

Image getOrientationIcon(ScreenOrientation orientation, boolean flip) {
IconFactory icons = IconFactory.getInstance();







