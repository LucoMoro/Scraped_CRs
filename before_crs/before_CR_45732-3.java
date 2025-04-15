/*SIPTransactionStack: Map.contains(key) was used instead of Map.contains(value)

Change-Id:Ia19b220f72ad5c4cc0ce82cd1b30f20337d76f5cSigned-off-by: László Dávid <laszlo.david@gmail.com>*/
//Synthetic comment -- diff --git a/java/gov/nist/javax/sip/stack/SIPTransactionStack.java b/java/gov/nist/javax/sip/stack/SIPTransactionStack.java
//Synthetic comment -- index 1788bbe..037caa2 100644

//Synthetic comment -- @@ -889,8 +889,7 @@
* @return
*/
public boolean isTransactionPendingAck(SIPServerTransaction serverTransaction) {
        String branchId = ((SIPRequest)serverTransaction.getRequest()).getTopmostVia().getBranch();
        return this.terminatedServerTransactionsPendingAck.contains(branchId); 
}

/**







