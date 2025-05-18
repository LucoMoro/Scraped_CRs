//<Beginning of snippet n. 0>
/**
 * @return a list of error messages for any ranges that couldn't be added
 */
private List<String> tryAddMultipleRanges(List<int[]> ranges, boolean selected) {
    List<String> errorMessages = new ArrayList<>();
    List<int[]> validRanges = new ArrayList<>();

    for (int[] range : ranges) {
        int startId = range[0];
        int endId = range[1];
        if (startId > endId) {
            errorMessages.add("Invalid range: startId " + startId + " is greater than endId " + endId);
            continue;
        }
        boolean overlapFound = false;
        for (int[] existingRange : validRanges) {
            if (overlaps(existingRange, startId, endId)) {
                errorMessages.add("Range " + startId + " to " + endId + " overlaps with existing range: " 
                                  + existingRange[0] + " to " + existingRange[1]);
                overlapFound = true;
                break;
            }
        }
        if (!overlapFound) {
            validRanges.add(range);
        }
    }

    if (!errorMessages.isEmpty()) {
        return errorMessages;
    }
    
    synchronized (mConfigList) {
        for (int[] range : validRanges) {
            addRange(range[0], range[1], selected);
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

private boolean overlaps(int[] existing, int startId, int endId) {
    return (existing[0] <= endId && existing[1] >= startId);
}

//<End of snippet n. 1>