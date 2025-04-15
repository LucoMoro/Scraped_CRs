/*Telephony: Null check for adncache before search/update.

Sometimes adncache is uninitialized leading to exceptions.
Add checking against null before using it for searching or updating
records.

Conflicts:

	src/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java

Change-Id:I5b6787ce917db921d1ee35753acf890dfaf169b7*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java b/src/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java
//Synthetic comment -- index 2bab4bb..a2c0d0f 100644

//Synthetic comment -- @@ -183,8 +183,12 @@
Message response = mBaseHandler.obtainMessage(EVENT_UPDATE_DONE, status);
AdnRecord oldAdn = new AdnRecord(oldTag, oldPhoneNumber);
AdnRecord newAdn = new AdnRecord(newTag, newPhoneNumber);
            adnCache.updateAdnBySearch(efid, oldAdn, newAdn, pin2, response);
            waitForResult(status);
}
return success;
}
//Synthetic comment -- @@ -226,8 +230,12 @@
AtomicBoolean status = new AtomicBoolean(false);
Message response = mBaseHandler.obtainMessage(EVENT_UPDATE_DONE, status);
AdnRecord newAdn = new AdnRecord(newTag, newPhoneNumber);
            adnCache.updateAdnByIndex(efid, newAdn, index, pin2, response);
            waitForResult(status);
}
return success;
}
//Synthetic comment -- @@ -268,8 +276,12 @@
checkThread();
AtomicBoolean status = new AtomicBoolean(false);
Message response = mBaseHandler.obtainMessage(EVENT_LOAD_DONE, status);
            adnCache.requestLoadAllAdnLike(efid, adnCache.extensionEfForEf(efid), response);
            waitForResult(status);
}
return records;
}







