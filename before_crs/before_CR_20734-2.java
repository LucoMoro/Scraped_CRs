/*Set timeout for blocking read in non-TSL/SSL POP3.

If connection is lost while reading data from socket,
the thread blocks and subsequent commands will never
have a chance to run. UI progress will keep spinning
for a long time.

Change-Id:I5a3d096c8eb5d923f58be512670c7aabd2e4faea*/
//Synthetic comment -- diff --git a/src/com/android/email/mail/store/Pop3Store.java b/src/com/android/email/mail/store/Pop3Store.java
//Synthetic comment -- index 771354e..088fe31 100644

//Synthetic comment -- @@ -235,6 +235,7 @@

try {
mTransport.open();

// Eat the banner
executeSimpleCommand(null);







