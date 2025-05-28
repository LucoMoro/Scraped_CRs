
//<Beginning of snippet n. 0>


private ToolItem mTBShowThreadUpdates;
private ToolItem mTBShowHeapUpdates;
private ToolItem mTBHalt;
private ToolItem mTBCauseGc;
private ToolItem mTBDumpHprof;
private ToolItem mTBProfiling;
}
});

new ToolItem(toolBar, SWT.SEPARATOR);

// add "kill VM" button; need to make this visually distinct from
mTBShowThreadUpdates.setEnabled(true);
mTBShowHeapUpdates.setSelection(mCurrentClient.isHeapUpdateEnabled());
mTBShowHeapUpdates.setEnabled(true);
mTBHalt.setEnabled(true);
mTBCauseGc.setEnabled(true);

mTBShowThreadUpdates.setEnabled(false);
mTBShowHeapUpdates.setSelection(false);
mTBShowHeapUpdates.setEnabled(false);
mTBHalt.setEnabled(false);
mTBCauseGc.setEnabled(false);


//<End of snippet n. 0>










//<Beginning of snippet n. 1>


HandleWait.register(monitorThread);
HandleProfiling.register(monitorThread);
HandleNativeHeap.register(monitorThread);
}

/**

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


public final static String FEATURE_OPENGL_TRACING = "opengl-tracing"; //$NON-NLS-1$

/**
* String for feature allowing to dump hprof files
* @see #hasFeature(String)
*/

//<End of snippet n. 2>










//<Beginning of snippet n. 3>

new file mode 100644


//<End of snippet n. 3>










//<Beginning of snippet n. 4>


import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.ddmlib.Client;
import com.android.ddmlib.ClientData;
import com.android.ddmlib.ClientData.DebuggerStatus;
import com.android.ddmlib.DdmPreferences;
import com.android.ddmlib.IDevice;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import java.util.ArrayList;
import java.util.Locale;

}
}

public void toggleMethodProfiling() {
if (mCurrentClient != null) {
mCurrentClient.toggleMethodProfiling();

//<End of snippet n. 4>








