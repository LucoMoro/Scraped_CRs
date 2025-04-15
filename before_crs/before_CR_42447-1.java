/*Mms: JAVACRASH in com.android.mms at android.os.AsyncTask$3.done

TestCase:
    1>"%1$" displays when Restore to default in SMS settings
    2> Fore close when there is no app that start activity
    3> manage sim messages
    4> Force close happened when open a MMS with vcard
    5>  Audio is inserted to MMS, and then user play it. If long pressing
    progress bar of the audio playback until play finished, messaging will crash

Change-Id:I04eec747029fe4d6d73e290dfa682c949a5930a8Author: b497 <b497@borqs.com>
Signed-off-by: Fang, JiuX <jiux.fang@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Beare, Bruce J <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 25327*/
//Synthetic comment -- diff --git a/src/com/android/mms/model/SlideModel.java b/src/com/android/mms/model/SlideModel.java
//Synthetic comment -- index 98c66f0..48c9d7c 100644

//Synthetic comment -- @@ -233,6 +233,9 @@
public void decreaseSlideSize(int decreaseSize) {
if (decreaseSize > 0) {
mSlideSize -= decreaseSize;
}
}

//Synthetic comment -- @@ -252,6 +255,9 @@
if ((decreaseSize > 0) && (null != mParent)) {
int size = mParent.getCurrentMessageSize();
size -= decreaseSize;
mParent.setCurrentMessageSize(size);
}
}







