/*Fix data type of RIL_UNSOL_RESPONSE_NEW_BROADCAST_SMS in RIL.

Fixing incorrect data type of RIL_UNSOL_RESPONSE_NEW_BROADCAST_SMS
in Android RIL.

The Android RIL specifies that the unsolicited response signal
RIL_UNSOL_RESPONSE_NEW_BROADCAST_SMS should contain a raw byte
array (ril.h). However, in the implementation of the Java RIL and
the C++ RIL daemon, the data is unpacked as a string. This causes
data delivered from the network stack to be garbled.

Changing so that both the C++ RIL deamon in platform/hardware/ril
and the Java RIL in frameworks/base follows the specification.

Change-Id:I9b9b26d16cd39422c43d9fa7aadfcff3e60c78ae*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RIL.java b/telephony/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index 7239889..8ec1d61 100644

//Synthetic comment -- @@ -2329,7 +2329,7 @@
case RIL_UNSOL_RESTRICTED_STATE_CHANGED: ret = responseInts(p); break;
case RIL_UNSOL_RESPONSE_SIM_STATUS_CHANGED:  ret =  responseVoid(p); break;
case RIL_UNSOL_RESPONSE_CDMA_NEW_SMS:  ret =  responseCdmaSms(p); break;
            case RIL_UNSOL_RESPONSE_NEW_BROADCAST_SMS:  ret =  responseRaw(p); break;
case RIL_UNSOL_CDMA_RUIM_SMS_STORAGE_FULL:  ret =  responseVoid(p); break;
case RIL_UNSOL_ENTER_EMERGENCY_CALLBACK_MODE: ret = responseVoid(p); break;
case RIL_UNSOL_CDMA_CALL_WAITING: ret = responseCdmaCallWaiting(p); break;







