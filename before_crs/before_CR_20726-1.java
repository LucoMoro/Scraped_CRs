/*fix mms crash when large video file is sent

If we try to attach a video with size more than allowed by
CarrierContentRestriction in the Messaging  application
(ComposeMessageActivity), the app crashes since it tries to delete a
message from database with improper URI (null value). Prevent the
deletion of message with null URI in correctAttachmentSize()

Change-Id:Icc08419accc23f13e80376b2078b2494815ee87fSigned-off-by: Madan Ankapura <mankapur@sta.samsung.com>*/
//Synthetic comment -- diff --git a/src/com/android/mms/data/WorkingMessage.java b/src/com/android/mms/data/WorkingMessage.java
//Synthetic comment -- index 9dd76bb..00fa740 100644

//Synthetic comment -- @@ -234,8 +234,10 @@
if (slideCount == 0) {
mAttachmentType = TEXT;
mSlideshow = null;
            asyncDelete(mMessageUri, null, null);
            mMessageUri = null;
} else if (slideCount > 1) {
mAttachmentType = SLIDESHOW;
} else {







