/*Phone: Graphical bugs on Sim unlock menu

Change the ICCPanel GUI layer from TYPE_PRIORITY_PHONE to TYPE_SYSTEM_DIALOG
to avoid the layer mistake.

Change-Id:I25defc8947bbe51c4ce72a4188497396e4766d19Author: Bin Yang <bin.y.yang@intel.com>
Signed-off-by: Frederic Predon <frederic.predon@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 6717*/




//Synthetic comment -- diff --git a/src/com/android/phone/IccPanel.java b/src/com/android/phone/IccPanel.java
//Synthetic comment -- index e603a06..0ebe020 100644

//Synthetic comment -- @@ -41,7 +41,7 @@
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
Window winP = getWindow();
        winP.setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG);
winP.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
WindowManager.LayoutParams.MATCH_PARENT);
winP.setGravity(Gravity.CENTER);







