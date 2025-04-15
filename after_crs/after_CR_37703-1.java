/*Add @NonNull annotations to overridden method

Eclipse 4.2's new null analysis is useful, but (unfortunately)
requires nullness annotations to be repeated on methods overriding or
implementing interfaces where there are other nullness annotations.
This changeset adds this for the manifest merger.

(Eclipse Issue 381443 tracks a request for making the null analysis
not require this and simply pick up annotations from the overridden or
implemented method signature.)

Change-Id:I063b7713c5dbca642a8afd3231bbe4002ec130e1*/




//Synthetic comment -- diff --git a/manifmerger/src/com/android/manifmerger/MergerLog.java b/manifmerger/src/com/android/manifmerger/MergerLog.java
//Synthetic comment -- index 417e6bb..c1f8cb2 100755

//Synthetic comment -- @@ -36,9 +36,9 @@
return new IMergerLog() {
@Override
public void error(
                    @NonNull Severity severity,
                    @NonNull FileAndLine location,
                    @NonNull String message,
Object...msgParams) {

switch(severity) {
//Synthetic comment -- @@ -64,10 +64,10 @@
}

@Override
            public void conflict(@NonNull Severity severity,
                    @NonNull FileAndLine location1,
                    @NonNull FileAndLine location2,
                    @NonNull String message,
Object...msgParams) {

switch(severity) {
//Synthetic comment -- @@ -116,9 +116,9 @@
return new IMergerLog() {
@Override
public void error(
                    @NonNull Severity severity,
                    @NonNull FileAndLine location,
                    @NonNull String message,
Object...msgParams) {

if (filePath1 != null) {
//Synthetic comment -- @@ -129,10 +129,10 @@
}

@Override
            public void conflict(@NonNull Severity severity,
                    @NonNull FileAndLine location1,
                    @NonNull FileAndLine location2,
                    @NonNull String message,
Object...msgParams) {

if (filePath1 != null) {







