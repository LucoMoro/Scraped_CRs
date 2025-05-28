
//<Beginning of snippet n. 0>

new file mode 100644


//<End of snippet n. 0>










//<Beginning of snippet n. 1>


*/

public void loadEFTransparent(int fileid, Message onLoaded) {
        Message response = obtainMessage(EVENT_GET_BINARY_SIZE_DONE,
                        fileid, 0, onLoaded);

        phone.mCM.iccIO(COMMAND_GET_RESPONSE, fileid, getEFPath(fileid),
                        0, 0, GET_RESPONSE_EF_SIZE_BYTES, null, null, response);
}

/**
* Load a SIM Transparent EF-IMG. Used right after loadEFImgLinearFixed to
* retrive STK's icon data.

fileid = msg.arg1;

if (TYPE_EF != data[RESPONSE_DATA_FILE_TYPE]) {
throw new IccFileTypeMismatch();
}

size = ((data[RESPONSE_DATA_FILE_SIZE_1] & 0xff) << 8)
+ (data[RESPONSE_DATA_FILE_SIZE_2] & 0xff);

                phone.mCM.iccIO(COMMAND_READ_BINARY, fileid, getEFPath(fileid),
0, 0, size, null, null,
obtainMessage(EVENT_READ_BINARY_DONE,
fileid, 0, response));

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


*/
void unsetOnEcbModeExitResponse(Handler h);


/**
* TODO: Adding a function for each property is not good.

//<End of snippet n. 2>










//<Beginning of snippet n. 3>


public boolean isCspPlmnEnabled() {
return mActivePhone.isCspPlmnEnabled();
}
}

//<End of snippet n. 3>








