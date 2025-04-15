/*Telephony: Specify file path when reading contacts from EF_ADN

Some cards, like RUIM and UICC with CSIM, have the same file ID for
multiple EFs. e.g. EF_ADN and EF_CSIM_LI. When trying to retrieve
the EF_ADN from such a card, the path gets mapped to that of
EF_CSIM_LI resulting in incorrect behavior.

To avoid this, specify the EF Id and the path to uniquely identify
EF_ADN, when reading contacts.

Change-Id:Id070bf9fe77fe6dba9d42d5e4fcf1cdc8aabd024*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/AdnRecordLoader.java b/src/java/com/android/internal/telephony/uicc/AdnRecordLoader.java
//Synthetic comment -- index 4de69d1..ad36a19 100644

//Synthetic comment -- @@ -24,6 +24,8 @@
import android.os.Message;
import android.telephony.Rlog;

import com.android.internal.telephony.uicc.IccConstants;

public class AdnRecordLoader extends Handler {
final static String LOG_TAG = "RIL_AdnRecordLoader";

//Synthetic comment -- @@ -63,6 +65,14 @@
mFh = fh;
}

    private String getEFPath(int efid) {
        if (efid == IccConstants.EF_ADN) {
            return IccConstants.MF_SIM + IccConstants.DF_TELECOM;
        }

        return null;
    }

/**
* Resulting AdnRecord is placed in response.obj.result
* or response.obj.exception is set
//Synthetic comment -- @@ -75,9 +85,15 @@
this.recordNumber = recordNumber;
this.userResponse = response;

        if (ef == IccConstants.EF_ADN) {
            mFh.loadEFLinearFixed(
                        ef, getEFPath(ef), recordNumber,
                        obtainMessage(EVENT_ADN_LOAD_DONE));
        } else {
            mFh.loadEFLinearFixed(
ef, recordNumber,
obtainMessage(EVENT_ADN_LOAD_DONE));
        }

}

//Synthetic comment -- @@ -93,10 +109,20 @@
this.extensionEF = extensionEF;
this.userResponse = response;

        /* If we are loading from EF_ADN, specifically
         * specify the path as well, since, on some cards,
         * the fileid is not unique.
         */
        if (ef == IccConstants.EF_ADN) {

            mFh.loadEFLinearFixedAll(
                    ef, getEFPath(ef),
                    obtainMessage(EVENT_ADN_LOAD_ALL_DONE));
        } else {
            mFh.loadEFLinearFixedAll(
                        ef,
                        obtainMessage(EVENT_ADN_LOAD_ALL_DONE));
        }
}

/**
//Synthetic comment -- @@ -120,8 +146,13 @@
this.userResponse = response;
this.pin2 = pin2;

        if (ef == IccConstants.EF_ADN) {
            mFh.getEFLinearRecordSize( ef, getEFPath(ef),
                obtainMessage(EVENT_EF_LINEAR_RECORD_SIZE_DONE, adn));
        } else {
            mFh.getEFLinearRecordSize( ef,
                    obtainMessage(EVENT_EF_LINEAR_RECORD_SIZE_DONE, adn));
        }
}

//***** Overridden from Handler








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/IccFileHandler.java b/src/java/com/android/internal/telephony/uicc/IccFileHandler.java
//Synthetic comment -- index ae460ba..df964d4 100644

//Synthetic comment -- @@ -104,6 +104,7 @@
int efid;
int recordNum, recordSize, countRecords;
boolean loadAll;
        String path;

Message onLoaded;

//Synthetic comment -- @@ -114,6 +115,23 @@
this.recordNum = recordNum;
this.onLoaded = onLoaded;
this.loadAll = false;
            this.path = null;
        }

        LoadLinearFixedContext(int efid, int recordNum, String path, Message onLoaded) {
            this.efid = efid;
            this.recordNum = recordNum;
            this.onLoaded = onLoaded;
            this.loadAll = false;
            this.path = path;
        }

        LoadLinearFixedContext(int efid, String path, Message onLoaded) {
            this.efid = efid;
            this.recordNum = 1;
            this.loadAll = true;
            this.onLoaded = onLoaded;
            this.path = path;
}

LoadLinearFixedContext(int efid, Message onLoaded) {
//Synthetic comment -- @@ -121,6 +139,7 @@
this.recordNum = 1;
this.loadAll = true;
this.onLoaded = onLoaded;
            this.path = null;
}
}

//Synthetic comment -- @@ -142,6 +161,26 @@
* Load a record from a SIM Linear Fixed EF
*
* @param fileid EF id
     * @param path Path of the EF on the card
     * @param recordNum 1-based (not 0-based) record number
     * @param onLoaded
     *
     * ((AsyncResult)(onLoaded.obj)).result is the byte[]
     *
     */
    public void loadEFLinearFixed(int fileid, String path, int recordNum, Message onLoaded) {
        Message response
            = obtainMessage(EVENT_GET_RECORD_SIZE_DONE,
                        new LoadLinearFixedContext(fileid, recordNum, path, onLoaded));

        mCi.iccIOForApp(COMMAND_GET_RESPONSE, fileid, path,
                        0, 0, GET_RESPONSE_EF_SIZE_BYTES, null, null, mAid, response);
    }

    /**
     * Load a record from a SIM Linear Fixed EF
     *
     * @param fileid EF id
* @param recordNum 1-based (not 0-based) record number
* @param onLoaded
*
//Synthetic comment -- @@ -149,12 +188,7 @@
*
*/
public void loadEFLinearFixed(int fileid, int recordNum, Message onLoaded) {
        loadEFLinearFixed(fileid, getEFPath(fileid), recordNum, onLoaded);
}

