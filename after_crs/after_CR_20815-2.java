/*Add simple parsing steps to response from Email server in order to check the information regarding "ID"

There are two kind of phase when open the IMAP connection, IdPhrase and LoginPhrase
But when trying to log-in New Zealand Vodafone account, our application always goes to IdPhrase
even though there is no ID information from server response. So we add simple parsing steps
in order to check the information regarding "ID" . So in case that there is no "ID" response from server,
we've changed code to directly go to LoginPhrase to setting up account works properly

Issue : Account of New Zealand Vodafone didn't set up

Change-Id:I3c0a42fb2d75594b99b5b23f5b485d03ecf1b2cbSigned-off-by: Sang-Jun Park <sj2202.park@samsung.com>*/




//Synthetic comment -- diff --git a/src/com/android/email/mail/store/ImapStore.java b/src/com/android/email/mail/store/ImapStore.java
//Synthetic comment -- index c96cd53..8a35abf 100644

//Synthetic comment -- @@ -1444,8 +1444,22 @@
}
// else: mIdPhrase = null, no ID will be emitted

                String capabilities = capabilityResponse.toString();
                Log.e("Email","capability response : " + capabilities);
                boolean canTryID = false;
                if( capabilities != null ) {
                    //#null# [CAPABILITY, ID, ...]
                    Pattern p = Pattern.compile("^(#(null|[0-9])+#)|[\\[\\], ]");
                    String capas[] = p.split(capabilities);
                    if( capas != null ) {
                        for( String capa : capas ) {
                            if(capa != null && capa.equals("ID")) canTryID = true;
                        }
                    }
                }

// Send user-agent in an RFC2971 ID command
                if (mIdPhrase != null && canTryID == true) {
try {
executeSimpleCommand(mIdPhrase);
} catch (ImapException ie) {







