/*Viewpager RTL support

Hey,
I am trying to add RTL support into View Pager class and
I've encountered a problem. The relative layout direction
methods are hidden, therefore I can't use them inside the
support library. I tried anything I know including java
reflection, I tried it with "isLayoutRtl" and
"getLayoutDirecion". But it didn't help a lot, because RTL
layout was set to false (I guess its the legacy support).
;
I even tried to get the layout direction (with reflection)
through "getConfigutation" method and It worked, the only
problem was that is was set by the system's locale, and
not by the app's layout direcion.
;
My latest idea that I have'nt tried yet is to move it into
android.widget, but I think it will create a problem with
older systems, as it won't be inside their frameworks.
Any other, better, ideas? or at least a way to support older
versions of Andriod?
;
P.S: About the code itself, I know it is messy, mostly
because of my trials and errors with setting layout
direction. It also might not compile as I am short in space
in my computer's HDD. I have ported it from my CyanogenMod's
commit which I, of course, compiled.
I'll fix those things after I will have a clue about the
layout direction problem. I am also new to android
development, so it might be a stupid, easy answer.
;
Thanks,
Or

Change-Id:Ib9999193fa22e1b2b60931fcf763980a772367b6*/




//Synthetic comment -- diff --git a/v4/java/android/support/v4/view/PagerTabStrip.java b/v4/java/android/support/v4/view/PagerTabStrip.java
//Synthetic comment -- index 21488b8..3fa23f9 100644

//Synthetic comment -- @@ -242,9 +242,9 @@

case MotionEvent.ACTION_UP:
if (x < mCurrText.getLeft() - mTabPadding) {
                    mPager.setCurrentItem(mPager.getCurrentItem() - (mPager.isRtl() ? - 1 : 1));
} else if (x > mCurrText.getRight() + mTabPadding) {
                    mPager.setCurrentItem(mPager.getCurrentItem() + (mPager.isRtl() ? - 1 : 1));
}
break;
}








//Synthetic comment -- diff --git a/v4/java/android/support/v4/view/PagerTitleStrip.java b/v4/java/android/support/v4/view/PagerTitleStrip.java
//Synthetic comment -- index 79c771f..dce264c 100644

//Synthetic comment -- @@ -404,12 +404,13 @@
mCurrText.layout(currLeft, currTop, currRight,
currTop + mCurrText.getMeasuredHeight());

        final int prevLeft = mPager.isRtl() ? Math.max(stripWidth - paddingRight - prevWidth, currRight + mScaledTextSpacing):
            Math.min(paddingLeft, currLeft - mScaledTextSpacing - prevWidth);
mPrevText.layout(prevLeft, prevTop, prevLeft + prevWidth,
prevTop + mPrevText.getMeasuredHeight());

        final int nextLeft = mPager.isRtl() ? Math.min(paddingLeft, currLeft - mScaledTextSpacing - nextWidth):
            Math.max(stripWidth - paddingRight - nextWidth, currRight + mScaledTextSpacing);
mNextText.layout(nextLeft, nextTop, nextLeft + nextWidth,
nextTop + mNextText.getMeasuredHeight());

//Synthetic comment -- @@ -475,7 +476,12 @@
public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
if (positionOffset > 0.5f) {
// Consider ourselves to be on the next page when we're 50% of the way there.
                if(mPager.isRtl()){
                    position--;
                }
                else{
                    position++;
                }
}
updateTextPositions(position, positionOffset, false);
}








//Synthetic comment -- diff --git a/v4/java/android/support/v4/view/ViewPager.java b/v4/java/android/support/v4/view/ViewPager.java
//Synthetic comment -- index 966fcbcd..009d6ab 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package android.support.v4.view;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
//Synthetic comment -- @@ -140,8 +141,8 @@
// Offsets of the first and last items, if known.
// Set during population, used to determine if we are at the beginning
// or end of the pager data set during touch scrolling.
    private float mLeftEdgeOffset = -Float.MAX_VALUE;
    private float mRightEdgeOffset = Float.MAX_VALUE;

private int mChildWidthMeasureSpec;
private int mChildHeightMeasureSpec;
//Synthetic comment -- @@ -201,12 +202,14 @@
private boolean mNeedCalculatePageOffsets = false;
private boolean mCalledSuper;
private int mDecorChildCount;
    private boolean mRtlLayout = false;

