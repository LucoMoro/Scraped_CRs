//<Beginning of snippet n. 0>
/**
 * @return List of String indicating success or failure with messages for each range
 */
private List<String> tryAddMultipleRanges(List<int[]> ranges, boolean selected) {
    List<String> results = new ArrayList<>();
    List<SmsBroadcastConfigInfo> addedRanges = new ArrayList<>();
    boolean transactionSuccess = true;

    startUpdate();
    try {
        for (int[] range : ranges) {
            int startId = range[0];
            int endId = range[1];
            if (startId >= endId) {
                results.add("Invalid range: [" + startId + ", " + endId + "]");
                transactionSuccess = false;
                continue;
            }
            
            if (!validateRange(startId, endId)) {
                results.add("Overlap detected for range: [" + startId + ", " + endId + "]");
                transactionSuccess = false;
                continue;
            }

            boolean success = addRange(startId, endId, selected);
            if (success) {
                addedRanges.add(new SmsBroadcastConfigInfo(startId, endId, 
                        SMS_CB_CODE_SCHEME_MIN, SMS_CB_CODE_SCHEME_MAX, selected));
                results.add("Range added: [" + startId + ", " + endId + "]");
            } else {
                results.add("Failed to add range: [" + startId + ", " + endId + "]");
                transactionSuccess = false;
            }
        }

        if (!transactionSuccess) {
            rollbackRanges(addedRanges);
        } else {
            finishUpdate();
        }

    } catch (Exception e) {
        rollbackRanges(addedRanges);
        results.add("Error occurred: " + e.getMessage());
    } finally {
        if (transactionSuccess) {
            finishUpdate();
        }
    }
    
    return results;
}

private void rollbackRanges(List<SmsBroadcastConfigInfo> addedRanges) {
    for (SmsBroadcastConfigInfo range : addedRanges) {
        mConfigList.remove(range);
    }
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
    SmsBroadcastConfigInfo newRange = new SmsBroadcastConfigInfo(startId, endId,
            SMS_CB_CODE_SCHEME_MIN, SMS_CB_CODE_SCHEME_MAX, selected);
    mConfigList.add(newRange);
    return true; // Successfully added
}

/**
 * Validate the range to prevent adding overlapping ranges
 * @param startId the starting id of the range
 * @param endId the ending id of the range
 * @return true if the range can be added, false otherwise
 */
private boolean validateRange(int startId, int endId) {
    synchronized (mConfigList) {
        for (SmsBroadcastConfigInfo range : mConfigList) {
            if ((startId >= range.getStartId() && startId <= range.getEndId()) ||
                (endId >= range.getStartId() && endId <= range.getEndId()) ||
                (startId <= range.getStartId() && endId >= range.getEndId()) ||
                (endId == range.getStartId() || startId == range.getEndId())) {
                return false; // Overlap detected
            }
        }
    }
    return true; // No overlap
}
    
//<End of snippet n. 1>