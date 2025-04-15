/*Remove sent USSD log

Content of messages sent over USSD are copied in the radio logcat only if
debug level set to verbose. There are USSD services requiring highly
sensitive information to be sent by the user, we really don't want this
information to stay in the logcat by default.

Change-Id:Ieba37be0e346a6728dc7b139105a148577e0c0ff*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index faae72a..f3ed594 100644

//Synthetic comment -- @@ -1712,8 +1712,12 @@
RILRequest rr
= RILRequest.obtain(RIL_REQUEST_SEND_USSD, response);

        if (RILJ_LOGD) {
            String logUssdString = "*******";
            if (RILJ_LOGV) logUssdString = ussdString;
            riljLog(rr.serialString() + "> " + requestToString(rr.mRequest)
                                   + " " + logUssdString);
        }

rr.mp.writeString(ussdString);








