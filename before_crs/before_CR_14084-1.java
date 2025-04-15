/*Implement support for receiving HTML emails from Exchange 2003 and
Zimbra's mobile sync.

EasSyncService.java: update ActiveSync versions less under 12.0 to request
MIME_DATA instead of defaulting to text only

EmailSyncAdapter.java: parse MIME body content when Tags.EMAIL_MIME_DATA is
encountered, appropriately populate Message.mHtml or Message.mText

Change-Id:Ib8fc707c293ae4247dc63cc82985681b1a159d06*/
//Synthetic comment -- diff --git a/src/com/android/exchange/EasSyncService.java b/src/com/android/exchange/EasSyncService.java
//Synthetic comment -- index bba2ddf..9b03317 100644

//Synthetic comment -- @@ -1053,7 +1053,12 @@
.data(Tags.BASE_TRUNCATION_SIZE, Eas.EAS12_TRUNCATION_SIZE)
.end();
} else {
                s.data(Tags.SYNC_TRUNCATION, Eas.EAS2_5_TRUNCATION_SIZE);
}
s.end();









//Synthetic comment -- diff --git a/src/com/android/exchange/adapter/EmailSyncAdapter.java b/src/com/android/exchange/adapter/EmailSyncAdapter.java
//Synthetic comment -- index 946ae4e..ee56812 100644

//Synthetic comment -- @@ -18,6 +18,12 @@
package com.android.exchange.adapter;

import com.android.email.mail.Address;
import com.android.email.provider.AttachmentProvider;
import com.android.email.provider.EmailContent;
import com.android.email.provider.EmailProvider;
//Synthetic comment -- @@ -45,6 +51,9 @@

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
//Synthetic comment -- @@ -159,6 +168,16 @@
case Tags.EMAIL_FLAG:
msg.mFlagFavorite = flagParser();
break;
case Tags.EMAIL_BODY:
String text = getValue();
msg.mText = text;
//Synthetic comment -- @@ -231,6 +250,88 @@
msg.mText = body;
}
}

private void attachmentsParser(ArrayList<Attachment> atts, Message msg) throws IOException {
while (nextTag(Tags.EMAIL_ATTACHMENTS) != END) {







