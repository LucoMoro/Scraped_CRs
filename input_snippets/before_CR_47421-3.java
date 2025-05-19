
//<Beginning of snippet n. 0>


RILRequest rr
= RILRequest.obtain(RIL_REQUEST_SEND_USSD, response);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest)
                            + " " + ussdString);

rr.mp.writeString(ussdString);


//<End of snippet n. 0>








