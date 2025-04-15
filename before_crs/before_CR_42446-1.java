/*Mms: Fix the logging of the TransactionBundle

The byte array returned by the getPushData() function should be
properly converted before beeing logged.

Change-Id:If24a20bc636282bdab5631935f1bcc6eafc20d90Author: Frederic Predon <frederic.predon@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Beare, Bruce J <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 28037*/
//Synthetic comment -- diff --git a/src/com/android/mms/transaction/TransactionBundle.java b/src/com/android/mms/transaction/TransactionBundle.java
//Synthetic comment -- index 4a3c94c..5962b90 100644

//Synthetic comment -- @@ -19,6 +19,8 @@

import android.os.Bundle;

/**
* A wrapper around the Bundle instances used to start the TransactionService.
* It provides high-level APIs to set the information required for the latter to
//Synthetic comment -- @@ -148,7 +150,7 @@
public String toString() {
return "transactionType: " + getTransactionType() +
" uri: " + getUri() +
            " pushData: " + getPushData() +
" mmscUrl: " + getMmscUrl() +
" proxyAddress: " + getProxyAddress() +
" proxyPort: " + getProxyPort();







