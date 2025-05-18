//<Beginning of snippet n. 0>
private ToolItem mTBShowThreadUpdates;
private ToolItem mTBShowHeapUpdates;
private ToolItem mTBHalt;
private ToolItem mTBCauseGc;
private ToolItem mTBDumpHprof;
private ToolItem mTBProfiling;

private void updateToolItemsState() {
    if (mCurrentClient != null && mCurrentClient.hasUserPermissions()) {
        mTBShowThreadUpdates.setEnabled(true);
        mTBShowHeapUpdates.setSelection(mCurrentClient.isHeapUpdateEnabled());
        mTBShowHeapUpdates.setEnabled(mCurrentClient.isHeapUpdateAllowed() && mCurrentClient.hasUserPermissionsForHeapUpdate());
        mTBHalt.setEnabled(true);
        mTBCauseGc.setEnabled(true);
    } else {
        mTBShowThreadUpdates.setEnabled(false);
        mTBShowHeapUpdates.setSelection(false);
        mTBShowHeapUpdates.setEnabled(false);
        mTBHalt.setEnabled(false);
        mTBCauseGc.setEnabled(false);
    }
}

updateToolItemsState();
//<End of snippet n. 0>

//<Beginning of snippet n. 4>
public void toggleMethodProfiling() {
    if (mCurrentClient != null) {
        try {
            mCurrentClient.toggleMethodProfiling();
        } catch (Exception e) {
            System.err.println("Failed to toggle method profiling: " + e.getMessage());
            // User-friendly feedback can be provided here.
            System.out.println("An error occurred while toggling method profiling. Please try again.");
        }
    } else {
        System.out.println("No current client available to toggle method profiling.");
    }
}
//<End of snippet n. 4>