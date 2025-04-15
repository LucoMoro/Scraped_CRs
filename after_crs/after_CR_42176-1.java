/*Clean up device listeners

The configuration composite listens for device changes,
but the listeners were not unregistered when the layouts
were closed. Also, initDevices() could be called more
than once, which made the same listener be registered more
than once.

Change-Id:Ia679cf1b5b5a394748034277ef002af0107ba3e1*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 9287787..2a0a396 100644

//Synthetic comment -- @@ -68,8 +68,8 @@
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.devices.DeviceManager.DevicesChangeListener;
import com.android.sdklib.devices.State;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.repository.PkgProps;
//Synthetic comment -- @@ -89,6 +89,8 @@
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
//Synthetic comment -- @@ -148,7 +150,7 @@
*   loading.<br>
*/
public class ConfigurationComposite extends Composite
        implements SelectionListener, DevicesChangeListener, DisposeListener {
public static final String ATTR_CONTEXT = "context";          //$NON-NLS-1$
private static final String ICON_SQUARE = "square";           //$NON-NLS-1$
private static final String ICON_LANDSCAPE = "landscape";     //$NON-NLS-1$
//Synthetic comment -- @@ -559,6 +561,8 @@
addTargetMenuListener(mTargetCombo);
addThemeListener(mThemeCombo);
addOrientationMenuListener(mOrientationCombo);

        addDisposeListener(this);
}

private void updateActivity() {
//Synthetic comment -- @@ -568,6 +572,26 @@
}
}

    // ---- Dispose

    @Override
    public void widgetDisposed(DisposeEvent e) {
        dispose();
    }

    @Override
    public void dispose() {
        if (!isDisposed()) {
            super.dispose();

            final Sdk sdk = Sdk.getCurrent();
            if (sdk != null) {
                DeviceManager manager = sdk.getDeviceManager();
                manager.unregisterListener(this);
            }
        }
    }

// ---- Init and reset/reload methods ----

/**
//Synthetic comment -- @@ -2675,6 +2699,8 @@
if (sdk != null) {
mDeviceList = sdk.getDevices();
DeviceManager manager = sdk.getDeviceManager();
            // This method can be called more than once, so avoid duplicate entries
            manager.unregisterListener(this);
manager.registerListener(this);
} else {
mDeviceList = new ArrayList<Device>();
//Synthetic comment -- @@ -2698,7 +2724,9 @@
Display.getDefault().asyncExec(new Runnable() {
@Override
public void run() {
                if (!mDeviceCombo.isDisposed()) {
                    addDeviceMenuListener(mDeviceCombo);
                }
}
});
}







