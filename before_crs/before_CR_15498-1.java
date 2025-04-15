/*Fix hang when error in SIM card communication

When there is a problem communicating with the RIL (e g when the SIM
card is not connected) the IccPhoneBookManager hangs. This is corrected.

Change-Id:I28cf6cd3b79a29f4784fdef2b5072af5533e53da*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java b/telephony/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java
//Synthetic comment -- index 9f8e57f..48257cc7 100644

//Synthetic comment -- @@ -62,8 +62,8 @@
logd("GET_RECORD_SIZE Size " + recordSize[0] +
" total " + recordSize[1] +
" #record " + recordSize[2]);
                            mLock.notifyAll();
}
}
break;
case EVENT_UPDATE_DONE:







