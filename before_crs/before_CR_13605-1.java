/*In response to Code Review:

core/java/android/widget/DigitalClock.java:  removed the previous change; the getCurrentTime() is unnecessary
core/java/android/widget/AnalogClock.java:  removed the previous change; the getCurrentTime() is unnecessary
core/java/android/widget/CheckedTextView.java:   fixed indention
core/java/android/widget/CompoundButton.java:   fixed indention

Regarding the other CodeRevew suggestions, we would like to clarify our needs.  We use ViewDebug's normal
commands, LIST and DUMP in particular.  We recognize that its properties may change from release to release.
We accept that the ViewDebug interface was not designed to support automated testing; nevertheless, ViewDebug
has let us provide useful testing for many different apps on several different platforms.  Thusfar, changes in
ViewDebug have been much less effort for us than changes have app changes such as the extensive changes to the
Contacts app.  ViewDebug lets us do stand-aside system testing that has already found important integration-time
bugs for our partners.

Thank you for your consideration.*/
//Synthetic comment -- diff --git a/core/java/android/widget/AnalogClock.java b/core/java/android/widget/AnalogClock.java
//Synthetic comment -- index ac096e0..027f91d 100644

//Synthetic comment -- @@ -245,12 +245,4 @@
invalidate();
}
};

    /**
     * {@hide}
     */
    @ViewDebug.ExportedProperty
    public String getCurrentTime() {
        return DateFormat.format("h:mm:ss aa", mCalendar.toMillis(false)).toString();
    }
}








//Synthetic comment -- diff --git a/core/java/android/widget/CheckedTextView.java b/core/java/android/widget/CheckedTextView.java
//Synthetic comment -- index f13d438..f956df6 100644

//Synthetic comment -- @@ -75,7 +75,7 @@
setChecked(!mChecked);
}

@ViewDebug.ExportedProperty
public boolean isChecked() {
return mChecked;
}








//Synthetic comment -- diff --git a/core/java/android/widget/CompoundButton.java b/core/java/android/widget/CompoundButton.java
//Synthetic comment -- index 0d84dd0..bf02ad3 100644

//Synthetic comment -- @@ -99,7 +99,7 @@
return super.performClick();
}

@ViewDebug.ExportedProperty
public boolean isChecked() {
return mChecked;
}








//Synthetic comment -- diff --git a/core/java/android/widget/DigitalClock.java b/core/java/android/widget/DigitalClock.java
//Synthetic comment -- index 5ad6425..52776e1 100644

//Synthetic comment -- @@ -127,12 +127,4 @@
setFormat();
}
}

    /**
     * {@hide}
     */
    @ViewDebug.ExportedProperty
    public String getCurrentTime() {
        return DateFormat.format(m12, mCalendar).toString();
    }
}







