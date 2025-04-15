/*Added Horizontal Scrolling support.*/




//Synthetic comment -- diff --git a/core/java/android/widget/ScrollView.java b/core/java/android/widget/ScrollView.java
//Synthetic comment -- index 23a27ac..f3db6de 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package android.widget;

import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
//Synthetic comment -- @@ -32,8 +34,6 @@

import com.android.internal.R;

/**
* Layout container for a view hierarchy that can be scrolled by the user,
* allowing it to be larger than the physical display.  A ScrollView
//Synthetic comment -- @@ -81,6 +81,7 @@
/**
* Position of the last motion event.
*/
    private float mLastMotionX;
private float mLastMotionY;

/**
//Synthetic comment -- @@ -119,12 +120,18 @@
*/
private boolean mSmoothScrollingEnabled = true;

    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    /**
     * Orientation. Defaults to VERTICAL.
     */
    private int mOrientation = VERTICAL;

public ScrollView(Context context) {
super(context);
initScrollView();

        updateScrollBarsAndFadingEdge();

TypedArray a = context.obtainStyledAttributes(R.styleable.View);

//Synthetic comment -- @@ -142,6 +149,8 @@
super(context, attrs, defStyle);
initScrollView();

        updateScrollBarsAndFadingEdge();

TypedArray a =
context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.ScrollView, defStyle, 0);

//Synthetic comment -- @@ -152,32 +161,78 @@

@Override
protected float getTopFadingEdgeStrength() {
        if (isVerticalOrientation()) {
            if (getChildCount() == 0) {
                return 0.0f;
            }

            final int length = getVerticalFadingEdgeLength();
            if (mScrollY < length) {
                return mScrollY / (float) length;
            }

            return 1.0f;
        } else {
            return super.getTopFadingEdgeStrength();
        }
    }

    @Override
    protected float getLeftFadingEdgeStrength() {
        if (isHorizontalOrientation()) {
            if (getChildCount() == 0) {
                return 0.0f;
            }

            final int length = getHorizontalFadingEdgeLength();
            if (mScrollX < length) {
                return mScrollX / (float) length;
            }

            return 1.0f;
        } else {
            return super.getLeftFadingEdgeStrength();
        }
}

@Override
protected float getBottomFadingEdgeStrength() {
        if (isVerticalOrientation()) {
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
        } else {
            return super.getBottomFadingEdgeStrength();
        }
    }

    @Override
    protected float getRightFadingEdgeStrength() {
        if (isHorizontalOrientation()) {
            if (getChildCount() == 0) {
                return 0.0f;
            }

            final int length = getHorizontalFadingEdgeLength();
            final int right = getChildAt(0).getRight();
            final int span = right - mScrollX - getWidth();
            if (span < length) {
                return span / (float) length;
            }

            return 1.0f;
        } else {
            return super.getRightFadingEdgeStrength();
        }
}

/**
//Synthetic comment -- @@ -185,7 +240,9 @@
*   an arrow event.
*/
public int getMaxScrollAmount() {
        int maxScrollFactor = isVerticalOrientation() ? mBottom - mTop : mRight - mLeft;

        return (int)(MAX_SCROLL_FACTOR * maxScrollFactor);
}


//Synthetic comment -- @@ -238,8 +295,13 @@
private boolean canScroll() {
View child = getChildAt(0);
if (child != null) {
            if (isVerticalOrientation()) {
                int childHeight = child.getHeight();
                return getHeight() < childHeight + mPaddingTop + mPaddingBottom;
            } else {
                int childWidth = child.getWidth();
                return getWidth() < childWidth + mPaddingLeft + mPaddingRight;
            }
}
return false;
}
//Synthetic comment -- @@ -290,23 +352,44 @@
return;
}

        if (isVerticalOrientation()) {
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
        } else {
            final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            if (widthMode == MeasureSpec.UNSPECIFIED) {
                return;
            }

            final View child = getChildAt(0);
            int width = getMeasuredWidth();
            if (child.getMeasuredWidth() < width) {
                final FrameLayout.LayoutParams lp = (LayoutParams) child.getLayoutParams();

                int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, mPaddingTop
                        + mPaddingBottom, lp.height);
                width -= mPaddingLeft;
                width -= mPaddingRight;
                int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);

                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
}
}

