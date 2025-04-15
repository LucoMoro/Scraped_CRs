/*Telephony: Synchronize calls to enable/disable CB messages

Synchronization is needed for enable/disable CB messages as
multiple client applications can exist to receive CB messages.

Change-Id:Iff7d5dd57a2061f6e0ab0e3db0a73b344a080683*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SimSmsInterfaceManager.java b/src/java/com/android/internal/telephony/gsm/SimSmsInterfaceManager.java
//Synthetic comment -- index 92bf390..590e52b 100644

//Synthetic comment -- @@ -224,7 +224,7 @@
return disableCellBroadcastRange(messageIdentifier, messageIdentifier);
}

    public boolean enableCellBroadcastRange(int startMessageId, int endMessageId) {
if (DBG) log("enableCellBroadcastRange");

Context context = mPhone.getContext();
//Synthetic comment -- @@ -251,7 +251,7 @@
return true;
}

    public boolean disableCellBroadcastRange(int startMessageId, int endMessageId) {
if (DBG) log("disableCellBroadcastRange");

Context context = mPhone.getContext();







