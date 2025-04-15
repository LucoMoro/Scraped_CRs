/*Removing repetitive conditional; it simply does nothing except slowing down things.

Change-Id:Ibe76c1ed49bdf66eb57702e5f89363a5132d9617*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
//Synthetic comment -- index d2a4bd8..5b6bc1f 100755

//Synthetic comment -- @@ -674,8 +674,7 @@
return;
}

            if (err != CommandException.Error.OP_NOT_ALLOWED_BEFORE_REG_NW) {
Log.e(LOG_TAG,
"RIL implementation has returned an error where it must succeed",
ar.exception);