//Synthetic comment -- @@ -393,6 +476,7 @@
return false;
}

        final float x = ev.getX();
final float y = ev.getY();

switch (action) {
//Synthetic comment -- @@ -403,17 +487,25 @@
*/

/*
                * Locally do absolute value. mLastMotionX/Y is set to the x/y value
* of the down event.
*/
                if (isVerticalOrientation()) {
                    final int yDiff = (int) Math.abs(y - mLastMotionY);
                    if (yDiff > ViewConfiguration.getTouchSlop()) {
                        mIsBeingDragged = true;
                    }
                } else {
                    final int xDiff = (int) Math.abs(x - mLastMotionX);
                    if (xDiff > ViewConfiguration.getTouchSlop()) {
                        mIsBeingDragged = true;
                    }
}
break;

case MotionEvent.ACTION_DOWN:
/* Remember location of down touch */
                mLastMotionX = x;
mLastMotionY = y;

/*
//Synthetic comment -- @@ -457,6 +549,7 @@
mVelocityTracker.addMovement(ev);

final int action = ev.getAction();
        final float x = ev.getX();
final float y = ev.getY();

switch (action) {
//Synthetic comment -- @@ -470,29 +563,52 @@
}

// Remember where the motion event started
                mLastMotionX = x;
mLastMotionY = y;
break;
case MotionEvent.ACTION_MOVE:
// Scroll to follow the motion event
                if (isVerticalOrientation()) {
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
                } else {
                    final int deltaX = (int) (mLastMotionX - x);
                    mLastMotionX = x;
                    if (deltaX < 0) {
                        if (mScrollX > 0) {
                            scrollBy(deltaX,0);
                        }
                    } else if (deltaX > 0) {
                        final int rightEdge = getWidth() - mPaddingRight;
                        final int availableToScroll = getChildAt(0).getRight() - mScrollX - rightEdge;
                        if (availableToScroll > 0) {
                            scrollBy(Math.min(availableToScroll, deltaX),0);
                        }
}
}

break;
case MotionEvent.ACTION_UP:
final VelocityTracker velocityTracker = mVelocityTracker;
velocityTracker.computeCurrentVelocity(1000);

                int initialVelocity = 0;
                if (isVerticalOrientation()) {
                    initialVelocity = (int) velocityTracker.getYVelocity();
                } else {
                    initialVelocity = (int) velocityTracker.getXVelocity();
                }

if ((Math.abs(initialVelocity) > ViewConfiguration.getMinimumFlingVelocity()) &&
(getChildCount() > 0)) {
//Synthetic comment -- @@ -524,30 +640,55 @@
* @return the next focusable component in the bounds or null if none can be
*         found
*/
    private View findFocusableViewInMyBounds(final boolean topOrLeftFocus,
            final int topOrLeft, View preferredFocusable) {
/*
* The fading edge's transparent side should be considered for focus
* since it's mostly visible, so we divide the actual fading edge length
* by 2.
*/
        if (isVerticalOrientation()) {
            final int fadingEdgeLength = getVerticalFadingEdgeLength() / 2;
            final int topWithoutFadingEdge = topOrLeft + fadingEdgeLength;
            final int bottomWithoutFadingEdge = topOrLeft + getHeight() - fadingEdgeLength;

            if ((preferredFocusable != null)
                    && (preferredFocusable.getTop() < bottomWithoutFadingEdge)
                    && (preferredFocusable.getBottom() > topWithoutFadingEdge)) {
                return preferredFocusable;
            }

            return findFocusableViewInBounds(topOrLeftFocus, topWithoutFadingEdge,
                    bottomWithoutFadingEdge);
        } else {
            final int fadingEdgeLength = getHorizontalFadingEdgeLength() / 2;
            final int topWithoutFadingEdge = topOrLeft + fadingEdgeLength;
            final int rightWithoutFadingEdge = topOrLeft + getWidth() - fadingEdgeLength;

            if ((preferredFocusable != null)
                    && (preferredFocusable.getLeft() < rightWithoutFadingEdge)
                    && (preferredFocusable.getRight() > topWithoutFadingEdge)) {
                return preferredFocusable;
            }

            return findFocusableViewInBounds(topOrLeftFocus, topWithoutFadingEdge,
                    rightWithoutFadingEdge);
}
    }


    private View findFocusableViewInBounds(boolean topOrLeftFocus, int topOrLeft, int bottomOrRight) {
        if(isVerticalOrientation()) {
            return findFocusableViewInBoundsForVertical(topOrLeftFocus,topOrLeft,bottomOrRight);
        } else {
            return findFocusableViewInBoundsForHorizontal(topOrLeftFocus, topOrLeft, bottomOrRight);
        }
}

/**
* <p>
     * Finds the next focusable component that fits in the specified bounds when the orientation
     * is set to VERTICAL.
* </p>
*
* @param topFocus look for a candidate is the one at the top of the bounds
//Synthetic comment -- @@ -560,7 +701,7 @@
* @return the next focusable component in the bounds or null if none can
*         be found
*/
    private View findFocusableViewInBoundsForVertical(boolean topFocus, int top, int bottom) {

List<View> focusables = getFocusables(View.FOCUS_FORWARD);
View focusCandidate = null;
//Synthetic comment -- @@ -629,6 +770,91 @@
}

/**
     * <p>
     * Finds the next focusable component that fits in the specified bounds when the orientation
     * is set to HORIZONTAL.
     * </p>
     *
     * @param leftFocus look for a candidate is the one at the left of the bounds
     *                 if leftFocus is true, or at the right of the bounds if leftFocus is
     *                 false
     * @param left      the left offset of the bounds in which a focusable must be
     *                 found
     * @param right   the right offset of the bounds in which a focusable must
     *                 be found
     * @return the next focusable component in the bounds or null if none can
     *         be found
     */
    private View findFocusableViewInBoundsForHorizontal(boolean leftFocus, int left, int right) {

        List<View> focusables = getFocusables(View.FOCUS_FORWARD);
        View focusCandidate = null;

        /*
         * A fully contained focusable is one where its top is below the bound's
         * top, and its bottom is above the bound's bottom. A partially
         * contained focusable is one where some part of it is within the
         * bounds, but it also has some part that is not within bounds.  A fully contained
         * focusable is preferred to a partially contained focusable.
         */
        boolean foundFullyContainedFocusable = false;

        int count = focusables.size();
        for (int i = 0; i < count; i++) {
            View view = focusables.get(i);
            int viewLeft = view.getLeft();
            int viewRight = view.getRight();

            if (left < viewRight && viewLeft < right) {
                /*
                 * the focusable is in the target area, it is a candidate for
                 * focusing
                 */

                final boolean viewIsFullyContained = (left < viewLeft) &&
                        (viewRight < right);

                if (focusCandidate == null) {
                    /* No candidate, take this one */
                    focusCandidate = view;
                    foundFullyContainedFocusable = viewIsFullyContained;
                } else {
                    final boolean viewIsCloserToBoundary =
                            (leftFocus && viewLeft < focusCandidate.getLeft()) ||
                                    (!leftFocus && viewRight > focusCandidate
                                            .getRight());

                    if (foundFullyContainedFocusable) {
                        if (viewIsFullyContained && viewIsCloserToBoundary) {
                            /*
                             * We're dealing with only fully contained views, so
                             * it has to be closer to the boundary to beat our
                             * candidate
                             */
                            focusCandidate = view;
                        }
                    } else {
                        if (viewIsFullyContained) {
                            /* Any fully contained view beats a partially contained view */
                            focusCandidate = view;
                            foundFullyContainedFocusable = true;
                        } else if (viewIsCloserToBoundary) {
                            /*
                             * Partially contained view beats another partially
                             * contained view if it's closer
                             */
                            focusCandidate = view;
                        }
                    }
                }
            }
        }

        return focusCandidate;
    }


    /**
* <p>Handles scrolling in response to a "page up/down" shortcut press. This
* method will scroll the view by one page up or down and give the focus
* to the topmost/bottommost component in the new visible area. If no
//Synthetic comment -- @@ -642,26 +868,49 @@
*/
public boolean pageScroll(int direction) {
boolean down = direction == View.FOCUS_DOWN;
        if(isVerticalOrientation()) {
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
        } else {
            int width = getWidth();

            if (down) {
                mTempRect.left = getScrollX() + width;
                int count = getChildCount();
                if (count > 0) {
                    View view = getChildAt(count - 1);
                    if (mTempRect.left + width > view.getRight()) {
                        mTempRect.left = view.getRight() - width;
                    }
                }
            } else {
                mTempRect.left = getScrollX() - width;
                if (mTempRect.left < 0) {
                    mTempRect.left = 0;
                }
            }
            mTempRect.right = mTempRect.left + width;

            return scrollAndFocus(direction, mTempRect.left, mTempRect.right);
        }
}

/**
//Synthetic comment -- @@ -678,21 +927,39 @@
*/
public boolean fullScroll(int direction) {
boolean down = direction == View.FOCUS_DOWN;
        if (isVerticalOrientation()) {
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
        } else {
            int width = getWidth();

            mTempRect.left = 0;
            mTempRect.right = width;

            if (down) {
                int count = getChildCount();
                if (count > 0) {
                    View view = getChildAt(count - 1);
                    mTempRect.right = view.getRight();
                    mTempRect.left = mTempRect.right - width;
                }
            }

            return scrollAndFocus(direction, mTempRect.left, mTempRect.right);
        }
}

/**
//Synthetic comment -- @@ -708,29 +975,53 @@
* @param bottom    the bottom offset of the new area to be made visible
* @return true if the key event is consumed by this method, false otherwise
*/
    private boolean scrollAndFocus(int direction, int topOrLeft, int bottomOrRight) {
boolean handled = true;

        if (isVerticalOrientation()) {
            int height = getHeight();
            int containerTop = getScrollY();
            int containerBottom = containerTop + height;
            boolean up = direction == View.FOCUS_UP;

            View newFocused = findFocusableViewInBounds(up, topOrLeft, bottomOrRight);
            if (newFocused == null) {
                newFocused = this;
            }

            if (topOrLeft >= containerTop && bottomOrRight <= containerBottom) {
                handled = false;
            } else {
                int delta = up ? (topOrLeft - containerTop) : (bottomOrRight - containerBottom);
                doScrollY(delta);
            }

            if (newFocused != findFocus() && newFocused.requestFocus(direction)) {
                mScrollViewMovedFocus = true;
                mScrollViewMovedFocus = false;
            }
} else {
            int width = getWidth();
            int containerLeft = getScrollX();
            int containerRight = containerLeft + width;
            boolean up = direction == View.FOCUS_UP;

            View newFocused = findFocusableViewInBounds(up, topOrLeft, bottomOrRight);
            if (newFocused == null) {
                newFocused = this;
            }

            if (topOrLeft >= containerLeft && bottomOrRight <= containerRight) {
                handled = false;
            } else {
                int delta = up ? (topOrLeft - containerLeft) : (bottomOrRight - containerRight);
                doScrollX(delta);
            }

            if (newFocused != findFocus() && newFocused.requestFocus(direction)) {
                mScrollViewMovedFocus = true;
                mScrollViewMovedFocus = false;
            }
}

return handled;
//Synthetic comment -- @@ -756,28 +1047,52 @@
nextFocused.getDrawingRect(mTempRect);
offsetDescendantRectToMyCoords(nextFocused, mTempRect);
int scrollDelta = computeScrollDeltaToGetChildRectOnScreen(mTempRect);

            if(isVerticalOrientation()) {
                doScrollY(scrollDelta);
            } else {
                doScrollX(scrollDelta);
            }

nextFocused.requestFocus(direction);
} else {
// no new focus
int scrollDelta = maxJump;

            int scrollXY = getScrollY();
            if(isHorizontalOrientation()) {
                scrollXY = getScrollX();
            }
            if (direction == View.FOCUS_UP && scrollXY < scrollDelta) {
                scrollDelta = scrollXY;
} else if (direction == View.FOCUS_DOWN) {

              if(isVerticalOrientation()) {
                  int daBottom = getChildAt(getChildCount() - 1).getBottom();

                  int screenBottom = getScrollY() + getHeight();

                  if (daBottom - screenBottom < maxJump) {
                      scrollDelta = daBottom - screenBottom;
                  }
              } else {
                  int daRight = getChildAt(getChildCount() - 1).getRight();

                  int screenRight = getScrollX() + getWidth();

                  if (daRight - screenRight < maxJump) {
                      scrollDelta = daRight - screenRight;
                  }
              }
}
if (scrollDelta == 0) {
return false;
}
            if(isVerticalOrientation()) {
                doScrollY(direction == View.FOCUS_DOWN ? scrollDelta : -scrollDelta);
            } else {
                doScrollX(direction == View.FOCUS_DOWN ? scrollDelta : -scrollDelta);
            }
}

if (currentFocused != null && currentFocused.isFocused()
//Synthetic comment -- @@ -811,8 +1126,28 @@
descendant.getDrawingRect(mTempRect);
offsetDescendantRectToMyCoords(descendant, mTempRect);

        if (isVerticalOrientation()) {
            return (mTempRect.bottom + delta) >= getScrollY()
            && (mTempRect.top - delta) <= (getScrollY() + getHeight());
        } else {
            return (mTempRect.right + delta) >= getScrollX()
            && (mTempRect.left - delta) <= (getScrollX() + getWidth());
        }
    }

    /**
     * Smooth scroll by a X delta
     *
     * @param delta the number of pixels to scroll by on the Y axis
     */
    private void doScrollX(int delta) {
        if (delta != 0) {
            if (mSmoothScrollingEnabled) {
                smoothScrollBy(delta,0);
            } else {
                scrollBy(delta,0);
            }
        }
}

/**
//Synthetic comment -- @@ -867,7 +1202,21 @@
@Override
protected int computeVerticalScrollRange() {
int count = getChildCount();
        if (isVerticalOrientation()) {
            return count == 0 ? getHeight() : (getChildAt(0)).getBottom();
        } else {
            return super.computeVerticalScrollOffset();
        }
    }

    @Override
    protected int computeHorizontalScrollRange() {
        int count = getChildCount();
        if (isHorizontalOrientation()) {
            return count == 0 ? getWidth() : (getChildAt(0)).getRight();
        } else {
            return super.computeHorizontalScrollRange();
        }
}


//Synthetic comment -- @@ -878,10 +1227,17 @@
int childWidthMeasureSpec;
int childHeightMeasureSpec;

        if (isVerticalOrientation()) {
            childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, mPaddingLeft
                    + mPaddingRight, lp.width);
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        } else {
            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec, mPaddingTop
                    + mPaddingBottom, lp.height);

        }

child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
}
//Synthetic comment -- @@ -891,12 +1247,22 @@
int parentHeightMeasureSpec, int heightUsed) {
final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

        int childWidthMeasureSpec = 0;
        int childHeightMeasureSpec = 0;

        if(isVerticalOrientation()) {
            childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                    mPaddingLeft + mPaddingRight + lp.leftMargin + lp.rightMargin
                            + widthUsed, lp.width);
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    lp.topMargin + lp.bottomMargin, MeasureSpec.UNSPECIFIED);
        } else {
            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    lp.leftMargin + lp.rightMargin, MeasureSpec.UNSPECIFIED);
            childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                    mPaddingLeft + mPaddingRight + lp.leftMargin + lp.rightMargin
                    + heightUsed, lp.height);
        }
child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
}

//Synthetic comment -- @@ -951,8 +1317,14 @@

int scrollDelta = computeScrollDeltaToGetChildRectOnScreen(mTempRect);

        if (isVerticalOrientation()) {
            if (scrollDelta != 0) {
                scrollBy(0, scrollDelta);
            }
        } else {
            if (scrollDelta != 0) {
                scrollBy(scrollDelta,0);
            }
}
}

//Synthetic comment -- @@ -967,11 +1339,21 @@
private boolean scrollToChildRect(Rect rect, boolean immediate) {
final int delta = computeScrollDeltaToGetChildRectOnScreen(rect);
final boolean scroll = delta != 0;
        if (isVerticalOrientation()) {
            if (scroll) {
                if (immediate) {
                    scrollBy(0, delta);
                } else {
                    smoothScrollBy(0, delta);
                }
            }
        } else {
            if (scroll) {
                if (immediate) {
                    scrollBy(delta,0);
                } else {
                    smoothScrollBy(delta,0);
                }
}
}
return scroll;
//Synthetic comment -- @@ -986,60 +1368,115 @@
* @return The scroll delta.
*/
protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        if (isVerticalOrientation()) {
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
        } else {
            int width = getWidth();
            int screenLeft = getScrollX();
            int screenRight = screenLeft + width;

            int fadingEdge = getHorizontalFadingEdgeLength();

            // leave room for top fading edge as long as rect isn't at very top
            if (rect.left > 0) {
                screenLeft += fadingEdge;
            }

            // leave room for bottom fading edge as long as rect isn't at very bottom
            if (rect.right < getChildAt(0).getWidth()) {
                screenRight -= fadingEdge;
            }

            int scrollXDelta = 0;

            if (rect.right > screenRight && rect.left > screenLeft) {
                // need to move down to get it in view: move down just enough so
                // that the entire rectangle is in view (or at least the first
                // screen size chunk).

                if (rect.width() > width) {
                    // just enough to get screen size chunk on
                    scrollXDelta += (rect.left - screenLeft);
                } else {
                    // get entire rect at bottom of screen
                    scrollXDelta += (rect.right - screenRight);
                }

                // make sure we aren't scrolling beyond the end of our content
                int right = getChildAt(getChildCount() - 1).getRight();
                int distanceToRight = right - screenRight;
                scrollXDelta = Math.min(scrollXDelta, distanceToRight);

            } else if (rect.left < screenRight && rect.right < screenRight) {
                // need to move up to get it in view: move up just enough so that
                // entire rectangle is in view (or at least the first screen
                // size chunk of it).

                if (rect.width() > width) {
                    // screen size chunk
                    scrollXDelta -= (screenRight - rect.right);
                } else {
                    // entire rect at top
                    scrollXDelta -= (screenLeft - rect.left);
                }

                // make sure we aren't scrolling any further than the top our content
                scrollXDelta = Math.max(scrollXDelta, -getScrollX());
            }
            return scrollXDelta;
}
}

