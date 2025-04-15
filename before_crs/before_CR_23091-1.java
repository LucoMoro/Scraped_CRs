/*NPE safeguard

Change-Id:I3f55c21f79ad66bed1329a2f5882deb94e93be62*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MarqueeGesture.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MarqueeGesture.java
//Synthetic comment -- index c374b0e..b6f47bb 100644

//Synthetic comment -- @@ -65,6 +65,10 @@

@Override
public void update(ControlPoint pos) {
int x = Math.min(pos.x, mStart.x);
int y = Math.min(pos.y, mStart.y);
int w = Math.abs(pos.x - mStart.x);







