/*Fix for the Android issue no 15127

The 24hr option turn ON/OFF wasn't working as expected. The changes are not reflecting in the notification bar.
This fix will resolve this issue.

Change-Id:Ib704026ba514856155576be1f437f9c3ad8289d0*/
//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/statusbar/Clock.java b/packages/SystemUI/src/com/android/systemui/statusbar/Clock.java
//Synthetic comment -- index 9fc8df5..895ee72 100644

//Synthetic comment -- @@ -57,7 +57,7 @@
private static final int AM_PM_STYLE_SMALL   = 1;
private static final int AM_PM_STYLE_GONE    = 2;

    private static final int AM_PM_STYLE = AM_PM_STYLE_GONE;

public Clock(Context context) {
this(context, null);