/**
//Synthetic comment -- @@ -181,17 +215,31 @@
* get record size for a linear fixed EF
*
* @param fileid EF id
     * @param path Path of the EF on the card
     * @param onLoaded ((AsnyncResult)(onLoaded.obj)).result is the recordSize[]
     *        int[0] is the record length int[1] is the total length of the EF
     *        file int[3] is the number of records in the EF file So int[0] *
     *        int[3] = int[1]
     */
    public void getEFLinearRecordSize(int fileid, String path, Message onLoaded) {
        Message response
                = obtainMessage(EVENT_GET_EF_LINEAR_RECORD_SIZE_DONE,
                        new LoadLinearFixedContext(fileid, path, onLoaded));
        mCi.iccIOForApp(COMMAND_GET_RESPONSE, fileid, path,
                    0, 0, GET_RESPONSE_EF_SIZE_BYTES, null, null, mAid, response);
    }

    /**
     * get record size for a linear fixed EF
     *
     * @param fileid EF id
* @param onLoaded ((AsnyncResult)(onLoaded.obj)).result is the recordSize[]
*        int[0] is the record length int[1] is the total length of the EF
*        file int[3] is the number of records in the EF file So int[0] *
*        int[3] = int[1]
*/
public void getEFLinearRecordSize(int fileid, Message onLoaded) {
        getEFLinearRecordSize(fileid, getEFPath(fileid), onLoaded);
}

/**
//Synthetic comment -- @@ -204,13 +252,28 @@
*
*/
public void loadEFLinearFixedAll(int fileid, Message onLoaded) {
        loadEFLinearFixedAll(fileid, getEFPath(fileid), onLoaded);
    }

    /**
     * Load all records from a SIM Linear Fixed EF
     *
     * @param fileid EF id
     * @param path Path of the EF on the card
     * @param onLoaded
     *
     * ((AsyncResult)(onLoaded.obj)).result is an ArrayList<byte[]>
     *
     */
    public void loadEFLinearFixedAll(int fileid, String path, Message onLoaded) {
        Message response = obtainMessage(EVENT_GET_RECORD_SIZE_DONE,
                        new LoadLinearFixedContext(fileid, path, onLoaded));

        mCi.iccIOForApp(COMMAND_GET_RESPONSE, fileid, path,
0, 0, GET_RESPONSE_EF_SIZE_BYTES, null, null, mAid, response);
}


/**
* Load a SIM Transparent EF
*
//Synthetic comment -- @@ -278,6 +341,23 @@
/**
* Update a record in a linear fixed EF
* @param fileid EF id
     * @param path Path of the EF on the card
     * @param recordNum 1-based (not 0-based) record number
     * @param data must be exactly as long as the record in the EF
     * @param pin2 for CHV2 operations, otherwist must be null
     * @param onComplete onComplete.obj will be an AsyncResult
     *                   onComplete.obj.userObj will be a IccIoResult on success
     */
    public void updateEFLinearFixed(int fileid, String path, int recordNum, byte[] data,
            String pin2, Message onComplete) {
        mCi.iccIOForApp(COMMAND_UPDATE_RECORD, fileid, path,
                        recordNum, READ_RECORD_MODE_ABSOLUTE, data.length,
                        IccUtils.bytesToHexString(data), pin2, mAid, onComplete);
    }

    /**
     * Update a record in a linear fixed EF
     * @param fileid EF id
* @param recordNum 1-based (not 0-based) record number
* @param data must be exactly as long as the record in the EF
* @param pin2 for CHV2 operations, otherwist must be null
//Synthetic comment -- @@ -333,6 +413,7 @@
int fileid;
int recordNum;
int recordSize[];
        String path = null;

try {
switch (msg.what) {
//Synthetic comment -- @@ -446,6 +527,7 @@
data = result.payload;
fileid = lc.efid;
recordNum = lc.recordNum;
                path = lc.path;

if (TYPE_EF != data[RESPONSE_DATA_FILE_TYPE]) {
throw new IccFileTypeMismatch();
//Synthetic comment -- @@ -466,7 +548,10 @@
lc.results = new ArrayList<byte[]>(lc.countRecords);
}

                 if (path == null) {
                     path = getEFPath(lc.efid);
                 }
                 mCi.iccIOForApp(COMMAND_READ_RECORD, lc.efid, path,
lc.recordNum,
READ_RECORD_MODE_ABSOLUTE,
lc.recordSize, null, null, mAid,
//Synthetic comment -- @@ -516,6 +601,7 @@
lc = (LoadLinearFixedContext) ar.userObj;
result = (IccIoResult) ar.result;
response = lc.onLoaded;
                path = lc.path;

if (ar.exception != null) {
sendResult(response, null, ar.exception);
//Synthetic comment -- @@ -539,7 +625,11 @@
if (lc.recordNum > lc.countRecords) {
sendResult(response, lc.results, null);
} else {
                        if (path == null) {
                            path = getEFPath(lc.efid);
                        }

                        mCi.iccIOForApp(COMMAND_READ_RECORD, lc.efid, path,
lc.recordNum,
READ_RECORD_MODE_ABSOLUTE,
lc.recordSize, null, null, mAid,







