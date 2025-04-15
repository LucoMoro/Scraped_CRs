/*Fix number counter will keep going after window losed focus.

Use Time pick in AlarmClock, long press on + or -, make an
incoming call or press power key to suspend the device,
after resumed, the counter will keep going without press.

Change-Id:I5e69d5e17d3be9aa78648e6f8e28665ec305b36f*/




//Synthetic comment -- diff --git a/core/java/android/widget/NumberPickerButton.java b/core/java/android/widget/NumberPickerButton.java
//Synthetic comment -- index 1c8579c..292b668 100644

//Synthetic comment -- @@ -85,4 +85,12 @@
mNumberPicker.cancelDecrement();
}
}

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus) {
            cancelLongpress();
        }
    }

}







