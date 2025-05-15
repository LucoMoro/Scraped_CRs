//<Beginning of snippet n. 0>
private ToolItem mTBShowThreadUpdates;
private ToolItem mTBShowHeapUpdates;
private ToolItem mTBHalt;
private ToolItem mTBCauseGc;
private ToolItem mTBDumpHprof;
private ToolItem mTBProfiling;

private void updateToolItemStates() {
    if (mCurrentClient != null) {
        boolean isHeapUpdateEnabled = mCurrentClient.isHeapUpdateEnabled();
        mTBShowThreadUpdates.setEnabled(mCurrentClient.isThreadUpdatesEnabled());
        mTBShowHeapUpdates.setEnabled(isHeapUpdateEnabled);
        mTBHalt.setEnabled(mCurrentClient.isHaltEnabled());
        mTBCauseGc.setEnabled(mCurrentClient.isCauseGcEnabled());
        
        // Update the selection state accordingly
        mTBShowHeapUpdates.setSelection(isHeapUpdateEnabled);
    } else {
        mTBShowThreadUpdates.setEnabled(false);
        mTBShowHeapUpdates.setEnabled(false);
        mTBHalt.setEnabled(false);
        mTBCauseGc.setEnabled(false);
    }
}

// Initial state setup
updateToolItemStates();
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
HandleWait.register(monitorThread);
HandleProfiling.register(monitorThread);
HandleNativeHeap.register(monitorThread);
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
public void toggleMethodProfiling() {
    if (mCurrentClient != null) {
        try {
            mCurrentClient.toggleMethodProfiling();
        } catch (Exception e) {
            showError("Failed to toggle method profiling: " + e.getMessage());
        }
        updateToolItemStates();
    } else {
        showError("No active client to toggle profiling.");
    }
}

// Simple method for showing error messages
private void showError(String message) {
    // Implement user feedback logic (e.g., dialog box, console log)
}
//<End of snippet n. 4>