/*Do not dispatch context selection events to non-visible fragments.

When used in a `ViewPager`, fragments that are present on the adjacent,
cached pages will receive context selection dispatches which, depending
on your fragment contents, can be difficult to determine whether or not
the event truly originated from your view.

By using the visible hint we restrict dispatching to only those fragments
which are marked as being visible. Since the fragment pager adapter
updates this setting properly most implementations will be afforded this
fix without any change required. If the user is implementing their own
adapter they likely already understand the implications of these cached
fragments and the reponsibility for updating the boolean falls to them.

Mirrors support library changeIe6a72c1c82c2784774373670007b6f5948fe16daChange-Id:I1bc6f74e26660ef4a3c20419c077e37992412e94*/
//Synthetic comment -- diff --git a/core/java/android/app/FragmentManager.java b/core/java/android/app/FragmentManager.java
//Synthetic comment -- index 0519d3e..7e1daa4 100644

//Synthetic comment -- @@ -1890,7 +1890,7 @@
if (mActive != null) {
for (int i=0; i<mAdded.size(); i++) {
Fragment f = mAdded.get(i);
                if (f != null && !f.mHidden) {
if (f.onContextItemSelected(item)) {
return true;
}







