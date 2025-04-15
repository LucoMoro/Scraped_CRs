/*Telephony: Update Permanent Fail and Event Loggable Types

- Updating isPermanentFail() and isEventLoggable() to better
  comply with "no retry" requirements in ril.h

Change-Id:I377eba7ec0203e41ae7689192a41f6b260a8775a*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DataConnection.java b/src/java/com/android/internal/telephony/DataConnection.java
//Synthetic comment -- index 9751040..43b89d0 100644

//Synthetic comment -- @@ -163,9 +163,11 @@
public boolean isPermanentFail() {
return (this == OPERATOR_BARRED) || (this == MISSING_UNKNOWN_APN) ||
(this == UNKNOWN_PDP_ADDRESS_TYPE) || (this == USER_AUTHENTICATION) ||
                   (this == ACTIVATION_REJECT_GGSN) || (this == SERVICE_OPTION_NOT_SUPPORTED) ||
(this == SERVICE_OPTION_NOT_SUBSCRIBED) || (this == NSAPI_IN_USE) ||
                   (this == ONLY_IPV4_ALLOWED) || (this == ONLY_IPV6_ALLOWED) ||
                   (this == PROTOCOL_ERRORS) || (this == SIGNAL_LOST) ||
                   (this == RADIO_POWER_OFF) || (this == TETHERED_CALL_ACTIVE);
}

public boolean isEventLoggable() {
//Synthetic comment -- @@ -175,7 +177,9 @@
(this == SERVICE_OPTION_NOT_SUBSCRIBED) ||
(this == SERVICE_OPTION_NOT_SUPPORTED) ||
(this == SERVICE_OPTION_OUT_OF_ORDER) || (this == NSAPI_IN_USE) ||
                    (this == ONLY_IPV4_ALLOWED) || (this == ONLY_IPV6_ALLOWED) ||
                    (this == PROTOCOL_ERRORS) || (this == SIGNAL_LOST) ||
                    (this == RADIO_POWER_OFF) || (this == TETHERED_CALL_ACTIVE) ||
(this == UNACCEPTABLE_NETWORK_PARAMETER);
}








