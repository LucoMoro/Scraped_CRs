/*Telephony: Add ACTIVATION_REJECT_GGSN to Permanent Fail Causes

- Adding ACTIVATION_REJECT_GGSN to Permanent Fail Codes
  in order to comply with "no retry" requirements in ril.h

Change-Id:I377eba7ec0203e41ae7689192a41f6b260a8775a*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DataConnection.java b/src/java/com/android/internal/telephony/DataConnection.java
//Synthetic comment -- index 9751040..80b446c 100644

//Synthetic comment -- @@ -163,7 +163,7 @@
public boolean isPermanentFail() {
return (this == OPERATOR_BARRED) || (this == MISSING_UNKNOWN_APN) ||
(this == UNKNOWN_PDP_ADDRESS_TYPE) || (this == USER_AUTHENTICATION) ||
                   (this == SERVICE_OPTION_NOT_SUPPORTED) ||
(this == SERVICE_OPTION_NOT_SUBSCRIBED) || (this == NSAPI_IN_USE) ||
(this == PROTOCOL_ERRORS);
}







