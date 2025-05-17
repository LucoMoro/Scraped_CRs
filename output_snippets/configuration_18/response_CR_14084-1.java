//<Beginning of snippet n. 0>
.data(Tags.BASE_TRUNCATION_SIZE, Eas.EAS12_TRUNCATION_SIZE)
.end();
} else {
    if (Eas.getProtocolVersion() < 12.0) {
        s.data(Tags.MIME_DATA, Eas.EAS2_5_TRUNCATION_SIZE);
    } else {
        s.data(Tags.SYNC_TRUNCATION, Eas.EAS2_5_TRUNCATION_SIZE);
    }
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
    String body = getValue();
    String text = getTextValue(); // assuming this method exists for text extraction
    if (body != null) {
        if (isHtml(body)) {
            msg.mHtml = body;
        } else {
            msg.mText = body;
        }
    }
}

private void attachmentsParser(ArrayList<Attachment> atts, Message msg) throws IOException {
    while (nextTag(Tags.EMAIL_ATTACHMENTS) != END) {
//<End of snippet n. 1>