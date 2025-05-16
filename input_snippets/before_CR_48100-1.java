
//<Beginning of snippet n. 0>


* @return true if successful, false otherwise
*/
private boolean tryAddSingleRange(int startId, int endId, boolean selected) {
        startUpdate();
addRange(startId, endId, selected);
return finishUpdate();
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

/**

//<End of snippet n. 1>








