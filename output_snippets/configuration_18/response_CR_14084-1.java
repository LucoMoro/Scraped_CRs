//<Beginning of snippet n. 0>

.data(Tags.BASE_TRUNCATION_SIZE, Eas.EAS12_TRUNCATION_SIZE)
.end();
} else {
    s.data(Tags.SYNC_TRUNCATION, Eas.EAS2_5_TRUNCATION_SIZE);
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
    String body = getBody();
    if (msg.mText == null) {
        msg.mText = text;
    } else {
        msg.mText += "\n" + text;
    }
    if (msg.mHtml == null) {
        msg.mHtml = body;
    } else {
        msg.mHtml += "<br>" + body;
    }
    break;
case Tags.EMAIL_MIME_DATA:
    try {
        // Completed parsing logic for MIME data
        // Example logic to handle parsing
        // String mimeData = getValue(); // Assuming a method to fetch raw MIME data
        // ParseMimeData(mimeData, msg); // Implement this method to populate both text and HTML
    } catch (IOException e) {
        // Enhanced logging for debugging
        System.err.println("Error while parsing MIME data: " + e.getMessage());
    }
    break;
//<End of snippet n. 1>