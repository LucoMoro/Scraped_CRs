/*Fix typo in the Fragment lifecycle documentation.

In the Fragment lifecycle documentation there was a typo where it said
"Activity.onCreaate" instead of "Activity.onCreate".
Issue 21018

Change-Id:I49257a862c9e05265d9e982fdaaf3478c446a541Signed-off-by: Chris Trotman <chris.trotman.0@gmail.com>*/




//Synthetic comment -- diff --git a/core/java/android/app/Fragment.java b/core/java/android/app/Fragment.java
//Synthetic comment -- index f427e78..47054d4 100644

//Synthetic comment -- @@ -203,7 +203,7 @@
* <li> {@link #onCreateView} creates and returns the view hierarchy associated
* with the fragment.
* <li> {@link #onActivityCreated} tells the fragment that its activity has
 * completed its own {@link Activity#onCreate Activity.onCreate}.
* <li> {@link #onStart} makes the fragment visible to the user (based on its
* containing activity being started).
* <li> {@link #onResume} makes the fragment interacting with the user (based on its







