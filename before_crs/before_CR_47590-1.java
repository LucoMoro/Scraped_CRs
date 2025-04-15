/*[Automation][Stress]java.lang.Error(JavaCrash) happened in com.android.email

SetupData instance is cleared to null, which causes the crash

Change-Id:Iceff5494c161c71d5990f4716fc8ec1ce678e254Author: Rui Wang <ruix.r.wang@intel.com>
Signed-off-by: Rui Wang <ruix.r.wang@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 56361*/
//Synthetic comment -- diff --git a/src/com/android/email/activity/setup/AccountSetupExchange.java b/src/com/android/email/activity/setup/AccountSetupExchange.java
//Synthetic comment -- index 828aeb0..e0cee71 100644

//Synthetic comment -- @@ -145,12 +145,17 @@
return;
}

        Account account = SetupData.getAccount();
        // If we've got a username and password and we're NOT editing, try autodiscover
        String username = account.mHostAuthRecv.mLogin;
        String password = account.mHostAuthRecv.mPassword;
        if (username != null && password != null) {
            onProceedNext(SetupData.CHECK_AUTODISCOVER, mFragment);
}
}








