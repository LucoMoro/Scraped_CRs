//<Beginning of snippet n. 0>
private ToolItem mTBShowThreadUpdates;
private ToolItem mTBShowHeapUpdates;
private ToolItem mTBHalt;
private ToolItem mTBCauseGc;
private ToolItem mTBDumpHprof;
private ToolItem mTBProfiling;

private void updateToolItems() {
    if (mCurrentClient == null) {
        setToolItemsEnabled(false);
        return;
    }

    boolean hasHeapUpdatePermission = mCurrentClient.hasPermission("heap_update");
    boolean hasProfilingPermission = mCurrentClient.hasPermission("profiling");

    boolean isHeapUpdateEnabled = mCurrentClient.isHeapUpdateEnabled() && hasHeapUpdatePermission;
    boolean isProfilingEnabled = mCurrentClient.isProfilingEnabled() && hasProfilingPermission;

    mTBShowThreadUpdates.setEnabled(true);
    mTBShowHeapUpdates.setSelection(isHeapUpdateEnabled);
    mTBShowHeapUpdates.setEnabled(isHeapUpdateEnabled);
    mTBHalt.setEnabled(isProfilingEnabled);
    mTBCauseGc.setEnabled(isProfilingEnabled);
    mTBProfiling.setEnabled(isProfilingEnabled);
}

private void setToolItemsEnabled(boolean enabled) {
    mTBShowThreadUpdates.setEnabled(enabled);
    mTBShowHeapUpdates.setEnabled(enabled);
    mTBHalt.setEnabled(enabled);
    mTBCauseGc.setEnabled(enabled);
    mTBProfiling.setEnabled(enabled);
}
//<End of snippet n. 0>

//<Beginning of snippet n. 4>
public void toggleMethodProfiling() {
    if (mCurrentClient != null) {
        try {
            mCurrentClient.toggleMethodProfiling();
        } catch (Exception e) {
            notifyUser("Error during method profiling: " + e.getMessage());
        }
    } else {
        notifyUser("No current client selected.");
    }
}

private void notifyUser(String message) {
    // Logic to display a user-friendly notification (implementation may vary)
    System.out.println(message); // Placeholder for user notification logic
}
//<End of snippet n. 4>