/*To prevent the reference to null pointer

When the "cb" is null, the reference to member method using that object should not be executed.

Change-Id:Ib9a1241ea11ad3fdec4fe51e4eae7ba800568097*/




//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/PhoneWindow.java b/policy/src/com/android/internal/policy/impl/PhoneWindow.java
old mode 100644
new mode 100755
//Synthetic comment -- index dffccf8..7297115

//Synthetic comment -- @@ -329,7 +329,7 @@
}

// Callback and return if the callback does not want to show the menu
            if ( (cb != null) && !cb.onPreparePanel(st.featureId, st.createdPanelView, st.menu)) {
return false;
}








