//<Beginning of snippet n. 0>

* @return true if successful, false otherwise
*/
private boolean tryAddSingleRange(int startId, int endId, boolean selected) {
    if (startId > endId) {
        throw new IllegalArgumentException("startId must be less than or equal to endId");
    }
    startUpdate();
    boolean success = addRange(startId, endId, selected);
    return finishUpdate() && success;
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

}

/**
 * Called after {@link #startUpdate} to indicate a range of enabled
 * values.
 * @param startId the first id included in the range
 * @param endId the last id included in the range
*/
protected boolean addRange(int startId, int endId, boolean selected) {
    synchronized (mConfigList) {
        for (SmsBroadcastConfigInfo existing : mConfigList) {
            if ((startId <= existing.getEndId() && endId >= existing.getStartId())) {
                throw new IllegalArgumentException("Range overlaps with existing range");
            }
        }
        mConfigList.add(new SmsBroadcastConfigInfo(startId, endId,
                SMS_CB_CODE_SCHEME_MIN, SMS_CB_CODE_SCHEME_MAX, selected));
    }
    return true;
}

//<End of snippet n. 1>