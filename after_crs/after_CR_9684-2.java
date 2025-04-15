/*Lazily allocate the mBounds Rect in Drawable.

For background, see:http://kohlerm.blogspot.com/2009/04/analyzing-memory-usage-off-your-android.htmlThanks, Markus!*/




//Synthetic comment -- diff --git a/graphics/java/android/graphics/drawable/Drawable.java b/graphics/java/android/graphics/drawable/Drawable.java
//Synthetic comment -- index 42e28e8..ca0ed7e 100644

//Synthetic comment -- @@ -97,10 +97,12 @@
*/
public abstract class Drawable {

    private static final Rect ZERO_BOUNDS_RECT = new Rect();

private int[] mStateSet = StateSet.WILD_CARD;
private int mLevel = 0;
private int mChangingConfigurations = 0;
    private Rect mBounds = ZERO_BOUNDS_RECT;  // lazily becomes a new Rect()
/*package*/ Callback mCallback = null;
private boolean mVisible = true;

//Synthetic comment -- @@ -118,7 +120,11 @@
*/
public void setBounds(int left, int top, int right, int bottom) {
Rect oldBounds = mBounds;

        if (oldBounds == ZERO_BOUNDS_RECT) {
            oldBounds = mBounds = new Rect();
        }

if (oldBounds.left != left || oldBounds.top != top ||
oldBounds.right != right || oldBounds.bottom != bottom) {
mBounds.set(left, top, right, bottom);