@Override
//Synthetic comment -- @@ -1141,24 +1578,45 @@
*                  numbers mean that the finger/curor is moving down the screen,
*                  which means we want to scroll towards the top.
*/
    public void fling(int velocity) {
        if(isVerticalOrientation()) {
            int height = getHeight();
            int bottom = getChildAt(getChildCount() - 1).getBottom();

            mScroller.fling(mScrollX, mScrollY, 0, velocity, 0, 0, 0, bottom - height);

            final boolean movingDown = velocity > 0;

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
        } else {
            int width = getWidth();
            int right = getChildAt(getChildCount() - 1).getRight();

            mScroller.fling(mScrollX, mScrollY, velocity, 0, 0, right - width, 0, 0);

            final boolean movingRight = velocity > 0;

            View newFocused =
                    findFocusableViewInMyBounds(movingRight, mScroller.getFinalX(), findFocus());
            if (newFocused == null) {
                newFocused = this;
            }

            if (newFocused != findFocus()
                    && newFocused.requestFocus(movingRight ? View.FOCUS_DOWN : View.FOCUS_UP)) {
                mScrollViewMovedFocus = true;
                mScrollViewMovedFocus = false;
            }
}

invalidate();
//Synthetic comment -- @@ -1210,4 +1668,40 @@
}
return n;
}

    public boolean isVerticalOrientation() {
        return mOrientation == VERTICAL;
    }

    public boolean isHorizontalOrientation() {
        return mOrientation == HORIZONTAL;
    }

    /**
     * Set the orientation of the Scroll View.
     * Possible orientations are VERTICAL or HORIZONTAL
     * @param orientation
     */
    public void setOrientation(int orientation) {
        if (orientation != VERTICAL && orientation != HORIZONTAL) {
            throw new IllegalArgumentException("Uknown orientation " + orientation + 
                    ". Supports VERTICAL or HORIZONTAL.");
        }
        if(mOrientation != orientation) {
            mOrientation = orientation;
            updateScrollBarsAndFadingEdge();
        }
    }

    /**
     * Enables/Disables the Scroll Bars and Fading Edges depending
     * on the currently set orientation.
     */
    private void updateScrollBarsAndFadingEdge() {
        boolean vertical = isVerticalOrientation();
        setVerticalScrollBarEnabled(vertical);
        setVerticalFadingEdgeEnabled(vertical);
        setHorizontalScrollBarEnabled(!vertical);
        setHorizontalFadingEdgeEnabled(!vertical);
    }
}







