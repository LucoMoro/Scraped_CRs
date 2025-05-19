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
    if (msg.mText == null || msg.mText.isEmpty()) {
        msg.mText = text;
    } else {
        msg.mText += "\n" + text;
    }
    if (msg.mHtml == null || msg.mHtml.isEmpty()) {
        msg.mHtml = body;
    } else {
        msg.mHtml += "<br>" + body;
    }
    break;
case Tags.EMAIL_MIME_DATA:
    try {
        String mimeData = getValue();
        ParseMimeData(mimeData, msg);
    } catch (IOException e) {
        System.err.println("Error while parsing MIME data: " + e.getMessage());
    }
    break;

private void ParseMimeData(String mimeData, Message msg) throws IOException {
    // Logic to separate MIME data into text and HTML
    String[] parts = mimeData.split("\\r?\\n");
    for (String part : parts) {
        if (part.startsWith("Content-Type: text/html")) {
            msg.mHtml += part;
        } else if (part.startsWith("Content-Type: text/plain")) {
            msg.mText += part;
        }
    }
}

//<End of snippet n. 1>