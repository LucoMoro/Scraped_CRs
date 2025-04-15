/*Fixing concurrency issue on IccPhoneBookInterfaceManager.

All the reads/updates methods are synchronous calls that rely
on an unique lock object in order to wait for the asynchronous
simcard operations to complete and return appropriate results

Concurent calls to these methods will cause errors when one
completed operation will unlock all waiting calls generating
inconsistent results on some of the method calls.

Change-Id:I8b87004ac039bcb971b8369f7640281f1bf9eb35Signed-off-by: David Sobreira Marques <dpsmarques@gmail.com>*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java b/telephony/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java
//Synthetic comment -- index 2f22d74..e94efa8 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import android.os.ServiceManager;

import java.util.List;

/**
* SimPhoneBookInterfaceManager to provide an inter-process communication to
//Synthetic comment -- @@ -63,14 +64,14 @@
" total " + recordSize[1] +
" #record " + recordSize[2]);
}
                        mLock.notifyAll();
}
break;
case EVENT_UPDATE_DONE:
ar = (AsyncResult) msg.obj;
synchronized (mLock) {
success = (ar.exception == null);
                        mLock.notifyAll();
}
break;
case EVENT_LOAD_DONE:
//Synthetic comment -- @@ -84,11 +85,20 @@
records.clear();
}
}
                        mLock.notifyAll();
}
break;
}
}
};

public IccPhoneBookInterfaceManager(PhoneBase phone) {
//Synthetic comment -- @@ -150,15 +160,12 @@
synchronized(mLock) {
checkThread();
success = false;
            Message response = mBaseHandler.obtainMessage(EVENT_UPDATE_DONE);
AdnRecord oldAdn = new AdnRecord(oldTag, oldPhoneNumber);
AdnRecord newAdn = new AdnRecord(newTag, newPhoneNumber);
adnCache.updateAdnBySearch(efid, oldAdn, newAdn, pin2, response);
            try {
                mLock.wait();
            } catch (InterruptedException e) {
                logd("interrupted while trying to update by search");
            }
}
return success;
}
//Synthetic comment -- @@ -197,14 +204,11 @@
synchronized(mLock) {
checkThread();
success = false;
            Message response = mBaseHandler.obtainMessage(EVENT_UPDATE_DONE);
AdnRecord newAdn = new AdnRecord(newTag, newPhoneNumber);
adnCache.updateAdnByIndex(efid, newAdn, index, pin2, response);
            try {
                mLock.wait();
            } catch (InterruptedException e) {
                logd("interrupted while trying to update by index");
            }
}
return success;
}
//Synthetic comment -- @@ -243,15 +247,12 @@

synchronized(mLock) {
checkThread();
            Message response = mBaseHandler.obtainMessage(EVENT_LOAD_DONE);
adnCache.requestLoadAllAdnLike(efid, adnCache.extensionEfForEf(efid), response);
            try {
                mLock.wait();
            } catch (InterruptedException e) {
                logd("interrupted while trying to load from the SIM");
            }
}
            return records;
}

protected void checkThread() {
//Synthetic comment -- @@ -274,5 +275,15 @@
}
return efid;
}
}









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/RuimPhoneBookInterfaceManager.java b/telephony/java/com/android/internal/telephony/cdma/RuimPhoneBookInterfaceManager.java
//Synthetic comment -- index 6e12f24a..e99c7d0 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.internal.telephony.cdma;

import android.os.Message;
import android.util.Log;

//Synthetic comment -- @@ -56,13 +58,16 @@
recordSize = new int[3];

//Using mBaseHandler, no difference in EVENT_GET_SIZE_DONE handling
            Message response = mBaseHandler.obtainMessage(EVENT_GET_SIZE_DONE);

phone.getIccFileHandler().getEFLinearRecordSize(efid, response);
            try {
                mLock.wait();
            } catch (InterruptedException e) {
                logd("interrupted while trying to load from the RUIM");
}
}









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SimPhoneBookInterfaceManager.java b/telephony/java/com/android/internal/telephony/gsm/SimPhoneBookInterfaceManager.java
//Synthetic comment -- index feb508a..13562ed 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.internal.telephony.gsm;

import android.os.Message;
import android.util.Log;

//Synthetic comment -- @@ -56,13 +58,16 @@
recordSize = new int[3];

//Using mBaseHandler, no difference in EVENT_GET_SIZE_DONE handling
            Message response = mBaseHandler.obtainMessage(EVENT_GET_SIZE_DONE);

phone.getIccFileHandler().getEFLinearRecordSize(efid, response);
            try {
                mLock.wait();
            } catch (InterruptedException e) {
                logd("interrupted while trying to load from the SIM");
}
}








