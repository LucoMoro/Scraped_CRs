/*[Email]JAVACRASH happens when pressing back key in Email outgoing server setup view.

SetupData been cleaned by some reason.

Change-Id:Ib2170814aabbe45ee070d7b4e2641aa4f9dc4d46Author: Rui Wang <ruix.r.wang@intel.com>
Signed-off-by: Rui Wang <ruix.r.wang@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 59682*/




//Synthetic comment -- diff --git a/src/com/android/email/activity/setup/AccountServerBaseFragment.java b/src/com/android/email/activity/setup/AccountServerBaseFragment.java
//Synthetic comment -- index abc31a1..006dd2f 100644

//Synthetic comment -- @@ -401,13 +401,17 @@
public boolean haveSettingsChanged() {
Account account = SetupData.getAccount();

        try{
            HostAuth sendAuth = account.getOrCreateHostAuthSend(mContext);
            boolean sendChanged = (mLoadedSendAuth != null && !mLoadedSendAuth.equals(sendAuth));

            HostAuth recvAuth = account.getOrCreateHostAuthRecv(mContext);
            boolean recvChanged = (mLoadedRecvAuth != null && !mLoadedRecvAuth.equals(recvAuth));

            return sendChanged || recvChanged;
        } catch(RuntimeException re) {
            return false;
        }
}

/**







