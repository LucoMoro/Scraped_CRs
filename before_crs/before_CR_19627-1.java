/*Handle WebChromeClient null in CallbackProxy

CallbackProxy's handleMessage function needs to handle
the case when WebChromeClient is null for message
ADD_MESSAGE_TO_CONSOLE.

WebChromeClient can be null if WebChromeClient is set to
null while the ADD_MESSAGE_TO_CONSOLE is in the message queue.
It's unlikely to happen but has happened since the
issue was found by live user.

Change-Id:I183ee52bc78440d262f225c69118cd450787bd4d*/
//Synthetic comment -- diff --git a/core/java/android/webkit/CallbackProxy.java b/core/java/android/webkit/CallbackProxy.java
//Synthetic comment -- index d65c106..c7dfd32 100644

//Synthetic comment -- @@ -689,7 +689,8 @@
ConsoleMessage.MessageLevel messageLevel =
ConsoleMessage.MessageLevel.values()[msgLevel];

                if (!mWebChromeClient.onConsoleMessage(new ConsoleMessage(message, sourceID,
lineNumber, messageLevel))) {
// If false was returned the user did not provide their own console function so
//  we should output some default messages to the system log.







