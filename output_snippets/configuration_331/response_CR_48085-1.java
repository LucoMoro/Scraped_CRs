//<Beginning of snippet n. 0>
Message response = obtainMessage(EVENT_READ_ICON_DONE, fileid, 0, onLoaded);

// Constructing complete path for EF-IMG (4F20)
String completeFileId = constructPathForEFIMG(fileid);

mCi.iccIOForApp(COMMAND_GET_RESPONSE, completeFileId, "img", 0, 0,
GET_RESPONSE_EF_IMG_SIZE_BYTES, null, null,
mAid, response);
}

//<End of snippet n. 0>