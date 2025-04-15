/*Fix ViewRootImpl to find missing focus using D-pad.

By using D-pad, no-focus in non touch mode is rare but legal in a case like below.

1. The first request to get focus for a new activity is handled in the first
   performTraversals() call when activity is not ready for a complete view hierarchy.
   So there might be no focusable yet.
2. If the activity has some menus, ActionMenuView can be attached to the view hierarchy
   in the PhoneWindow.preparePanel() soon.
   So there can be focusables but still not focused.

Fixed ViewRootImpl.deliverKeyEventPostIme() to handle this case to resurrect a focus
if there are focusables.

How to reproduce:
(1) Open "API Demos" application -> Views -> Search View
(2) Select "Action Bar" item using the D-pad
(3) Try to focus the Search View, using the D-pad.

Change-Id:Ic379774f0307f168f0ed775d0f6a9078ac5c9713*/




//Synthetic comment -- diff --git a/core/java/android/view/ViewRootImpl.java b/core/java/android/view/ViewRootImpl.java
//Synthetic comment -- index 27d770b..6d63c6f 100644

//Synthetic comment -- @@ -3793,6 +3793,13 @@
finishInputEvent(q, true);
return;
}
                } else {
                    // find the best view to give focus to in this non-touch-mode with no-focus
                    View v = focusSearch(null, direction);
                    if (v != null && v.requestFocus(direction)) {
                        finishInputEvent(q, true);
                        return;
                    }
}
}
}







