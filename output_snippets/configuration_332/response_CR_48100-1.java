//<Beginning of snippet n. 0>
/**
 * @return a list of error messages for any ranges that couldn't be added
 */
private List<String> tryAddMultipleRanges(List<int[]> ranges, boolean selected) {
    List<String> errorMessages = new ArrayList<>();
    synchronized (mConfigList) {
        for (int[] range : ranges) {
            int startId = range[0];
            int endId = range[1];
            if (startId > endId) {
                errorMessages.add("Invalid range: startId " + startId + " is greater than endId " + endId);
                continue;
            }
            boolean overlapFound = false;
            for (SmsBroadcastConfigInfo existing : mConfigList) {
                if (overlaps(existing, startId, endId)) {
                    errorMessages.add("Range " + startId + " to " + endId + " overlaps with existing range: " 
                                      + existing.startId + " to " + existing.endId);
                    overlapFound = true;
                    break;
                }
            }
            if (!overlapFound) {
                addRange(startId, endId, selected);
            }
        }
    }
    return errorMessages;
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>
/**
 * Called after {@link #startUpdate} to indicate a range of enabled
 * values.
 * @param startId the first id included in the range
 * @param endId the last id included in the range
 */
protected void addRange(int startId, int endId, boolean selected) {
    synchronized (mConfigList) {
        mConfigList.add(new SmsBroadcastConfigInfo(startId, endId,
                SMS_CB_CODE_SCHEME_MIN, SMS_CB_CODE_SCHEME_MAX, selected));
    }
}

private boolean overlaps(SmsBroadcastConfigInfo existing, int startId, int endId) {
    return (existing.startId <= endId && existing.endId >= startId);
}

//<End of snippet n. 1>