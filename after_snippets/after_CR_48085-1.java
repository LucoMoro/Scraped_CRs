
//<Beginning of snippet n. 0>


Message response = obtainMessage(EVENT_READ_ICON_DONE, fileid, 0,
onLoaded);

        mCi.iccIOForApp(COMMAND_GET_RESPONSE, fileid, getEFPath(fileid), 0, 0,
GET_RESPONSE_EF_IMG_SIZE_BYTES, null, null,
mAid, response);
}

//<End of snippet n. 0>








