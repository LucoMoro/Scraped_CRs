/*Added Horizontal Scrolling support.*/
//Synthetic comment -- diff --git a/core/java/android/widget/ScrollView.java b/core/java/android/widget/ScrollView.java
//Synthetic comment -- index 23a27ac..f3db6de 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
//Synthetic comment -- @@ -32,8 +34,6 @@

import com.android.internal.R;

import java.util.List;

/**
* Layout container for a view hierarchy that can be scrolled by the user,
* allowing it to be larger than the physical display.  A ScrollView
//Synthetic comment -- @@ -81,6 +81,7 @@
/**
* Position of the last motion event.
*/
private float mLastMotionY;

/**
//Synthetic comment -- @@ -119,12 +120,18 @@
*/
private boolean mSmoothScrollingEnabled = true;

public ScrollView(Context context) {
super(context);
initScrollView();

        setVerticalScrollBarEnabled(true);
        setVerticalFadingEdgeEnabled(true);

TypedArray a = context.obtainStyledAttributes(R.styleable.View);

//Synthetic comment -- @@ -142,6 +149,8 @@
super(context, attrs, defStyle);
initScrollView();

TypedArray a =
context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.ScrollView, defStyle, 0);

//Synthetic comment -- @@ -152,32 +161,78 @@

@Override
protected float getTopFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }

        final int length = getVerticalFadingEdgeLength();
        if (mScrollY < length) {
            return mScrollY / (float) length;
        }

        return 1.0f;
}

@Override
protected float getBottomFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }

        final int length = getVerticalFadingEdgeLength();
        final int bottom = getChildAt(0).getBottom();
        final int span = bottom - mScrollY - getHeight();
        if (span < length) {
            return span / (float) length;
        }

        return 1.0f;
}

/**
//Synthetic comment -- @@ -185,7 +240,9 @@
*   an arrow event.
*/
public int getMaxScrollAmount() {
        return (int) (MAX_SCROLL_FACTOR * (mBottom - mTop));
}


//Synthetic comment -- @@ -238,8 +295,13 @@
private boolean canScroll() {
View child = getChildAt(0);
if (child != null) {
            int childHeight = child.getHeight();
            return getHeight() < childHeight + mPaddingTop + mPaddingBottom;
}
return false;
}
//Synthetic comment -- @@ -290,23 +352,44 @@
return;
}

        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.UNSPECIFIED) {
            return;
        }

        final View child = getChildAt(0);
        int height = getMeasuredHeight();
        if (child.getMeasuredHeight() < height) {
            final FrameLayout.LayoutParams lp = (LayoutParams) child.getLayoutParams();

            int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, mPaddingLeft
                    + mPaddingRight, lp.width);
            height -= mPaddingTop;
            height -= mPaddingBottom;
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
}
}

//Synthetic comment -- @@ -393,6 +476,7 @@
return false;
}

final float y = ev.getY();

