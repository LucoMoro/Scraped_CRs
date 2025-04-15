/*Make ImapFolder locale safe

Explicitly using Locale.US to avoid unexpected results in some
locales.

Change-Id:I5f86cbf8be1618c5adc012bd856af62d5764a30c*/




//Synthetic comment -- diff --git a/src/com/android/email/mail/store/ImapFolder.java b/src/com/android/email/mail/store/ImapFolder.java
//Synthetic comment -- index bd194be..3ab7bba 100644

//Synthetic comment -- @@ -60,6 +60,7 @@
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

class ImapFolder extends Folder {
private final static Flag[] PERMANENT_FLAGS =
//Synthetic comment -- @@ -186,7 +187,7 @@
}
}
try {
            connection.executeSimpleCommand(String.format(Locale.US,
ImapConstants.STATUS + " \"%s\" (" + ImapConstants.UIDVALIDITY + ")",
ImapStore.encodeFolderName(mName, mStore.mPathPrefix)));
mExists = true;
//Synthetic comment -- @@ -232,7 +233,8 @@
}
}
try {
            connection.executeSimpleCommand(String.format(Locale.US,
                    ImapConstants.CREATE + " \"%s\"",
ImapStore.encodeFolderName(mName, mStore.mPathPrefix)));
return true;

//Synthetic comment -- @@ -256,7 +258,7 @@
checkOpen();
try {
List<ImapResponse> responseList = mConnection.executeSimpleCommand(
                    String.format(Locale.US, ImapConstants.UID_COPY + " %s \"%s\"",
ImapStore.joinMessageUids(messages),
ImapStore.encodeFolderName(folder.getName(), mStore.mPathPrefix)));
// Build a message map for faster UID matching
//Synthetic comment -- @@ -344,7 +346,7 @@
try {
int unreadMessageCount = 0;
List<ImapResponse> responses = mConnection.executeSimpleCommand(String.format(
                    Locale.US, ImapConstants.STATUS + " \"%s\" (" + ImapConstants.UNSEEN + ")",
ImapStore.encodeFolderName(mName, mStore.mPathPrefix)));
// S: * STATUS mboxname (MESSAGES 231 UIDNEXT 44292)
for (ImapResponse response : responses) {
//Synthetic comment -- @@ -478,7 +480,7 @@
throw new MessagingException(String.format("Invalid range: %d %d", start, end));
}
return getMessagesInternal(
                searchForUids(String.format(Locale.US, "%d:%d NOT DELETED", start, end)), listener);
}

@Override
//Synthetic comment -- @@ -573,7 +575,7 @@
}

try {
            mConnection.sendCommand(String.format(Locale.US,
ImapConstants.UID_FETCH + " %s (%s)", ImapStore.joinMessageUids(messages),
Utility.combine(fetchFields.toArray(new String[fetchFields.size()]), ' ')
), false);
//Synthetic comment -- @@ -753,7 +755,7 @@

} else {
if (e.isString()) {
                        mp.setSubType(bs.getStringOrEmpty(i).getString().toLowerCase(Locale.US));
}
break; // Ignore the rest of the list.
}
//Synthetic comment -- @@ -778,7 +780,7 @@
final ImapString type = bs.getStringOrEmpty(0);
final ImapString subType = bs.getStringOrEmpty(1);
final String mimeType =
                    (type.getString() + "/" + subType.getString()).toLowerCase(Locale.US);

final ImapList bodyParams = bs.getListOrEmpty(2);
final ImapString cid = bs.getStringOrEmpty(3);
//Synthetic comment -- @@ -813,7 +815,7 @@
// TODO We need to convert " into %22, but
// because MimeUtility.getHeaderParameter doesn't recognize it,
// we can't fix it for now.
                contentType.append(String.format(Locale.US, ";\n %s=\"%s\"",
bodyParams.getStringOrEmpty(i - 1).getString(),
bodyParams.getStringOrEmpty(i).getString()));
}
//Synthetic comment -- @@ -836,7 +838,7 @@

if (bodyDisposition.size() > 0) {
final String bodyDisposition0Str =
                        bodyDisposition.getStringOrEmpty(0).getString().toLowerCase(Locale.US);
if (!TextUtils.isEmpty(bodyDisposition0Str)) {
contentDisposition.append(bodyDisposition0Str);
}
//Synthetic comment -- @@ -850,9 +852,9 @@
for (int i = 1, count = bodyDispositionParams.size(); i < count; i += 2) {

// TODO We need to convert " into %22.  See above.
                        contentDisposition.append(String.format(Locale.US, ";\n %s=\"%s\"",
bodyDispositionParams.getStringOrEmpty(i - 1)
                                        .getString().toLowerCase(Locale.US),
bodyDispositionParams.getStringOrEmpty(i).getString()));
}
}
//Synthetic comment -- @@ -861,7 +863,7 @@
if ((size > 0)
&& (MimeUtility.getHeaderParameter(contentDisposition.toString(), "size")
== null)) {
                contentDisposition.append(String.format(Locale.US, ";\n size=%d", size));
}

if (contentDisposition.length() > 0) {
//Synthetic comment -- @@ -937,7 +939,7 @@
}

mConnection.sendCommand(
                        String.format(Locale.US, ImapConstants.APPEND + " \"%s\" (%s) {%d}",
ImapStore.encodeFolderName(mName, mStore.mPathPrefix),
flagList,
out.getCount()), false);
//Synthetic comment -- @@ -982,13 +984,13 @@
}
// Most servers don't care about parenthesis in the search query [and, some
// fail to work if they are used]
                String[] uids = searchForUids(String.format(Locale.US, "HEADER MESSAGE-ID %s", messageId));
if (uids.length > 0) {
message.setUid(uids[0]);
}
// However, there's at least one server [AOL] that fails to work unless there
// are parenthesis, so, try this as a last resort
                uids = searchForUids(String.format(Locale.US, "(HEADER MESSAGE-ID %s)", messageId));
if (uids.length > 0) {
message.setUid(uids[0]);
}
//Synthetic comment -- @@ -1036,7 +1038,7 @@
allFlags = flagList.substring(1);
}
try {
            mConnection.executeSimpleCommand(String.format(Locale.US,
ImapConstants.UID_STORE + " %s %s" + ImapConstants.FLAGS_SILENT + " (%s)",
ImapStore.joinMessageUids(messages),
value ? "+" : "-",
//Synthetic comment -- @@ -1074,7 +1076,7 @@
*/
private void doSelect() throws IOException, MessagingException {
List<ImapResponse> responses = mConnection.executeSimpleCommand(
                String.format(Locale.US, ImapConstants.SELECT + " \"%s\"",
ImapStore.encodeFolderName(mName, mStore.mPathPrefix)));

// Assume the folder is opened read-write; unless we are notified otherwise







