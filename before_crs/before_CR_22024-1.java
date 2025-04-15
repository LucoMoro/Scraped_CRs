/*Correctly calculate the FastScroller's scrollTo position when using
ExpandableListView.

The current code calculates the target as a flat list position, but then
incorrectly uses it as a group index by passing it to
getPackedPositionForGroup(). As a result, if there are expanded items,
we will potentially scroll to the bottom of the list much too early.

This patch assumes that the desired behavior is having the fast scroll
feature target the individual groups. The alternative would be to target
each individual flat list item while scrolling.

Change-Id:Ie62f781c26edcf3073d7818cd37eb9fd4a8aa14d*/
//Synthetic comment -- diff --git a/core/java/android/widget/FastScroller.java b/core/java/android/widget/FastScroller.java
//Synthetic comment -- index aa68a74..66b8171 100644

//Synthetic comment -- @@ -34,7 +34,7 @@
* Helper class for AbsListView to draw and control the Fast Scroll thumb
*/
class FastScroller {
   
// Minimum number of pages to justify showing a fast scroll thumb
private static int MIN_PAGES = 4;
// Scroll thumb not showing
//Synthetic comment -- @@ -47,7 +47,7 @@
private static final int STATE_DRAGGING = 3;
// Scroll thumb fading out due to inactivity timeout
private static final int STATE_EXIT = 4;
    
private Drawable mThumbDrawable;
private Drawable mOverlayDrawable;

//Synthetic comment -- @@ -65,21 +65,21 @@
private int mListOffset;
private int mItemCount = -1;
private boolean mLongList;
    
private Object [] mSections;
private String mSectionText;
private boolean mDrawOverlay;
private ScrollFade mScrollFade;
    
private int mState;
    
private Handler mHandler = new Handler();
    
private BaseAdapter mListAdapter;
private SectionIndexer mSectionIndexer;

private boolean mChangedBounds;
    
public FastScroller(Context context, AbsListView listView) {
mList = listView;
init(context);
//Synthetic comment -- @@ -106,18 +106,18 @@
}
mState = state;
}
    
public int getState() {
return mState;
}
    
private void resetThumbPos() {
final int viewWidth = mList.getWidth();
// Bounds are always top right. Y coordinate get's translated during draw
mThumbDrawable.setBounds(viewWidth - mThumbW, 0, viewWidth, mThumbH);
mThumbDrawable.setAlpha(ScrollFade.ALPHA_MAX);
}
    
private void useThumbDrawable(Context context, Drawable drawable) {
mThumbDrawable = drawable;
mThumbW = context.getResources().getDimensionPixelSize(
//Synthetic comment -- @@ -132,10 +132,10 @@
final Resources res = context.getResources();
useThumbDrawable(context, res.getDrawable(
com.android.internal.R.drawable.scrollbar_handle_accelerated_anim2));
        
mOverlayDrawable = res.getDrawable(
com.android.internal.R.drawable.menu_submenu_background);
        
mScrollCompleted = true;

getSectionsFromIndexer();
//Synthetic comment -- @@ -148,7 +148,7 @@
mPaint.setAntiAlias(true);
mPaint.setTextAlign(Paint.Align.CENTER);
mPaint.setTextSize(mOverlaySize / 2);
        TypedArray ta = context.getTheme().obtainStyledAttributes(new int[] { 
android.R.attr.textColorPrimary });
ColorStateList textColor = ta.getColorStateList(ta.getIndex(0));
int textColorNormal = textColor.getDefaultColor();
//Synthetic comment -- @@ -159,20 +159,20 @@
if (mList.getWidth() > 0 && mList.getHeight() > 0) {
onSizeChanged(mList.getWidth(), mList.getHeight(), 0, 0);
}
        
mState = STATE_NONE;
}
    
void stop() {
setState(STATE_NONE);
}
    
boolean isVisible() {
return !(mState == STATE_NONE);
}
    
public void draw(Canvas canvas) {
        
if (mState == STATE_NONE) {
// No need to draw anything
return;
//Synthetic comment -- @@ -209,7 +209,7 @@
if (alpha == 0) { // Done with exit
setState(STATE_NONE);
} else {
                mList.invalidate(viewWidth - mThumbW, y, viewWidth, y + mThumbH);            
}
}
}
//Synthetic comment -- @@ -228,8 +228,8 @@
(int) pos.right, (int) pos.bottom);
}
}
    
    void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, 
