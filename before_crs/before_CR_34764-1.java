/*Fix a divide-by-zero case

-modify calculating frame rate per second (FPS)
 to fix divide-by-zero crash issue after too slow task switch.

Signed-off-by: fujita <fujita@brilliantservice.co.jp>*/
//Synthetic comment -- diff --git a/src/com/replica/replicaisland/GameThread.java b/src/com/replica/replicaisland/GameThread.java
//Synthetic comment -- index ad5c08e..7ee47dd 100644

//Synthetic comment -- @@ -83,9 +83,9 @@
if (mProfileTime > PROFILE_REPORT_DELAY * 1000) {
final long averageFrameTime = mProfileTime / mProfileFrames;
DebugLog.d("Game Profile", "Average: " + averageFrameTime);
mProfileTime = 0;
mProfileFrames = 0;
                        mGameRoot.sSystemRegistry.hudSystem.setFPS(1000 / (int)averageFrameTime);
}
}
// If the game logic completed in less than 16ms, that means it's running







