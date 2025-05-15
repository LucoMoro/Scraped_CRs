```
<<Beginning of snippet n. 0>>
spnDisplayCondition = -1;
efMWIS = null;
efCPHS_MWI = null;
spdiNetworks = null;
pnnHomeName = null;

if (isSimRemoved()) {
    resetIndicators();
    notifyCallForwardingChanged();
    notifyMessageWaitingChanged();
}

pw.println(" mUsimServiceTable=" + mUsimServiceTable);
pw.flush();
}

private boolean isSimRemoved() {
    // Implement logic to check if the SIM is removed
}

private void resetIndicators() {
    // Resetting state variables and flags
    spnDisplayCondition = -1;
    efMWIS = null;
    efCPHS_MWI = null;
    spdiNetworks = null;
    pnnHomeName = null;
    // Reset related state flags here
}

private void notifyCallForwardingChanged() {
    try {
        // Implement notification logic for call forwarding
    } catch (Exception e) {
        // Log error
    }
}

private void notifyMessageWaitingChanged() {
    try {
        // Implement notification logic for message waiting
    } catch (Exception e) {
        // Log error
    }
}
<<End of snippet n. 0>>