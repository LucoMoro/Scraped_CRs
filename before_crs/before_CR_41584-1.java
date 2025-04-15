/*Fix ANR happens in "com.android.musicfx" at "keyDispatchingTimedOut"

Need to replace synchronously method commit() with asynchronous method apply() when change preference values.

Change-Id:I1775fc5efa4056fd6a9f5ba2fe399dc97e9f670bAuthor: b359 <b359@borqs.com>
Signed-off-by: Hongyu Zhang <hongyu.zhang@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 36235*/
//Synthetic comment -- diff --git a/src/com/android/musicfx/ControlPanelEffect.java b/src/com/android/musicfx/ControlPanelEffect.java
//Synthetic comment -- index 8abb4f3..db46c3e 100644

//Synthetic comment -- @@ -1036,7 +1036,7 @@
// Exit after the virtualizer has been re-enabled

if (isExistingAudioSession) {
            editor.commit();
return;
}








