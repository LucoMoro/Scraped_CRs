/*Trim the mime type portion of the Content-Type header to allow "text/plain ; charset=iso-8859-1"*/
//Synthetic comment -- diff --git a/src/com/android/email/mail/internet/MimeUtility.java b/src/com/android/email/mail/internet/MimeUtility.java
//Synthetic comment -- index 1b0cb9f..2e106df 100644

//Synthetic comment -- @@ -189,7 +189,7 @@
}
String[] parts = unfold(header).split(";");
if (name == null) {
            return parts[0];
}
String lowerCaseName = name.toLowerCase();
for (String part : parts) {







