/*Telephony: Fix issue in SMS-CB range management

Android SMS-CB range management cannot manage several ranges.
This patch fixes this issue.

Change-Id:I42e85aadc689cadfe06a350dffc65d036cde509eAuthor: Emmanuel Delaude <emmanuelx.delaude@intel.com>
Signed-off-by: Emmanuel Delaude <emmanuelx.delaude@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 8118*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IntRangeManager.java b/src/java/com/android/internal/telephony/IntRangeManager.java
//Synthetic comment -- index cc7774d..025f491 100644

//Synthetic comment -- @@ -537,7 +537,6 @@
* @return true if successful, false otherwise
*/
private boolean tryAddSingleRange(int startId, int endId, boolean selected) {
addRange(startId, endId, selected);
return finishUpdate();
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SimSmsInterfaceManager.java b/src/java/com/android/internal/telephony/gsm/SimSmsInterfaceManager.java
//Synthetic comment -- index 4e51bfd..0af63b2 100644

//Synthetic comment -- @@ -292,14 +292,27 @@
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