private OnPageChangeListener mOnPageChangeListener;
private OnPageChangeListener mInternalPageChangeListener;
private OnAdapterChangeListener mAdapterChangeListener;
private PageTransformer mPageTransformer;
private Method mSetChildrenDrawingOrderEnabled;
    private Method mGetLayoutDirection;

private static final int DRAW_ORDER_DEFAULT = 0;
private static final int DRAW_ORDER_FORWARD = 1;
//Synthetic comment -- @@ -360,6 +363,7 @@
mFlingDistance = (int) (MIN_DISTANCE_FOR_FLING * density);
mCloseEnough = (int) (CLOSE_ENOUGH * density);
mDefaultGutterSize = (int) (DEFAULT_GUTTER_SIZE * density);
        mRtlLayout = isLayoutRTL();

ViewCompat.setAccessibilityDelegate(this, new MyAccessibilityDelegate());

//Synthetic comment -- @@ -370,6 +374,10 @@
}
}

    public boolean isRtl(){
        return mRtlLayout;
    }

@Override
protected void onDetachedFromWindow() {
removeCallbacks(mEndScrollRunnable);
//Synthetic comment -- @@ -534,8 +542,8 @@
int destX = 0;
if (curInfo != null) {
final int width = getClientWidth();
            destX = (int) (width * Math.max(mLeftEdgeOffset,
                    Math.min(curInfo.offset, mRightEdgeOffset)));
}
if (smoothScroll) {
smoothScrollTo(destX, 0, velocity);
//Synthetic comment -- @@ -610,6 +618,23 @@
}
}

    boolean isLayoutRTL(){
        if(mGetLayoutDirection == null) {
            try {
                mGetLayoutDirection = Configuration.class.getDeclaredMethod("getLayoutDirection");
            } catch (NoSuchMethodException e) {
                Log.e(TAG, "Can't find getLayoutDirection", e);
                return mRtlLayout;
            }
        }
        try {
            return (Integer) mGetLayoutDirection.invoke(getContext().getResources().getConfiguration()) == 1;//LAYOUT_DIRECTION_RTL
        } catch (Exception e) {
            Log.e(TAG, "Error getting layout direction", e);
            return mRtlLayout;
        }
    }

@Override
protected int getChildDrawingOrder(int childCount, int i) {
final int index = mDrawingOrder == DRAW_ORDER_REVERSE ? childCount - 1 - i : i;
//Synthetic comment -- @@ -1075,7 +1100,8 @@
if (oldCurPosition < curItem.position) {
int itemIndex = 0;
ItemInfo ii = null;
                float offset = (mRtlLayout ? oldCurInfo.offset:
                    oldCurInfo.offset + oldCurInfo.widthFactor + marginOffset);
for (int pos = oldCurPosition + 1;
pos <= curItem.position && itemIndex < mItems.size(); pos++) {
ii = mItems.get(itemIndex);
//Synthetic comment -- @@ -1086,16 +1112,22 @@
while (pos < ii.position) {
// We don't have an item populated for this,
// ask the adapter for an offset.
                        offset += (mAdapter.getPageWidth(pos) + marginOffset) * (mRtlLayout ? -1 : 1);
pos++;
}
                    if(mRtlLayout){
                        offset -= ii.widthFactor + marginOffset;
                        ii.offset = offset;
                    } else {
                        ii.offset = offset;
                        offset += ii.widthFactor + marginOffset;
                    }
}
} else if (oldCurPosition > curItem.position) {
int itemIndex = mItems.size() - 1;
ItemInfo ii = null;
                float offset = (mRtlLayout ? oldCurInfo.offset + oldCurInfo.widthFactor + marginOffset:
                    oldCurInfo.offset);
for (int pos = oldCurPosition - 1;
pos >= curItem.position && itemIndex >= 0; pos--) {
ii = mItems.get(itemIndex);
//Synthetic comment -- @@ -1106,45 +1138,80 @@
while (pos > ii.position) {
// We don't have an item populated for this,
// ask the adapter for an offset.
                        offset -= (mAdapter.getPageWidth(pos) + marginOffset) * (mRtlLayout ? -1 : 1);
pos--;
}
                    if(mRtlLayout){
                        ii.offset = offset;
                        offset += ii.widthFactor + marginOffset;
                    } else {
                        offset -= ii.widthFactor + marginOffset;
                        ii.offset = offset;
                    }
}
}
}

