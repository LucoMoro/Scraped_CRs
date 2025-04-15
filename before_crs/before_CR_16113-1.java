/*ADT GLE2: minor unit tests

Change-Id:I231b272363bf1d9652093c747fd2db865f1658c7*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/editors/layout/gscripts/Rect.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/editors/layout/gscripts/Rect.java
//Synthetic comment -- index b09f8f9..1f5948a 100755

//Synthetic comment -- @@ -50,21 +50,24 @@
}

/** Initialize rectangle to the given values. They can be invalid. */
    public void set(int x, int y, int w, int h) {
this.x = x;
this.y = y;
this.w = w;
this.h = h;
}

/** Initialize rectangle to match the given one. */
    public void set(Rect r) {
set(r.x, r.y, r.w, r.h);
}

/** Initialize rectangle to match the given one. */
    public void set(Rectangle swtRect) {
set(swtRect.x, swtRect.y, swtRect.width, swtRect.height);
}

/** Returns a new instance of a rectangle with the same values. */
//Synthetic comment -- @@ -88,7 +91,7 @@

/** Returns true if the rectangle contains the x,y coordinates, borders included. */
public boolean contains(Point p) {
        return contains(p.x, p.y);
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/editors/layout/gscripts/PointTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/editors/layout/gscripts/PointTest.java
new file mode 100755
//Synthetic comment -- index 0000000..b37995d

//Synthetic comment -- @@ -0,0 +1,121 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/editors/layout/gscripts/RectTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/editors/layout/gscripts/RectTest.java
new file mode 100755
//Synthetic comment -- index 0000000..b8d78dd

//Synthetic comment -- @@ -0,0 +1,283 @@







