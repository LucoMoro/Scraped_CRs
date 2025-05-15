
//<Beginning of snippet n. 0>


RILRequest rr
= RILRequest.obtain(RIL_REQUEST_SEND_USSD, response);

        if (RILJ_LOGD) {
            String logUssdString = "*******";
            if (RILJ_LOGV) logUssdString = ussdString;
            riljLog(rr.serialString() + "> " + requestToString(rr.mRequest)
                                   + " " + logUssdString);
        }

rr.mp.writeString(ussdString);


//<End of snippet n. 0>








