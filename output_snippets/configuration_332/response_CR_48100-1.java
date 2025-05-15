//<Beginning of snippet n. 0>
/**
 * @return true if successful, false otherwise
 */
private boolean tryAddSingleRange(List<Range> ranges) {
    startUpdate();
    for (Range range : ranges) {
        if (!addRange(range.startId, range.endId, range.selected)) {
            finishUpdate();
            return false;
        }
    }
    return finishUpdate();
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
/**
 * Called after {@link #startUpdate} to indicate a range of enabled
 * values.
 * @param startId the first id included in the range
 * @param endId the last id included in the range
 */
protected boolean addRange(int startId, int endId, boolean selected) {
    if (isRangeValid(startId, endId)) {
        mConfigList.add(new SmsBroadcastConfigInfo(startId, endId,
                SMS_CB_CODE_SCHEME_MIN, SMS_CB_CODE_SCHEME_MAX, selected));
        return true;
    }
    return false;
}

private boolean isRangeValid(int startId, int endId) {
    for (SmsBroadcastConfigInfo config : mConfigList) {
        if ((startId <= config.getEndId() && endId >= config.getStartId())) {
            return false; // Overlapping range detected
        }
    }
    return startId <= endId; // Check for valid range
}
//<End of snippet n. 1>