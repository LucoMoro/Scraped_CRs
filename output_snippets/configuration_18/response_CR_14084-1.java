//<Beginning of snippet n. 0>
.data(Tags.BASE_TRUNCATION_SIZE, Eas.EAS12_TRUNCATION_SIZE)
.end();
} else {
                s.data(Tags.MIME_DATA, Eas.EAS2_5_TRUNCATION_SIZE); // Updated for MIME_DATA request
}
s.end();

//<End of snippet n. 0>


//<Beginning of snippet n. 1>

package com.android.exchange.adapter;

import com.android.email.mail.Address;
import com.android.email.provider.AttachmentProvider;
import com.android.email.provider.EmailContent;
import com.android.email.provider.EmailProvider;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

case Tags.EMAIL_FLAG:
msg.mFlagFavorite = flagParser();
break;
case Tags.EMAIL_BODY:
String text = getValue();
msg.mText = text;

if (isHtmlContent()) { // Additional check for HTML content
    msg.mHtml = parseHtmlContent(text); // Ensures HTML content is populated correctly
} else {
    msg.mText = body; // Assigning fallback text
}
}
}

private void attachmentsParser(ArrayList<Attachment> atts, Message msg) throws IOException {
while (nextTag(Tags.EMAIL_ATTACHMENTS) != END) {
//<End of snippet n. 1>