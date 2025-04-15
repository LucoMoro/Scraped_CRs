/*Remove title bar background drawable from running app detail view

The use of this drawable made the view look out of place on the new Holo theme,
so removing it makes things look a bit more consistent.

Change-Id:Id12ad9c7d13b7a9813f656b0a3e4ac51fcc988cdSigned-off-by: Eddie Ringle <eddie.ringle@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/settings/applications/RunningServiceDetails.java b/src/com/android/settings/applications/RunningServiceDetails.java
//Synthetic comment -- index 631e747..08cb0e0 100644

//Synthetic comment -- @@ -439,7 +439,6 @@

mAllDetails = (ViewGroup)view.findViewById(R.id.all_details);
mSnippet = (ViewGroup)view.findViewById(R.id.snippet);
        mSnippet.setBackgroundResource(com.android.internal.R.drawable.title_bar_medium);
mSnippet.setPadding(0, mSnippet.getPaddingTop(), 0, mSnippet.getPaddingBottom());
mSnippetViewHolder = new RunningProcessesView.ViewHolder(mSnippet);








