//<Beginning of snippet n. 0>
/**
 * @return true if successful, false otherwise
 */
private boolean tryAddMultipleRanges(List<int[]> ranges, boolean selected) {
    startUpdate();
    for (int[] range : ranges) {
        if (!isValidRange(range[0], range[1])) {
            finishUpdate();
            return false;
        }
        addRange(range[0], range[1], selected);
    }
    return finishUpdate();
}

private boolean isValidRange(int startId, int endId) {
    if (startId < 0 || endId < 0 || startId > endId) {
        return false;
    }
    for (SmsBroadcastConfigInfo config : mConfigList) {
        if (startId <= config.getEndId() && endId >= config.getStartId()) {
            return false; // Overlapping range
        }
    }
    return true;
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
protected void addRange(int startId, int endId, boolean selected) {
    mConfigList.add(new SmsBroadcastConfigInfo(startId, endId,
            SMS_CB_CODE_SCHEME_MIN, SMS_CB_CODE_SCHEME_MAX, selected));
}
//<End of snippet n. 1>