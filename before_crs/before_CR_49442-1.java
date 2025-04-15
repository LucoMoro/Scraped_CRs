/*Tests/RS: Fix rs fountain display problem

The radius of fireworks are determinated by pressure value.
This fix normalizes pressure, so rs Fountain can display normally
with different ranges of pressure on different target devices.

Change-Id:Ic1b34fa56762412ca10f4ba5f5af00fdc368e0a9Author: Yong Chen <yong.a.chen@intel.com>
Signed-off-by: Yong Chen <yong.a.chen@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 72018*/
//Synthetic comment -- diff --git a/tests/RenderScriptTests/Fountain/src/com/example/android/rs/fountain/FountainRS.java b/tests/RenderScriptTests/Fountain/src/com/example/android/rs/fountain/FountainRS.java
//Synthetic comment -- index 646c807..5e69fc0 100644

//Synthetic comment -- @@ -30,6 +30,8 @@
private Resources mRes;
private RenderScriptGL mRS;
private ScriptC_fountain mScript;
public void init(RenderScriptGL rs, Resources res) {
mRS = rs;
mRes = res;
//Synthetic comment -- @@ -57,10 +59,12 @@
if (id >= holdingColor.length) {
return;
}
        int rate = (int)(pressure * pressure * 500.f);
        if (rate > 500) {
            rate = 500;
}
if (rate > 0) {
mScript.invoke_addParticles(rate, x, y, id, !holdingColor[id]);
holdingColor[id] = true;








//Synthetic comment -- diff --git a/tests/RenderScriptTests/FountainFbo/src/com/example/android/rs/fountainfbo/FountainFboRS.java b/tests/RenderScriptTests/FountainFbo/src/com/example/android/rs/fountainfbo/FountainFboRS.java
//Synthetic comment -- index 3bf3ff1..b1efaab 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
private Allocation mColorBuffer;
private ProgramFragment mProgramFragment;
private ProgramFragment mTextureProgramFragment;
public void init(RenderScriptGL rs, Resources res) {
mRS = rs;
mRes = res;
//Synthetic comment -- @@ -83,10 +84,11 @@
if (id >= holdingColor.length) {
return;
}
        int rate = (int)(pressure * pressure * 500.f);
        if (rate > 500) {
            rate = 500;
}
if (rate > 0) {
mScript.invoke_addParticles(rate, x, y, id, !holdingColor[id]);
holdingColor[id] = true;







