/*Increase PDU parts buffer size for performance reasons

Changed the size of the temporary buffer used when storing a PDU part
to 8192 bytes instead of the previous 256 bytes. This greatly
decreases the time needed to store relatively large PDU parts. The
times to store PDU parts were so long that we frequently ended up with
an ANR. This change resulted in a total time usage of ~1000 ms instead
of ~10000 ms for ~500 kB worth of data.

Change-Id:Ia02cb28e4fd9dfe3aaa1fa30ff37659951cbed93*/
//Synthetic comment -- diff --git a/core/java/com/google/android/mms/pdu/PduPersister.java b/core/java/com/google/android/mms/pdu/PduPersister.java
//Synthetic comment -- index c4be513..54c7e6d 100644

//Synthetic comment -- @@ -765,7 +765,7 @@
Log.v(TAG, "Saving data to: " + uri);
}

                    byte[] buffer = new byte[256];
for (int len = 0; (len = is.read(buffer)) != -1; ) {
os.write(buffer, 0, len);
}







