/*NPE safeguard. DO NOT MERGE

Change-Id:I4d7a3d449a120e1e1ab86f99254daf0e58165ab4*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MarqueeGesture.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MarqueeGesture.java
//Synthetic comment -- index c374b0e..b6f47bb 100644

//Synthetic comment -- @@ -65,6 +65,10 @@

@Override
public void update(ControlPoint pos) {
        if (mOverlay == null) {
            return;
        }

int x = Math.min(pos.x, mStart.x);
int y = Math.min(pos.y, mStart.y);
int w = Math.abs(pos.x - mStart.x);