// Base all offsets off of curItem.
final int itemCount = mItems.size();
        float offset = (mRtlLayout ? curItem.offset + curItem.widthFactor + marginOffset:
            curItem.offset);
int pos = curItem.position - 1;
        mLeftEdgeOffset = curItem.position == (mRtlLayout ? N - 1 : 0) ? curItem.offset : -Float.MAX_VALUE;
        mRightEdgeOffset = curItem.position == (mRtlLayout ? 0 : N - 1) ?
curItem.offset + curItem.widthFactor - 1 : Float.MAX_VALUE;
// Previous pages
        if(mRtlLayout){
            for (int i = curIndex - 1; i >= 0; i--, pos--) {
                final ItemInfo ii = mItems.get(i);
                while (pos > ii.position) {
                    offset += mAdapter.getPageWidth(pos--) + marginOffset;
                }
                ii.offset = offset;
                if (ii.position == 0) {
                    mRightEdgeOffset = offset + ii.widthFactor - 1;
                }
                offset += ii.widthFactor + marginOffset;
}
}
        else{
            for (int i = curIndex - 1; i >= 0; i--, pos--) {
                final ItemInfo ii = mItems.get(i);
                while (pos > ii.position) {
                    offset -= mAdapter.getPageWidth(pos--) + marginOffset;
                }
                offset -= ii.widthFactor + marginOffset;
                ii.offset = offset;
                if (ii.position == 0) mLeftEdgeOffset = offset;
            }
        }
        offset = (mRtlLayout ? curItem.offset:
            curItem.offset + curItem.widthFactor + marginOffset);
pos = curItem.position + 1;
// Next pages
        if(mRtlLayout){
            for (int i = curIndex + 1; i < itemCount; i++, pos++) {
                final ItemInfo ii = mItems.get(i);
                while (pos < ii.position) {
                    offset -= mAdapter.getPageWidth(pos++) + marginOffset;
                }
                offset -= ii.widthFactor + marginOffset;
                ii.offset = offset;
                if (ii.position == N - 1) mLeftEdgeOffset = offset;
}
        }
        else{
            for (int i = curIndex + 1; i < itemCount; i++, pos++) {
                final ItemInfo ii = mItems.get(i);
                while (pos < ii.position) {
                    offset += mAdapter.getPageWidth(pos++) + marginOffset;
                }
                if (ii.position == N - 1) {
                    mRightEdgeOffset = offset + ii.widthFactor - 1;
                }
                ii.offset = offset;
                offset += ii.widthFactor + marginOffset;
}
}

