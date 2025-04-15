/*Corrected debug message in WindowManager

When WindowManager reports "Key dispatching timed out" it prints out
information about the window state that was present at the time the key
was sent to that window. There is a minor error in the class representing
the recorded window state so that the currently focused window is printed
instead of the recorded focused window.

Change-Id:I29a5471ef725e30f812ffd57fd4597ce81c0c7f2*/
//Synthetic comment -- diff --git a/services/java/com/android/server/WindowManagerService.java b/services/java/com/android/server/WindowManagerService.java
//Synthetic comment -- index 2bcf4cd..b90b03b 100644

//Synthetic comment -- @@ -5478,7 +5478,7 @@
+ " fin=" + finished + " gfw=" + gotFirstWindow
+ " ed=" + eventDispatching + " tts=" + timeToSwitch
+ " wf=" + wasFrozen + " fp=" + focusPaused
                        + " mcf=" + mCurrentFocus + "}}";
}
};
private DispatchState mDispatchState = null;







