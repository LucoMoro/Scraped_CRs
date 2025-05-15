
//<Beginning of snippet n. 0>


* @return true if successful, false otherwise
*/
private boolean tryAddSingleRange(int startId, int endId, boolean selected) {
addRange(startId, endId, selected);
return finishUpdate();
}

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


}

/**
         * Called after {@link #startUpdate} to add range(s) of enabled
         * values to the config list.
* @param startId the first id included in the range
* @param endId the last id included in the range
*/
protected void addRange(int startId, int endId, boolean selected) {
            boolean found = false;
            if (!mConfigList.isEmpty()) {
                for (int i = 0; i < mConfigList.size() ; i++) {
                    if ((mConfigList.get(i).getFromServiceId() == startId)
                            && (mConfigList.get(i).getToServiceId() == endId)) {
                        mConfigList.get(i).setSelected(selected);
                        found = true;
                    }
                }
            }
            if (!found) {
                mConfigList.add(new SmsBroadcastConfigInfo(startId, endId,
                            SMS_CB_CODE_SCHEME_MIN, SMS_CB_CODE_SCHEME_MAX,
                            selected));
            }
}

/**

//<End of snippet n. 1>








