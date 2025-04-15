/*Adding a content provider for reading specific operator flags from the SIM card.

Customization options are increased by introducing a way to read
specific operator defined flags from the SIM card. Operators can
store information on the SIM card that can be read at run-time or
at phone start up. This enables the possibility to use different
settings depending on the information stored on the SIM card. An
operator can for instance have different APN settings depending
on whether the SIM card is pre-pay or post-pay.

Change-Id:I04371e626535af6c27252ee8d4df93117beda967*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccCustProvider.java b/telephony/java/com/android/internal/telephony/IccCustProvider.java
new file mode 100644
//Synthetic comment -- index 0000000..b0f13c1

//Synthetic comment -- @@ -0,0 +1,308 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccFileHandler.java b/telephony/java/com/android/internal/telephony/IccFileHandler.java
//Synthetic comment -- index 92ddd2c..e8d4bfe 100644

//Synthetic comment -- @@ -214,13 +214,35 @@
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
//Synthetic comment -- @@ -422,6 +444,8 @@

fileid = msg.arg1;

if (TYPE_EF != data[RESPONSE_DATA_FILE_TYPE]) {
throw new IccFileTypeMismatch();
}
//Synthetic comment -- @@ -432,8 +456,13 @@

size = ((data[RESPONSE_DATA_FILE_SIZE_1] & 0xff) << 8)
+ (data[RESPONSE_DATA_FILE_SIZE_2] & 0xff);

                phone.mCM.iccIO(COMMAND_READ_BINARY, fileid, getEFPath(fileid),
0, 0, size, null, null,
obtainMessage(EVENT_READ_BINARY_DONE,
fileid, 0, response));








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/Phone.java b/telephony/java/com/android/internal/telephony/Phone.java
//Synthetic comment -- index 3c9a1f8..33d3117 100644

//Synthetic comment -- @@ -1707,6 +1707,12 @@
*/
void unsetOnEcbModeExitResponse(Handler h);


/**
* TODO: Adding a function for each property is not good.








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneProxy.java b/telephony/java/com/android/internal/telephony/PhoneProxy.java
//Synthetic comment -- index 5e7dcb0..be22b8f 100644

//Synthetic comment -- @@ -843,4 +843,8 @@
public boolean isCspPlmnEnabled() {
return mActivePhone.isCspPlmnEnabled();
}
}







