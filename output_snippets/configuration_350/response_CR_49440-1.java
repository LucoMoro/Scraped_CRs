//<Beginning of snippet n. 0>
spnDisplayCondition = -1;
clearIndicators();

private void clearIndicators() {
    efMWIS = null;
    efCPHS_MWI = null;
    notifyIndicatorsCleared();
}

private void notifyIndicatorsCleared() {
    // Notify relevant components/UI about the cleared indicators
    NotificationCenter.getInstance().postNotification("IndicatorsCleared");
    // Assuming an event listener is set up
    EventDispatcher.getInstance().notifyObservers("IndicatorsCleared");
}

pw.println(" mUsimServiceTable=" + mUsimServiceTable);
pw.flush();
}
//<End of snippet n. 0>