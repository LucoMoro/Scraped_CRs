//<Beginning of snippet n. 0>


Message response = obtainMessage(EVENT_READ_ICON_DONE, fileid, 0, onLoaded);

// Validate fileid for RUIM cards
if (isValidFileIdForRUIM(fileid)) {
    String efImgPath = getEFImgPathForRUIM(fileid);
    mCi.iccIOForApp(COMMAND_GET_RESPONSE, efImgPath, "img", 0, 0,
            GET_RESPONSE_EF_IMG_SIZE_BYTES, null, null,
            mAid, response);
} else {
    // Handle error scenario
    handleError("Invalid fileid for EF-IMG access");
}

//<End of snippet n. 0>