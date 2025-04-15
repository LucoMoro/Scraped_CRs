/*Removing self-assignment statement.

Change-Id:I429235093de4539bde3950075fbdaa4ef5bdeedd*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/sms/BearerData.java b/telephony/java/com/android/internal/telephony/cdma/sms/BearerData.java
//Synthetic comment -- index ab79fe9..ebe6467 100644

//Synthetic comment -- @@ -585,7 +585,6 @@
uData.payload = new byte[0];
uData.numFields = 0;
} else {
uData.numFields = uData.payload.length;
}
} else {







