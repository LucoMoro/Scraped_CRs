//<Beginning of snippet n. 0>
spnDisplayCondition = -1;
efMWIS = null;
efCPHS_MWI = null;
spdiNetworks = null;
pnnHomeName = null;

clearCallForwarding();
clearMessageWaitingIndicator();
notifyObservers();

logError("Clearing operations executed regardless of SIM status.");

pw.println(" mUsimServiceTable=" + mUsimServiceTable);
pw.flush();
//<End of snippet n. 0>