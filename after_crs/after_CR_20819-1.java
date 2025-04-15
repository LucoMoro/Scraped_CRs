/*Fix a dead code on initializing panel state's menu

Change-Id:I0c92c6abd891381fccdd5eedf4455e8f426d6b4fSigned-off-by: Douglas Drumond <douglas@eee19.com>*/




//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/PhoneWindow.java b/policy/src/com/android/internal/policy/impl/PhoneWindow.java
//Synthetic comment -- index dffccf8..fe096a9 100644

//Synthetic comment -- @@ -316,16 +316,17 @@
if (st.createdPanelView == null) {
// Init the panel state's menu--return false if init failed
if (st.menu == null) {
                if (!initializePanelMenu(st)) {
return false;
}
            }

            // Call callback, and return if it doesn't want to display menu
            if ((cb == null) || !cb.onCreatePanelMenu(st.featureId, st.menu)) {
                // Ditch the menu created above
                st.menu = null;

                return false;
}

// Callback and return if the callback does not want to show the menu







