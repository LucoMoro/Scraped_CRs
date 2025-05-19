//<Beginning of snippet n. 0>

package com.android.ide.eclipse.adt.internal.wizards.actions;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.build.DexWrapper;
import com.android.ide.eclipse.adt.internal.sdk.AdtConsoleSdkLog;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.UpdaterWindow;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
* Delegate for the toolbar/menu action "Android AVD Manager".
*/
public class AvdManagerAction implements IWorkbenchWindowActionDelegate, IObjectActionDelegate {

    private UpdaterWindow window;

    public void dispose() {
        if (window != null) {
            window.close();
            window = null;
        }
    }

    public void run(IAction action) {
        Sdk sdk = Sdk.getCurrent();
        if (sdk != null) {
            if (window == null || !window.isVisible()) {
                if (!ProgressDialog.isActive()) {
                    window = new UpdaterWindow(
                            getShell(),
                            new AdtConsoleSdkLog(),
                            sdk.getSdkLocation());
                    window.open();
                }
            }
        } else {
            AdtPlugin.displayError("Invalid SDK", "Please ensure the SDK location is set correctly and the specified path exists.");
        }
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        // nothing to do.
    }

    private Shell getShell() {
        IWorkbenchWindow workbenchWindow = AdtPlugin.getWorkbench().getActiveWorkbenchWindow();
        return (workbenchWindow != null) ? workbenchWindow.getShell() : null;
    }
}

//<End of snippet n. 0>