/*show SELECT ALL icon with text in landscape mode

Though set config_allowActionMenuItemTextWithIcon as true,
icon for the "SELECT ALL" menu on ActionBar is not shown as staring
in landscape mode.
To fix it, use "SELECT ALL" icon in onCreateActionMode() to show the
icon and text together.

To show or hide text is decided by updateTextButtonVisibility() of
core/java/com/android/internal/view/menu/ActionMenuItemView.java

STEPS TO REPRODUCE: (please be specific)
1. launch Browser/Chrome and go to google.com
2. rotate to landscape mode
3. long press on URL address

Bug: 8073761
Change-Id:Ie0e0aa45f0dff609ed8c03e4423b163bad5452ed*/




//Synthetic comment -- diff --git a/core/java/android/widget/Editor.java b/core/java/android/widget/Editor.java
//Synthetic comment -- index b1a44c5..4a7426b 100644

//Synthetic comment -- @@ -2659,23 +2659,14 @@
TypedArray styledAttributes = mTextView.getContext().obtainStyledAttributes(
com.android.internal.R.styleable.SelectionModeDrawables);

mode.setTitle(mTextView.getContext().getString(
com.android.internal.R.string.textSelectionCABTitle));
mode.setSubtitle(null);
mode.setTitleOptionalHint(true);

menu.add(0, TextView.ID_SELECT_ALL, 0, com.android.internal.R.string.selectAll).
                    setIcon(styledAttributes.getResourceId(
                            R.styleable.SelectionModeDrawables_actionModeSelectAllDrawable, 0)).
setAlphabeticShortcut('a').
setShowAsAction(
MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);







