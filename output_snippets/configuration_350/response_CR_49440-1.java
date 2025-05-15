//<Beginning of snippet n. 0>

spnDisplayCondition = -1;
clearIndicatorsAndNotify();

private void clearIndicatorsAndNotify() {
    efMWIS = ""; // Clear the Message Waiting Indicator
    efCPHS_MWI = ""; // Clear the Call Forwarding Indicator
    notifyUIComponents();
}

private void notifyUIComponents() {
    try {
        // Notify UI to update the Call Forwarding and Message Waiting icons
        updateCallForwardingIcon();
        updateMessageWaitingIcon();
    } catch (Exception e) {
        // Handle notification failure gracefully
        System.err.println("Failed to send notification: " + e.getMessage());
    }
}

// Assuming these methods exist to update the UI components
private void updateCallForwardingIcon() {
    // Logic to update Call Forwarding icon
}

private void updateMessageWaitingIcon() {
    // Logic to update Message Waiting icon
}

pw.println(" mUsimServiceTable=" + mUsimServiceTable);
pw.flush();
//<End of snippet n. 0>