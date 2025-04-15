/*Tests/RS: Add RS context check for RsBalls setAccel()

mRS could be null when lock and unlock screen by pressing power key
several times, it causes RsBalls crash. Add null pointer check to fix.

Change-Id:If37dee8609420daaff17772d5194cad5531f98e6Author: Yong Chen <yong.a.chen@intel.com>
Signed-off-by: Yong Chen <yong.a.chen@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 31271*/
//Synthetic comment -- diff --git a/tests/RenderScriptTests/Balls/src/com/example/android/rs/balls/BallsView.java b/tests/RenderScriptTests/Balls/src/com/example/android/rs/balls/BallsView.java
//Synthetic comment -- index b3b3756..041782d 100644

//Synthetic comment -- @@ -105,7 +105,7 @@
}

void setAccel(float x, float y, float z) {
        if (mRender == null) {
return;
}
mRender.setAccel(x, -y);







