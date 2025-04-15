/*Allow easy use of IME on devices with hardware keyboards

When a user has the hardware keyboard hidden on a device that supports
this, allow holding the Menu button to bring up the currently selected
IME.

This change gives users of devices with no keyboards and users with
devices that have hidden keyboards experience parity. Note that the
Search button currently has exactly the same behavior, but there
does not appear to be a way to press the search button when the
keyboard is closed on existing devices.*/




//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/PhoneWindow.java b/phone/com/android/internal/policy/impl/PhoneWindow.java
//Synthetic comment -- index da92275..69c4cb2 100644

//Synthetic comment -- @@ -649,8 +649,9 @@

PanelFeatureState st = getPanelState(featureId, true);
if (!st.isOpen) {
            Configuration config = getContext().getResources().getConfiguration(); 
            if (config.keyboard == Configuration.KEYBOARD_NOKEYS
                    || config.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
mKeycodeMenuTimeoutHandler.removeMessages(MSG_MENU_LONG_PRESS);
mKeycodeMenuTimeoutHandler.sendMessageDelayed(
mKeycodeMenuTimeoutHandler.obtainMessage(MSG_MENU_LONG_PRESS),







