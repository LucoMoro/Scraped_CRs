/*frameworks: Change default value of getIccLockEnabled

Change the default value of function getIccLockEnabled
to false.
When sim is deactivated/absent & user navigates to
Settings->Security->Set up SIM/RUIM card lock,
checkbox for "Lock Sim Card" option should be
unchecked by default.

Change-Id:Id031935a83c0cd38cd752826f7f1a866be30dd2b*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCardProxy.java b/src/java/com/android/internal/telephony/IccCardProxy.java
//Synthetic comment -- index eef0c6f..9f82fa0 100644

//Synthetic comment -- @@ -582,8 +582,8 @@
@Override
public boolean getIccLockEnabled() {
synchronized (mLock) {
            /* defaults to true, if ICC is absent */
            Boolean retValue = mUiccApplication != null ? mUiccApplication.getIccLockEnabled() : true;
return retValue;
}
}







