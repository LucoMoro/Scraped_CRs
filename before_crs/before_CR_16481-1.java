/*frameworks/base:Remove callbacks on SelectionNotifier when AdapterView is invalidated

Remove callbacks on SelectionNotifier while invalidating adapter view as
it was causing selection notifier to post itself continuously in the
message queue even after the view is invalidated.

Change-Id:Icfc4ee017cdf73448ae88d3ab7417198675f82d4*/
//Synthetic comment -- diff --git a/core/java/android/widget/AdapterView.java b/core/java/android/widget/AdapterView.java
//Synthetic comment -- index fe6d91a..5691740 100644

//Synthetic comment -- @@ -792,6 +792,10 @@

@Override
public void onInvalidated() {
mDataChanged = true;

if (AdapterView.this.getAdapter().hasStableIds()) {







