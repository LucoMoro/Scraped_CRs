/*telephony: Return NOT_SUPPORTED for RIL_REQUEST_GET_NEIGHBORING_CELL_IDS.

Currently, RIL_REQUEST_GET_NEIGHBORING_CELL_IDS causes the RIL to crash,
due to backward compatibility issues.
This fix is a temporary hack to make the API return REQUEST_NOT_SUPPORTED
Command Exception till the compatibility issues are fixed.*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/RIL.java b/telephony/java/com/android/internal/telephony/gsm/RIL.java
//Synthetic comment -- index 45000ba..8ec286a 100644

//Synthetic comment -- @@ -1577,12 +1577,22 @@
* {@inheritDoc}
*/
public void getNeighboringCids(Message response) {
        /* TODO: Remove this hack when backward compatibility issue is fixed.
         * RIL_REQUEST_GET_NEIGHBORING_CELL_IDS currently returns REQUEST_NOT_SUPPORTED
         */

        AsyncResult.forMessage(response).exception =
            new CommandException(CommandException.Error.REQUEST_NOT_SUPPORTED);
        response.sendToTarget();
        response = null;

        /* RILRequest rr = RILRequest.obtain(
RILConstants.RIL_REQUEST_GET_NEIGHBORING_CELL_IDS, response);

if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

send(rr);
        */
}

/**







