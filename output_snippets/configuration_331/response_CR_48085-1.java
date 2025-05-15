//<Beginning of snippet n. 0>

String fileid = "4F20"; // Complete path for EF-IMG (4F20) specific to RUIM cards
Message response = obtainMessage(EVENT_READ_ICON_DONE, fileid, 0, onLoaded);

try {
    mCi.iccIOForApp(COMMAND_GET_RESPONSE, fileid, "img", 0, 0,
    GET_RESPONSE_EF_IMG_SIZE_BYTES, null, null, mAid, response);
} catch (Exception e) {
    Log.e("EF-IMG Error", "Error accessing EF-IMG: " + e.getMessage());
}

//<End of snippet n. 0>