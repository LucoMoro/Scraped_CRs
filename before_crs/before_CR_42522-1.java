/*Telephony: Clarify IccCardConstants.State enum

Change-Id:I57c6704dbefbcca5d1c6b28e72f0fb80bdbea7fc*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccCardConstants.java b/telephony/java/com/android/internal/telephony/IccCardConstants.java
//Synthetic comment -- index 4d1eb3f..236bb2f 100644

//Synthetic comment -- @@ -47,10 +47,13 @@
/* PERM_DISABLED means ICC is permanently disabled due to puk fails */
public static final String INTENT_VALUE_ABSENT_ON_PERM_DISABLED = "PERM_DISABLED";

    /*
      UNKNOWN is a transient state, for example, after uesr inputs ICC pin under
      PIN_REQUIRED state, the query for ICC status returns UNKNOWN before it
      turns to READY
*/
public enum State {
UNKNOWN,







