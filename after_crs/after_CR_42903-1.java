/*Re-read shortcut labels if the language has changed

The shortcut label isn't updated to the current language
if the phone's language has changed.

The problem was that the shortcut label is stored in a
static variable which is set once and kept throughout the
life time of the process.

Change-Id:I89c5875fbf28fb82e073166c472ca205d28674fb*/




//Synthetic comment -- diff --git a/core/java/com/android/internal/view/menu/MenuItemImpl.java b/core/java/com/android/internal/view/menu/MenuItemImpl.java
//Synthetic comment -- index 9f7441d..7189610 100644

//Synthetic comment -- @@ -93,6 +93,7 @@
*/ 
private ContextMenuInfo mMenuInfo;

    private static String sLanguage;
private static String sPrependShortcutLabel;
private static String sEnterShortcutLabel;
private static String sDeleteShortcutLabel;
//Synthetic comment -- @@ -114,7 +115,9 @@
MenuItemImpl(MenuBuilder menu, int group, int id, int categoryOrder, int ordering,
CharSequence title, int showAsAction) {

        String lang = menu.getContext().getResources().getConfiguration().locale.toString();
        if (sPrependShortcutLabel == null || !lang.equals(sLanguage)) {
            sLanguage = lang;
// This is instantiated from the UI thread, so no chance of sync issues 
sPrependShortcutLabel = menu.getContext().getResources().getString(
com.android.internal.R.string.prepend_shortcut_label);







