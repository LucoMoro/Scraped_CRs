/*Fix for Android issue 1802: DatePickerDialog.updateDate() does not update the title of the dialog.

In response tohttps://review.source.android.com/Gerrit#change,10273, modified NumberPicker.setCurrent(). Previously, NumberPicker had both public setCurrent and private changeCurrent, the latter doing it the right way. Now it only has setCurrent, which does what changeCurrent did.*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/widget/NumberPicker.java b/core/java/com/android/internal/widget/NumberPicker.java
//Synthetic comment -- index 2f08c8d..3d02dc3 100644

//Synthetic comment -- @@ -68,10 +68,10 @@
private final Runnable mRunnable = new Runnable() {
public void run() {
if (mIncrement) {
                changeCurrent(mCurrent + 1);
mHandler.postDelayed(this, mSpeed);
} else if (mDecrement) {
                changeCurrent(mCurrent - 1);
mHandler.postDelayed(this, mSpeed);
}
}
//Synthetic comment -- @@ -175,11 +175,6 @@
updateView();
}

    public void setCurrent(int current) {
        mCurrent = current;
        updateView();
    }

/**
* The speed (in milliseconds) at which the numbers will scroll
* when the the +/- buttons are longpressed. Default is 300ms.
//Synthetic comment -- @@ -194,9 +189,9 @@

// now perform the increment/decrement
if (R.id.increment == v.getId()) {
            changeCurrent(mCurrent + 1);
} else if (R.id.decrement == v.getId()) {
            changeCurrent(mCurrent - 1);
}
}

//Synthetic comment -- @@ -206,7 +201,7 @@
: String.valueOf(value);
}

    private void changeCurrent(int current) {

// Wrap around the values if we go past the start or end
if (current > mEnd) {







