/*MMS: handle null exception - when there is no camcorder.

Change-Id:I128a5860b4fa9843f47dac0f57a53d4d7d6974f9*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index f7d0d79..19b06cd 100644

//Synthetic comment -- @@ -2407,7 +2407,10 @@
//    }

private int getVideoCaptureDurationLimit() {
        CamcorderProfile cp = CamcorderProfile.get(CamcorderProfile.QUALITY_LOW);
        return cp != null ? cp.duration : 0;
        // cp is null if there is no camera, just return 0 - its going to fail
        // anyway.
}

private void addAttachment(int type, boolean replace) {







