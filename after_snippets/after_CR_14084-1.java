
//<Beginning of snippet n. 0>


.data(Tags.BASE_TRUNCATION_SIZE, Eas.EAS12_TRUNCATION_SIZE)
.end();
} else {
                if (className.equals("Email")) {
                    s.data(Tags.SYNC_MIME_SUPPORT, Eas.BODY_PREFERENCE_HTML)
                            .data(Tags.SYNC_MIME_TRUNCATION,
                                    Eas.EAS2_5_TRUNCATION_SIZE);
                } else
                    s.data(Tags.SYNC_TRUNCATION, Eas.EAS2_5_TRUNCATION_SIZE);
}
s.end();


//<End of snippet n. 0>










//<Beginning of snippet n. 1>


package com.android.exchange.adapter;

import com.android.email.mail.Address;
import com.android.email.mail.internet.MimeMessage;
import com.android.email.mail.internet.MimeBodyPart;
import com.android.email.mail.internet.TextBody;
import com.android.email.mail.internet.BinaryTempFileBody;
import com.android.email.mail.Multipart;
import com.android.email.mail.MessagingException;
import com.android.email.provider.AttachmentProvider;
import com.android.email.provider.EmailContent;
import com.android.email.provider.EmailProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
case Tags.EMAIL_FLAG:
msg.mFlagFavorite = flagParser();
break;
                    case Tags.EMAIL_MIME_DATA:
                        try {
                            mimeBodyParser(msg, getValue());
                        }
                        catch (MessagingException e) {
                            IOException ioe = new IOException(e.getMessage());
                            ioe.initCause(e);
                            throw ioe;
                        }
                        break;
case Tags.EMAIL_BODY:
String text = getValue();
msg.mText = text;
msg.mText = body;
}
}
        private void mimeBodyParser(Message msg, String value)
        throws IOException, MessagingException {
            String mimeBody = value;
            ByteArrayInputStream in = new ByteArrayInputStream(
                    mimeBody.getBytes("UTF-8"));
            MimeMessage m = new MimeMessage(in);
            in.close();

            com.android.email.mail.Body b = m.getBody();
            if (b instanceof TextBody || b instanceof BinaryTempFileBody) {
                String type = m.getContentType();
                String text = getBodyContent(b);
                if (type.contains("text/html"))
                    msg.mHtml = text;
                else
                    msg.mText = text;

            } else if (b instanceof Multipart) {
                Multipart mp = (Multipart) b;
                MimeBodyPart p = getMultipartContent(mp);
                if (p != null) {
                    String type = p.getContentType();
                    if (type.contains("text/html")) {
                        msg.mHtml = getBodyContent(p.getBody());
                    } else {
                        msg.mText = getBodyContent(p.getBody());
                    }
                } else {
                    msg.mText = "Unable to find message body";
                }
            } else {
                // don't know if there are any other Body types
                msg.mText = b.getClass().getName();
            }
        }

        private MimeBodyPart getMultipartContent(Multipart mp)
        throws IOException, MessagingException {
            MimeBodyPart html = null;
            MimeBodyPart text = null;
            Multipart multipart = null;
            for (int i = 0, j = mp.getCount(); i < j && html == null; i++) {
                MimeBodyPart p = (MimeBodyPart) mp.getBodyPart(i);
                String type = p.getContentType();
                if (type.contains("text/html")) {
                    html = p;
                } else if (type.contains("text/plain")) {
                    text = p;
                } else if (p.getBody() instanceof Multipart) {
                    multipart = (Multipart) p.getBody();
                }
            }
            if (html != null)
                return html;
            if (text != null)
                return text;
            if (multipart != null) {
                return getMultipartContent(multipart);
            }
            return null;
        }

        private String getBodyContent(com.android.email.mail.Body body)
        throws IOException, MessagingException {
            if (body instanceof TextBody) {
                return ((TextBody)body).getText();
            } else if (body instanceof BinaryTempFileBody) {
                BinaryTempFileBody b = (BinaryTempFileBody) body;
                InputStreamReader isr = new InputStreamReader(b.getInputStream());
                StringWriter out = new StringWriter();
                char[] buf = new char[8192];
                int r;
                while ((r = isr.read(buf, 0, 8192)) != -1) {
                    out.write(buf, 0, r);
                }

                isr.close();
                return out.toString();
            } else {
                return body.getClass().getName();
            }
        }

private void attachmentsParser(ArrayList<Attachment> atts, Message msg) throws IOException {
while (nextTag(Tags.EMAIL_ATTACHMENTS) != END) {

//<End of snippet n. 1>








