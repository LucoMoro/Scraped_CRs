/*In CalendarView, y-position of date number is not correct at first run.

y-position of the date number is lowered after scroll.
The problem was that
mDrawPaint.getTextSize() is called before mDrawPaint.setTextSize() in drawWeekNumbersAndDates() method so position was not set correctly at first run.

Change-Id:I05772a5cf8fa466817b9cf314df6b7441a2d6681Signed-off-by: NoraBora <noranbora@gmail.com>*/




//Synthetic comment -- diff --git a/core/java/android/widget/CalendarView.java b/core/java/android/widget/CalendarView.java
//Synthetic comment -- index b06da06..fce860a 100644

//Synthetic comment -- @@ -1706,12 +1706,13 @@
* @param canvas The canvas to draw on
*/
private void drawWeekNumbersAndDates(Canvas canvas) {
            mDrawPaint.setTextAlign(Align.CENTER);
            mDrawPaint.setTextSize(mDateTextSize);

float textHeight = mDrawPaint.getTextSize();
int y = (int) ((mHeight + textHeight) / 2) - mWeekSeperatorLineWidth;
int nDays = mNumCells;

int i = 0;
int divisor = 2 * nDays;
if (mShowWeekNumber) {







