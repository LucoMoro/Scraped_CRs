/*frameworks/base: Use Java's Float method to compare float values

In some cases, float value comparision doesn't yeild desired results
causing more time to be spent in the loop of generate(). The loop is
executed by the focus application's looper thread and subsequent
messages corresponding to key press isn't serviced by the looper
causing a UI Freeze.

Example : Iterations having value of -2368302.0 for position
resulted in a value of 1 for dir. The same results with a value
of -1 with Java's Float.compare method.

Change-Id:I3798eb4c561feba7311855b5e285027080ac2f88*/




//Synthetic comment -- diff --git a/core/java/android/view/ViewRoot.java b/core/java/android/view/ViewRoot.java
//Synthetic comment -- index 03efea9..7aa2d26 100644

//Synthetic comment -- @@ -3156,7 +3156,7 @@
int movement = 0;
nonAccelMovement = 0;
do {
                final int dir = ( Float.compare(position, 0.0f) >= 0 ) ? 1 : -1;
switch (step) {
// If we are going to execute the first step, then we want
// to do this as soon as possible instead of waiting for







