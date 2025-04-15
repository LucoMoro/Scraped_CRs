/*Fix a bug of switching candidate pages

Change-Id:Iaf732f36be693f971b5febed47c751af9e8d3122Signed-off-by: Chih-Wei Huang <cwhuang@linux.org.tw>*/
//Synthetic comment -- diff --git a/src/com/android/inputmethod/pinyin/CandidateView.java b/src/com/android/inputmethod/pinyin/CandidateView.java
//Synthetic comment -- index 8dc1bf1..4157183 100644

//Synthetic comment -- @@ -258,13 +258,12 @@
@Override
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
int mOldWidth = mMeasuredWidth;
        int mOldHeight = mMeasuredHeight;

setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(),
widthMeasureSpec), getDefaultSize(getSuggestedMinimumHeight(),
heightMeasureSpec));

        if (mOldWidth != mMeasuredWidth || mOldHeight != mMeasuredHeight) {
onSizeChanged();
}
}







