/*Improve sending web link with Bluetooth

When sending web link from Google maps app, link cannot be used by
receiver as maps app will send extra text in addition to web link.

Improve the creation of html so that only the web link is
interpreted as a link.

Code is partly copied from MessageView class in the email app.

Change-Id:Idb093cc3d5af5fa6b85649d194fa6dc0c6fdfd1e*/




//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppLauncherActivity.java b/src/com/android/bluetooth/opp/BluetoothOppLauncherActivity.java
//Synthetic comment -- index 8f2b910..bde0030 100644

//Synthetic comment -- @@ -51,6 +51,11 @@
import android.util.Log;
import android.provider.Settings;

import android.util.Patterns;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Locale;

/**
* This class is designed to act as the entry point of handling the share intent
* via BT from other APPs. and also make "Bluetooth" available in sharing method
//Synthetic comment -- @@ -61,6 +66,10 @@
private static final boolean D = Constants.DEBUG;
private static final boolean V = Constants.VERBOSE;

    // Regex that matches characters that have special meaning in HTML. '<', '>', '&' and
    // multiple continuous spaces.
    private static final Pattern PLAIN_TEXT_TO_ESCAPE = Pattern.compile("[<>&]| {2,}|\r?\n");

@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
//Synthetic comment -- @@ -211,11 +220,58 @@
String fileName = getString(R.string.bluetooth_share_file_name) + ".html";
context.deleteFile(fileName);

            /*
             * Convert the plain text to HTML
             */
            StringBuffer sb = new StringBuffer("<html><head><meta http-equiv=\"Content-Type\""
                    + " content=\"text/html; charset=UTF-8\"/></head><body>");
            // Escape any inadvertent HTML in the text message
            String text = escapeCharacterToDisplay(shareContent.toString());

            // Regex that matches Web URL protocol part as case insensitive.
            Pattern webUrlProtocol = Pattern.compile("(?i)(http|https)://");

            Pattern pattern = Pattern.compile("("
                    + Patterns.WEB_URL.pattern() + ")|("
                    + Patterns.EMAIL_ADDRESS.pattern() + ")|("
                    + Patterns.PHONE.pattern() + ")");
            // Find any embedded URL's and linkify
            Matcher m = pattern.matcher(text);
            while (m.find()) {
                String matchStr = m.group();
                String link = null;

                // Find any embedded URL's and linkify
                if (Patterns.WEB_URL.matcher(matchStr).matches()) {
                    Matcher proto = webUrlProtocol.matcher(matchStr);
                    if (proto.find()) {
                        // This is work around to force URL protocol part be lower case,
                        // because WebView could follow only lower case protocol link.
                        link = proto.group().toLowerCase(Locale.US) +
                                matchStr.substring(proto.end());
                    } else {
                        // Patterns.WEB_URL matches URL without protocol part,
                        // so added default protocol to link.
                        link = "http://" + matchStr;
                    }

                // Find any embedded email address
                } else if (Patterns.EMAIL_ADDRESS.matcher(matchStr).matches()) {
                    link = "mailto:" + matchStr;

                // Find any embedded phone numbers and linkify
                } else if (Patterns.PHONE.matcher(matchStr).matches()) {
                    link = "tel:" + matchStr;
                }
                if (link != null) {
                    String href = String.format("<a href=\"%s\">%s</a>", link, matchStr);
                    m.appendReplacement(sb, href);
                }
            }
            m.appendTail(sb);
            sb.append("</body></html>");

            byte[] byteBuff = sb.toString().getBytes();

outStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
if (outStream != null) {
//Synthetic comment -- @@ -244,4 +300,44 @@
}
return fileUri;
}

    /**
     * Escape some special character as HTML escape sequence.
     *
     * @param text Text to be displayed using WebView.
     * @return Text correctly escaped.
     */
    private static String escapeCharacterToDisplay(String text) {
        Pattern pattern = PLAIN_TEXT_TO_ESCAPE;
        Matcher match = pattern.matcher(text);

        if (match.find()) {
            StringBuilder out = new StringBuilder();
            int end = 0;
            do {
                int start = match.start();
                out.append(text.substring(end, start));
                end = match.end();
                int c = text.codePointAt(start);
                if (c == ' ') {
                    // Escape successive spaces into series of "&nbsp;".
                    for (int i = 1, n = end - start; i < n; ++i) {
                        out.append("&nbsp;");
                    }
                    out.append(' ');
                } else if (c == '\r' || c == '\n') {
                    out.append("<br>");
                } else if (c == '<') {
                    out.append("&lt;");
                } else if (c == '>') {
                    out.append("&gt;");
                } else if (c == '&') {
                    out.append("&amp;");
                }
            } while (match.find());
            out.append(text.substring(end));
            text = out.toString();
        }
        return text;
    }
}