switch (action) {
//Synthetic comment -- @@ -403,17 +487,25 @@
*/

/*
                * Locally do absolute value. mLastMotionY is set to the y value
* of the down event.
*/
                final int yDiff = (int) Math.abs(y - mLastMotionY);
                if (yDiff > ViewConfiguration.getTouchSlop()) {
                    mIsBeingDragged = true;
}
break;

case MotionEvent.ACTION_DOWN:
/* Remember location of down touch */
mLastMotionY = y;

/*
//Synthetic comment -- @@ -457,6 +549,7 @@
mVelocityTracker.addMovement(ev);

final int action = ev.getAction();
final float y = ev.getY();

switch (action) {
//Synthetic comment -- @@ -470,29 +563,52 @@
}

// Remember where the motion event started
mLastMotionY = y;
break;
case MotionEvent.ACTION_MOVE:
// Scroll to follow the motion event
                final int deltaY = (int) (mLastMotionY - y);
                mLastMotionY = y;

                if (deltaY < 0) {
                    if (mScrollY > 0) {
                        scrollBy(0, deltaY);
}
                } else if (deltaY > 0) {
                    final int bottomEdge = getHeight() - mPaddingBottom;
                    final int availableToScroll = getChildAt(0).getBottom() - mScrollY - bottomEdge;
                    if (availableToScroll > 0) {
                        scrollBy(0, Math.min(availableToScroll, deltaY));
}
}
break;
case MotionEvent.ACTION_UP:
final VelocityTracker velocityTracker = mVelocityTracker;
velocityTracker.computeCurrentVelocity(1000);
                int initialVelocity = (int) velocityTracker.getYVelocity();

if ((Math.abs(initialVelocity) > ViewConfiguration.getMinimumFlingVelocity()) &&
(getChildCount() > 0)) {
//Synthetic comment -- @@ -524,30 +640,55 @@
* @return the next focusable component in the bounds or null if none can be
*         found
*/
    private View findFocusableViewInMyBounds(final boolean topFocus,
            final int top, View preferredFocusable) {
/*
* The fading edge's transparent side should be considered for focus
* since it's mostly visible, so we divide the actual fading edge length
* by 2.
*/
        final int fadingEdgeLength = getVerticalFadingEdgeLength() / 2;
        final int topWithoutFadingEdge = top + fadingEdgeLength;
        final int bottomWithoutFadingEdge = top + getHeight() - fadingEdgeLength;

        if ((preferredFocusable != null)
                && (preferredFocusable.getTop() < bottomWithoutFadingEdge)
                && (preferredFocusable.getBottom() > topWithoutFadingEdge)) {
            return preferredFocusable;
}

        return findFocusableViewInBounds(topFocus, topWithoutFadingEdge,
                bottomWithoutFadingEdge);
}

/**
* <p>
     * Finds the next focusable component that fits in the specified bounds.
* </p>
*
* @param topFocus look for a candidate is the one at the top of the bounds
//Synthetic comment -- @@ -560,7 +701,7 @@
* @return the next focusable component in the bounds or null if none can
*         be found
*/
    private View findFocusableViewInBounds(boolean topFocus, int top, int bottom) {

List<View> focusables = getFocusables(View.FOCUS_FORWARD);
View focusCandidate = null;
//Synthetic comment -- @@ -629,6 +770,91 @@
}

/**
* <p>Handles scrolling in response to a "page up/down" shortcut press. This
* method will scroll the view by one page up or down and give the focus
* to the topmost/bottommost component in the new visible area. If no
//Synthetic comment -- @@ -642,26 +868,49 @@
*/
public boolean pageScroll(int direction) {
boolean down = direction == View.FOCUS_DOWN;
        int height = getHeight();

        if (down) {
            mTempRect.top = getScrollY() + height;
            int count = getChildCount();
            if (count > 0) {
                View view = getChildAt(count - 1);
                if (mTempRect.top + height > view.getBottom()) {
                    mTempRect.top = view.getBottom() - height;
}
}
        } else {
            mTempRect.top = getScrollY() - height;
            if (mTempRect.top < 0) {
                mTempRect.top = 0;
            }
        }
        mTempRect.bottom = mTempRect.top + height;

        return scrollAndFocus(direction, mTempRect.top, mTempRect.bottom);
}

/**
//Synthetic comment -- @@ -678,21 +927,39 @@
*/
public boolean fullScroll(int direction) {
boolean down = direction == View.FOCUS_DOWN;
        int height = getHeight();

        mTempRect.top = 0;
        mTempRect.bottom = height;

        if (down) {
            int count = getChildCount();
            if (count > 0) {
                View view = getChildAt(count - 1);
                mTempRect.bottom = view.getBottom();
                mTempRect.top = mTempRect.bottom - height;
}
        }

        return scrollAndFocus(direction, mTempRect.top, mTempRect.bottom);
}

/**
//Synthetic comment -- @@ -708,29 +975,53 @@
* @param bottom    the bottom offset of the new area to be made visible
* @return true if the key event is consumed by this method, false otherwise
*/
    private boolean scrollAndFocus(int direction, int top, int bottom) {
boolean handled = true;

        int height = getHeight();
        int containerTop = getScrollY();
        int containerBottom = containerTop + height;
        boolean up = direction == View.FOCUS_UP;

        View newFocused = findFocusableViewInBounds(up, top, bottom);
        if (newFocused == null) {
            newFocused = this;
        }

        if (top >= containerTop && bottom <= containerBottom) {
            handled = false;
} else {
            int delta = up ? (top - containerTop) : (bottom - containerBottom);
            doScrollY(delta);
        }

        if (newFocused != findFocus() && newFocused.requestFocus(direction)) {
            mScrollViewMovedFocus = true;
            mScrollViewMovedFocus = false;
}

return handled;
//Synthetic comment -- @@ -756,28 +1047,52 @@
nextFocused.getDrawingRect(mTempRect);
offsetDescendantRectToMyCoords(nextFocused, mTempRect);
int scrollDelta = computeScrollDeltaToGetChildRectOnScreen(mTempRect);
            doScrollY(scrollDelta);
nextFocused.requestFocus(direction);
} else {
// no new focus
int scrollDelta = maxJump;

            if (direction == View.FOCUS_UP && getScrollY() < scrollDelta) {
                scrollDelta = getScrollY();
} else if (direction == View.FOCUS_DOWN) {

                int daBottom = getChildAt(getChildCount() - 1).getBottom();

                int screenBottom = getScrollY() + getHeight();

                if (daBottom - screenBottom < maxJump) {
                    scrollDelta = daBottom - screenBottom;
                }
}
if (scrollDelta == 0) {
return false;
}
            doScrollY(direction == View.FOCUS_DOWN ? scrollDelta : -scrollDelta);
}

if (currentFocused != null && currentFocused.isFocused()
//Synthetic comment -- @@ -811,8 +1126,28 @@
descendant.getDrawingRect(mTempRect);
offsetDescendantRectToMyCoords(descendant, mTempRect);

        return (mTempRect.bottom + delta) >= getScrollY()
                && (mTempRect.top - delta) <= (getScrollY() + getHeight());
}

/**
//Synthetic comment -- @@ -867,7 +1202,21 @@
@Override
protected int computeVerticalScrollRange() {
int count = getChildCount();
        return count == 0 ? getHeight() : (getChildAt(0)).getBottom();
}


//Synthetic comment -- @@ -878,10 +1227,17 @@
int childWidthMeasureSpec;
int childHeightMeasureSpec;

        childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, mPaddingLeft
                + mPaddingRight, lp.width);

        childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
}
//Synthetic comment -- @@ -891,12 +1247,22 @@
int parentHeightMeasureSpec, int heightUsed) {
final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                mPaddingLeft + mPaddingRight + lp.leftMargin + lp.rightMargin
                        + widthUsed, lp.width);
        final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                lp.topMargin + lp.bottomMargin, MeasureSpec.UNSPECIFIED);

child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
}

//Synthetic comment -- @@ -951,8 +1317,14 @@

int scrollDelta = computeScrollDeltaToGetChildRectOnScreen(mTempRect);

        if (scrollDelta != 0) {
            scrollBy(0, scrollDelta);
}
}

//Synthetic comment -- @@ -967,11 +1339,21 @@
private boolean scrollToChildRect(Rect rect, boolean immediate) {
final int delta = computeScrollDeltaToGetChildRectOnScreen(rect);
final boolean scroll = delta != 0;
        if (scroll) {
            if (immediate) {
                scrollBy(0, delta);
            } else {
                smoothScrollBy(0, delta);
}
}
return scroll;
//Synthetic comment -- @@ -986,60 +1368,115 @@
* @return The scroll delta.
*/
protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {

        int height = getHeight();
        int screenTop = getScrollY();
        int screenBottom = screenTop + height;

        int fadingEdge = getVerticalFadingEdgeLength();

        // leave room for top fading edge as long as rect isn't at very top
        if (rect.top > 0) {
            screenTop += fadingEdge;
        }

        // leave room for bottom fading edge as long as rect isn't at very bottom
        if (rect.bottom < getChildAt(0).getHeight()) {
            screenBottom -= fadingEdge;
        }

        int scrollYDelta = 0;

        if (rect.bottom > screenBottom && rect.top > screenTop) {
            // need to move down to get it in view: move down just enough so
            // that the entire rectangle is in view (or at least the first
            // screen size chunk).

            if (rect.height() > height) {
                // just enough to get screen size chunk on
                scrollYDelta += (rect.top - screenTop);
            } else {
                // get entire rect at bottom of screen
                scrollYDelta += (rect.bottom - screenBottom);
}

            // make sure we aren't scrolling beyond the end of our content
            int bottom = getChildAt(getChildCount() - 1).getBottom();
            int distanceToBottom = bottom - screenBottom;
            scrollYDelta = Math.min(scrollYDelta, distanceToBottom);

        } else if (rect.top < screenTop && rect.bottom < screenBottom) {
            // need to move up to get it in view: move up just enough so that
            // entire rectangle is in view (or at least the first screen
            // size chunk of it).

            if (rect.height() > height) {
                // screen size chunk
                scrollYDelta -= (screenBottom - rect.bottom);
            } else {
                // entire rect at top
                scrollYDelta -= (screenTop - rect.top);
}

            // make sure we aren't scrolling any further than the top our content
            scrollYDelta = Math.max(scrollYDelta, -getScrollY());
}
        return scrollYDelta;
}

@Override
//Synthetic comment -- @@ -1141,24 +1578,45 @@
*                  numbers mean that the finger/curor is moving down the screen,
*                  which means we want to scroll towards the top.
*/
    public void fling(int velocityY) {
        int height = getHeight();
        int bottom = getChildAt(getChildCount() - 1).getBottom();

        mScroller.fling(mScrollX, mScrollY, 0, velocityY, 0, 0, 0, bottom - height);

        final boolean movingDown = velocityY > 0;

        View newFocused =
                findFocusableViewInMyBounds(movingDown, mScroller.getFinalY(), findFocus());
        if (newFocused == null) {
            newFocused = this;
        }

        if (newFocused != findFocus()
                && newFocused.requestFocus(movingDown ? View.FOCUS_DOWN : View.FOCUS_UP)) {
            mScrollViewMovedFocus = true;
            mScrollViewMovedFocus = false;
}

invalidate();
//Synthetic comment -- @@ -1210,4 +1668,40 @@
}
return n;
}
}







