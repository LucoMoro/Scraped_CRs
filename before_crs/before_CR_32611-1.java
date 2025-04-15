/*Call onTimeChanged when AM/PM changed.
Commit for issuehttp://code.google.com/p/android/issues/detail?id=24388&q=type%3DDefect&colspec=ID%20Type%20Status%20Owner%20Summary%20Stars[Problem]
TimePicker doesn't call handler's onTimeChanged when AM/PM changed on
ICS althou it does on Gingerbread.

[Solution]
Call onTimeChanged as like as Hour/Minutes does.

Change-Id:I1223ba6c99f166a9f9e6ea70025c3445ca71a6baSigned-off-by: SeongJae Park <sj38.park@gmail.com>*/
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







