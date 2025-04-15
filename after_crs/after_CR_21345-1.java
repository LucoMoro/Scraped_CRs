/*Removing repetitive conditional; it simply does nothing except slowing down things.

Change-Id:I53a176cebae0051a33647317e2f269ebf149f7d2*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index 6ddb312..18ef121 100644

//Synthetic comment -- @@ -643,8 +643,7 @@
return;
}

            if (err != CommandException.Error.OP_NOT_ALLOWED_BEFORE_REG_NW) {
Log.e(LOG_TAG,
"RIL implementation has returned an error where it must succeed" +
ar.exception);







