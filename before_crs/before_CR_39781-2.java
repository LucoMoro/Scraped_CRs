/*GlobalActions: move silent mode to the bottom

The silent mode toggle is supposed to be the last, but when
Multi-user is enabled, the user list is shown below it.

Change-Id:I889ec3e1dbeb533eeb031ed86e95606b62fba812*/
//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/GlobalActions.java b/policy/src/com/android/internal/policy/impl/GlobalActions.java
//Synthetic comment -- index d1f8ef1..cba3661 100644

//Synthetic comment -- @@ -308,16 +308,16 @@
});
}

        // last: silent mode
        if (SHOW_SILENT_TOGGLE) {
            mItems.add(mSilentModeAction);
        }

// one more thing: optionally add a list of users to switch to
if (SystemProperties.getBoolean("fw.power_user_switcher", false)) {
addUsersToMenu(mItems);
}

mAdapter = new MyAdapter();

AlertParams params = new AlertParams(mContext);







