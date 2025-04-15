/*Telephony: Display proper error message when changing PIN1

PIN1 can be changed only after enabling SIM lock. RIL returns
REQUEST_NOT_SUPPORTED error if user tries to change PIN1 without
enabling SIM lock.
Handle the error and display appropriate message when trying to
change PIN1 using MMI code.

Change-Id:I888d9ddbf9d0f38277f96e9b51392ef12b5b8f71*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaMmiCode.java b/src/java/com/android/internal/telephony/cdma/CdmaMmiCode.java
//Synthetic comment -- index 8dd8c2e..69199ee 100644

//Synthetic comment -- @@ -271,6 +271,11 @@
sb.append(context.getText(
com.android.internal.R.string.passwordIncorrect));
}
} else {
sb.append(context.getText(
com.android.internal.R.string.mmiError));








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index fdc0606..ae7b232 100644

//Synthetic comment -- @@ -1007,6 +1007,11 @@
sb.append("\n");
sb.append(context.getText(
com.android.internal.R.string.needPuk2));
} else if (err == CommandException.Error.FDN_CHECK_FAILURE) {
Log.i(LOG_TAG, "FDN_CHECK_FAILURE");
sb.append(context.getText(com.android.internal.R.string.mmiFdnError));







