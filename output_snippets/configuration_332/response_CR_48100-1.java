//<Beginning of snippet n. 0>
/**
 * @return true if successful, false otherwise
 */
private boolean tryAddSingleRange(int startId, int endId, boolean selected) {
    startUpdate();
    boolean result = addRange(new int[][]{{startId, endId}}, selected);
    return finishUpdate() && result;
}
//<End of snippet n. 0>


//<Beginning of snippet n. 1>
/**
 * Called after {@link #startUpdate} to indicate a range of enabled
 * values.
 * @param ranges an array of start and end id pairs
 */
protected boolean addRange(int[][] ranges, boolean selected) {
    synchronized (mConfigList) {
        for (int[] range : ranges) {
            int startId = range[0];
            int endId = range[1];

            if (startId > endId) {
                return false;
            }
            for (SmsBroadcastConfigInfo existingRange : mConfigList) {
                if (startId <= existingRange.getEndId() && endId >= existingRange.getStartId()) {
                    return false;
                }
            }
            mConfigList.add(new SmsBroadcastConfigInfo(startId, endId,
                    SMS_CB_CODE_SCHEME_MIN, SMS_CB_CODE_SCHEME_MAX, selected));
        }
    }
    return true;
}
//<End of snippet n. 1>