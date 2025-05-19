
//<Beginning of snippet n. 0>


if (DBG) {
log("applyNewState(" + apnContext.getApnType() + ", " + enabled +
"(" + apnContext.isEnabled() + "), " + met + "(" +
                    apnContext.getDependencyMet() +"))");
}
        if (apnContext.isReady()) {
if (enabled && met) return;
if (!enabled) {
apnContext.setReason(Phone.REASON_DATA_DISABLED);

//<End of snippet n. 0>








