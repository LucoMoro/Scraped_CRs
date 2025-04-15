/*SIPTransactionStack: Map.contains(key) was used instead of Map.containsKey(key)

Change-Id:Ia19b220f72ad5c4cc0ce82cd1b30f20337d76f5c*/
//Synthetic comment -- diff --git a/java/gov/nist/javax/sip/stack/SIPTransactionStack.java b/java/gov/nist/javax/sip/stack/SIPTransactionStack.java
//Synthetic comment -- index 1788bbe..05e12bf 100644

//Synthetic comment -- @@ -890,7 +890,7 @@
*/
public boolean isTransactionPendingAck(SIPServerTransaction serverTransaction) {
String branchId = ((SIPRequest)serverTransaction.getRequest()).getTopmostVia().getBranch();
        return this.terminatedServerTransactionsPendingAck.contains(branchId); 
}

/**







