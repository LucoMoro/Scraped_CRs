/*Signed-off-by: Kenji Hirahara <kddi.tech.ke.hirahara@gmail.com>
Add FLAG_INCLUDE_STOPPED_PACKAGES flag to Wap Push.

We added FLAG_INCLUDE_STOPPED_PACKAGES flag to Wap Push broadcast intent.
FLAG_EXCLUDE_STOPPED_PACKAGES flag has been added to all the broadcast
intents since Android3.1 to prevent inadvertent or unnecessary launch of
stopped or never-launched applications.
But Wap Push is a reliable service, and it does not launch application
inadvertently.
So, it is better for users to allow WapPush Services to launch stopped
applications by adding FLAG_INCLUDE_STOPPED_PACKAGES.
For example, users can find lost device by using search application
even if the application is in stop state.

Change-Id:I4b09de96545a5d390940918526bdf1cc8225118f*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/WapPushOverSms.java b/src/java/com/android/internal/telephony/WapPushOverSms.java
//Synthetic comment -- index 1b174ab..558c503 100755

//Synthetic comment -- @@ -263,6 +263,7 @@
}

Intent intent = new Intent(Intents.WAP_PUSH_RECEIVED_ACTION);
intent.setType(mimeType);
intent.putExtra("transactionId", transactionId);
intent.putExtra("pduType", pduType);







