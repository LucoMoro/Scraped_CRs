/*Adding mimetypes to the mimetype map used in the web view

Sometimes a web server reports a faulty mime type, usually when the mimetype for this
type of content is not configured on the server. In this case the server might report
text/plain as the mime type.

When this ocurrs the phone opens this content as plain text inline in the browser instead
of asking system if this is supported by any application.

This is the case for the following mime types:

"XMF" which MIMEtype is "audio/midi"
"MXMF" which MIMEtype is "audio/mobile-xmf"
"AMR" which MIMEtype is "audio/amr"

By adding these MimeTypes to the lookup table in the browser it is possible to guess the
real mime type based on the file extension, and asks the system for support for the
mimtype in the table, fixing the problem.

Change-Id:I88bb37e4a6d66e68d7e26cb96bb9404175b33b68*/
//Synthetic comment -- diff --git a/core/java/android/webkit/MimeTypeMap.java b/core/java/android/webkit/MimeTypeMap.java
//Synthetic comment -- index 60dfce7..8914a91 100644

//Synthetic comment -- @@ -362,10 +362,13 @@
sMimeTypeMap.loadEntry("application/x-xcf", "xcf");
sMimeTypeMap.loadEntry("application/x-xfig", "fig");
sMimeTypeMap.loadEntry("application/xhtml+xml", "xhtml");
sMimeTypeMap.loadEntry("audio/basic", "snd");
sMimeTypeMap.loadEntry("audio/midi", "mid");
sMimeTypeMap.loadEntry("audio/midi", "midi");
sMimeTypeMap.loadEntry("audio/midi", "kar");
sMimeTypeMap.loadEntry("audio/mpeg", "mpga");
sMimeTypeMap.loadEntry("audio/mpeg", "mpega");
sMimeTypeMap.loadEntry("audio/mpeg", "mp2");







