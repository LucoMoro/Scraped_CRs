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
mimtype in the table, fixing the problem.*/




//Synthetic comment -- diff --git a/core/java/android/webkit/MimeTypeMap.java b/core/java/android/webkit/MimeTypeMap.java
//Synthetic comment -- index fffba1b..1fec0dc 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2007 The Android Open Source Project
 * Copyright (C) 2010 Sony Ericsson Mobile Communications AB
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -362,10 +363,13 @@
sMimeTypeMap.loadEntry("application/x-xcf", "xcf");
sMimeTypeMap.loadEntry("application/x-xfig", "fig");
sMimeTypeMap.loadEntry("application/xhtml+xml", "xhtml");
            sMimeTypeMap.loadEntry("audio/amr", "amr");
sMimeTypeMap.loadEntry("audio/basic", "snd");
sMimeTypeMap.loadEntry("audio/midi", "mid");
sMimeTypeMap.loadEntry("audio/midi", "midi");
sMimeTypeMap.loadEntry("audio/midi", "kar");
            sMimeTypeMap.loadEntry("audio/midi", "xmf");
            sMimeTypeMap.loadEntry("audio/mobile-xmf", "mxmf");
sMimeTypeMap.loadEntry("audio/mpeg", "mpga");
sMimeTypeMap.loadEntry("audio/mpeg", "mpega");
sMimeTypeMap.loadEntry("audio/mpeg", "mp2");







