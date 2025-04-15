/*Center the tab's text whenever possible

The current implementation of a TabView always use the default 
horizontal gravity of TextView (i.e Gravity.START) to align the
text. While this is not an issue when having a single line text
(laid out using WRAP_CONTENT), it may be a problem when having a
two-line text (seehttps://dl.dropbox.com/u/1484156/tab_not_centered.png)

This patch consists on forcing the gravity to CENTER_HORIZONTAL
when the TabView is used as a tab (and not as an list item) and
the icon is not visible (i.e the tab contains only some text).

Change-Id:Ib81cdf29c9bbae53ebcdb2f866ecc1667da3b355*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/widget/ScrollingTabContainerView.java b/core/java/com/android/internal/widget/ScrollingTabContainerView.java
//Synthetic comment -- index f8ff948..03c04df 100644

//Synthetic comment -- @@ -356,6 +356,8 @@
}

private class TabView extends LinearLayout implements OnLongClickListener {
private ActionBar.Tab mTab;
private TextView mTextView;
private ImageView mIconView;
//Synthetic comment -- @@ -363,6 +365,7 @@

public TabView(Context context, ActionBar.Tab tab, boolean forList) {
super(context, null, com.android.internal.R.attr.actionBarTabStyle);
mTab = tab;

if (forList) {
//Synthetic comment -- @@ -444,6 +447,9 @@
}
mTextView.setText(text);
mTextView.setVisibility(VISIBLE);
} else if (mTextView != null) {
mTextView.setVisibility(GONE);
mTextView.setText(null);