mNeedCalculatePageOffsets = false;
//Synthetic comment -- @@ -1420,7 +1487,7 @@
}
} else {
final ItemInfo ii = infoForPosition(mCurItem);
            final float scrollOffset = ii != null ? Math.min(ii.offset, mRightEdgeOffset) : 0;
final int scrollPos = (int) (scrollOffset *
(width - getPaddingLeft() - getPaddingRight()));
if (scrollPos != getScrollX()) {
//Synthetic comment -- @@ -1992,20 +2059,32 @@
float scrollX = oldScrollX + deltaX;
final int width = getClientWidth();

        float leftBound = width * mLeftEdgeOffset;
        float rightBound = width * mRightEdgeOffset;
boolean leftAbsolute = true;
boolean rightAbsolute = true;

final ItemInfo firstItem = mItems.get(0);
final ItemInfo lastItem = mItems.get(mItems.size() - 1);
if (firstItem.position != 0) {
            if(mRtlLayout){
                rightAbsolute = false;
                rightBound = firstItem.offset * width;
            }
            else{
                leftAbsolute = false;
                leftBound = firstItem.offset * width;
            }
}
if (lastItem.position != mAdapter.getCount() - 1) {
            if(mRtlLayout){
                leftAbsolute = false;
                leftBound = lastItem.offset * width;
            }
            else{
                rightAbsolute = false;
                rightBound = lastItem.offset * width;
            }
}

if (scrollX < leftBound) {
//Synthetic comment -- @@ -2049,7 +2128,7 @@
if (!first && ii.position != lastPos + 1) {
// Create a synthetic item for a missing page.
ii = mTempItem;
                ii.offset = lastOffset + (lastWidth + marginOffset) * (mRtlLayout ? -1 : 1);
ii.position = lastPos + 1;
ii.widthFactor = mAdapter.getPageWidth(ii.position);
i--;
//Synthetic comment -- @@ -2058,12 +2137,22 @@

final float leftBound = offset;
final float rightBound = offset + ii.widthFactor + marginOffset;
            if(mRtlLayout){
                if (first || scrollOffset < rightBound) {
                    if (scrollOffset >= leftBound || i == mItems.size() - 1) {
                        return ii;
                    }
                } else {
                    return lastItem;
}
} else {
                if (first || scrollOffset >= leftBound) {
                    if (scrollOffset < rightBound || i == mItems.size() - 1) {
                        return ii;
                    }
                } else {
                    return lastItem;
                }
}
first = false;
lastPos = ii.position;
//Synthetic comment -- @@ -2077,15 +2166,28 @@

private int determineTargetPage(int currentPage, float pageOffset, int velocity, int deltaX) {
int targetPage;
        if(mRtlLayout){
                if (Math.abs(deltaX) > mFlingDistance && Math.abs(velocity) > mMinimumVelocity) {
                    targetPage = velocity > 0 ? currentPage : currentPage - 1;
                } else if (mSeenPositionMin >= 0 && mSeenPositionMin < currentPage && pageOffset < 0.5f) {
                    targetPage = currentPage - 1;
                } else if (mSeenPositionMax >= 0 && mSeenPositionMax > currentPage + 1 &&
                        pageOffset >= 0.5f) {
                    targetPage = currentPage + 1;
                } else {
                    targetPage = (int) (currentPage + 1 - pageOffset - 0.5f);//Not sure about this one
                }
            } else {
                if (Math.abs(deltaX) > mFlingDistance && Math.abs(velocity) > mMinimumVelocity) {
                    targetPage = velocity > 0 ? currentPage : currentPage + 1;
                } else if (mSeenPositionMin >= 0 && mSeenPositionMin < currentPage && pageOffset < 0.5f) {
                    targetPage = currentPage + 1;
                } else if (mSeenPositionMax >= 0 && mSeenPositionMax > currentPage + 1 &&
                        pageOffset >= 0.5f) {
                    targetPage = currentPage - 1;
                } else {
                    targetPage = (int) (currentPage + pageOffset + 0.5f);
                }
}

if (mItems.size() > 0) {
//Synthetic comment -- @@ -2114,7 +2216,7 @@
final int width = getWidth();

canvas.rotate(270);
                canvas.translate(-height + getPaddingTop(), mLeftEdgeOffset * width);
mLeftEdge.setSize(height, width);
needsInvalidate |= mLeftEdge.draw(canvas);
canvas.restoreToCount(restoreCount);
//Synthetic comment -- @@ -2125,7 +2227,7 @@
final int height = getHeight() - getPaddingTop() - getPaddingBottom();

canvas.rotate(90);
                canvas.translate(-getPaddingTop(), -(mRightEdgeOffset + 1) * width);
mRightEdge.setSize(height, width);
needsInvalidate |= mRightEdge.draw(canvas);
canvas.restoreToCount(restoreCount);
//Synthetic comment -- @@ -2165,11 +2267,11 @@
float drawAt;
if (pos == ii.position) {
drawAt = (ii.offset + ii.widthFactor) * width;
                    offset = ii.offset + (ii.widthFactor + marginOffset) * (mRtlLayout ? -1 : 1);
} else {
float widthFactor = mAdapter.getPageWidth(pos);
drawAt = (offset + widthFactor) * width;
                    offset += (widthFactor + marginOffset) * (mRtlLayout ? -1 : 1);
}

if (drawAt + mPageMargin > scrollX) {
//Synthetic comment -- @@ -2270,16 +2372,26 @@
float scrollX = oldScrollX - xOffset;
final int width = getClientWidth();

        float leftBound = width * mLeftEdgeOffset;
        float rightBound = width * mRightEdgeOffset;

final ItemInfo firstItem = mItems.get(0);
final ItemInfo lastItem = mItems.get(mItems.size() - 1);
if (firstItem.position != 0) {
            if(mRtlLayout){
                rightBound = firstItem.offset * width;
            }
            else{
                leftBound = firstItem.offset * width;
            }
}
if (lastItem.position != mAdapter.getCount() - 1) {
            if(mRtlLayout){
                leftBound = lastItem.offset * width;
            }
            else{
                rightBound = lastItem.offset * width;
            }
}

if (scrollX < leftBound) {