int totalItemCount) {
// Are there enough pages to require fast scroll? Recompute only if total count changes
if (mItemCount != totalItemCount && visibleItemCount > 0) {
//Synthetic comment -- @@ -243,7 +243,7 @@
return;
}
if (totalItemCount - visibleItemCount > 0 && mState != STATE_DRAGGING ) {
            mThumbY = ((mList.getHeight() - mThumbH) * firstVisibleItem) 
/ (totalItemCount - visibleItemCount);
if (mChangedBounds) {
resetThumbPos();
//Synthetic comment -- @@ -291,7 +291,7 @@
mListAdapter = (BaseAdapter) adapter;
mSectionIndexer = (SectionIndexer) adapter;
mSections = mSectionIndexer.getSections();
                
} else {
mListAdapter = (BaseAdapter) adapter;
mSections = new String[] { " " };
//Synthetic comment -- @@ -328,7 +328,7 @@
if (section < nSections - 1) {
nextIndex = mSectionIndexer.getPositionForSection(section + 1);
}
            
// Find the previous index if we're slicing the previous section
if (nextIndex == index) {
// Non-existent letter
//Synthetic comment -- @@ -348,9 +348,9 @@
}
}
// Find the next index, in case the assumed next index is not
            // unique. For instance, if there is no P, then request for P's 
// position actually returns Q's. So we need to look ahead to make
            // sure that there is really a Q at Q's position. If not, move 
// further down...
int nextNextSection = nextSection + 1;
while (nextNextSection < nSections &&
//Synthetic comment -- @@ -360,18 +360,18 @@
}
// Compute the beginning and ending scroll range percentage of the
// currently visible letter. This could be equal to or greater than
            // (1 / nSections). 
float fPrev = (float) prevSection / nSections;
float fNext = (float) nextSection / nSections;
if (prevSection == exactSection && position - fPrev < fThreshold) {
index = prevIndex;
} else {
                index = prevIndex + (int) ((nextIndex - prevIndex) * (position - fPrev) 
/ (fNext - fPrev));
}
// Don't overflow
if (index > count - 1) index = count - 1;
            
if (mList instanceof ExpandableListView) {
ExpandableListView expList = (ExpandableListView) mList;
expList.setSelectionFromTop(expList.getFlatListPosition(
//Synthetic comment -- @@ -382,15 +382,19 @@
mList.setSelection(index + mListOffset);
}
} else {
            int index = (int) (position * count);
if (mList instanceof ExpandableListView) {
ExpandableListView expList = (ExpandableListView) mList;
expList.setSelectionFromTop(expList.getFlatListPosition(
ExpandableListView.getPackedPositionForGroup(index + mListOffset)), 0);
            } else if (mList instanceof ListView) {
                ((ListView)mList).setSelectionFromTop(index + mListOffset, 0);
            } else {
                mList.setSelection(index + mListOffset);
}
sectionIndex = -1;
}
//Synthetic comment -- @@ -410,7 +414,7 @@
mList.onTouchEvent(cancelFling);
cancelFling.recycle();
}
    
boolean onInterceptTouchEvent(MotionEvent ev) {
if (mState > STATE_NONE && ev.getAction() == MotionEvent.ACTION_DOWN) {
if (isPointInside(ev.getX(), ev.getY())) {
//Synthetic comment -- @@ -486,18 +490,18 @@
}

public class ScrollFade implements Runnable {
        
long mStartTime;
long mFadeDuration;
static final int ALPHA_MAX = 208;
static final long FADE_DURATION = 200;
        
void startFade() {
mFadeDuration = FADE_DURATION;
mStartTime = SystemClock.uptimeMillis();
setState(STATE_EXIT);
}
        
int getAlpha() {
if (getState() != STATE_EXIT) {
return ALPHA_MAX;
//Synthetic comment -- @@ -507,17 +511,17 @@
if (now > mStartTime + mFadeDuration) {
alpha = 0;
} else {
                alpha = (int) (ALPHA_MAX - ((now - mStartTime) * ALPHA_MAX) / mFadeDuration); 
}
return alpha;
}
        
public void run() {
if (getState() != STATE_EXIT) {
startFade();
return;
}
            
if (getAlpha() > 0) {
mList.invalidate();
} else {







