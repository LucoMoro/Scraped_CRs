/*Audio: Fix abnormal keytone playback during silent mode changing

Change-Id:Icfc4003c9756b3c33578c137e59dc4a2b4f25647Author: Chenyang Du<chenyang.du@intel.com>
Signed-off-by: Ertao Zhao <ertaox.zhao@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 19084, 4898*/




//Synthetic comment -- diff --git a/core/java/android/widget/AdapterView.java b/core/java/android/widget/AdapterView.java
//Synthetic comment -- index 502de31..f386dd6 100644

//Synthetic comment -- @@ -291,11 +291,11 @@
*/
public boolean performItemClick(View view, int position, long id) {
if (mOnItemClickListener != null) {
if (view != null) {
view.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);
}
mOnItemClickListener.onItemClick(this, view, position, id);
            playSoundEffect(SoundEffectConstants.CLICK);
return true;
}








