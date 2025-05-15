
//<Beginning of snippet n. 0>


protected void onEnableNewApn() {
// TODO:  To support simultaneous PDP contexts, this should really only call
// cleanUpConnection if it needs to free up a PdpConnection.
        cleanUpConnection(true, Phone.REASON_APN_SWITCHED);
}

protected void onTrySetupData(String reason) {

//<End of snippet n. 0>








