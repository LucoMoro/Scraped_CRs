/*Call onTimeChanged when AM/PM changed.

Commit for issuehttp://code.google.com/p/android/issues/detail?id=24388[Problem]
TimePicker doesn't call handler's onTimeChanged when AM/PM changed on
ICS althou it does on Gingerbread.

[Solution]
Call onTimeChanged as like as Hour/Minutes does.

Change-Id:I9911c351874168001b69c186f012836fc51285f5Signed-off-by: SeongJae Park <sj38.park@gmail.com>*/
//Synthetic comment -- diff --git a/core/java/android/widget/TimePicker.java b/core/java/android/widget/TimePicker.java
//Synthetic comment -- index afca2db..62373fc 100644

//Synthetic comment -- @@ -212,6 +212,7 @@
button.requestFocus();
mIsAm = !mIsAm;
updateAmPmControl();
}
});
} else {
//Synthetic comment -- @@ -226,6 +227,7 @@
picker.requestFocus();
mIsAm = !mIsAm;
updateAmPmControl();
}
});
mAmPmSpinnerInput = (EditText) mAmPmSpinner.findViewById(R.id.numberpicker_input);







