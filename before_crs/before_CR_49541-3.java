/*Added count intent actions.

Added count intent actions as specified inhttps://github.com/Pimm/android-count-intents/blob/master/README.md, which allow components
on a device to use each other's countdown (timer) and countup (stopwatch) functionalities.
Use case #1: A user requests Voice Actions to start a timer. VA in turn uses an intent to start
an activity that displays the countdown.
Use case #2: A cooking app uses an intent to start a timer, allowing a user to cook some tasty
pasta for exactly the correct amount of time according to the recipe.

If the intent specifications are no good, please provide some feedback.

I'm uncertain as to what to do with @SdkConstant(SdkConstantType.ACTIVITY_INTENT_ACTION). Please do
correct me.

Change-Id:I05feb77535a2bb2e722fd086b5a9e9aac47b1113*/
//Synthetic comment -- diff --git a/core/java/android/content/Intent.java b/core/java/android/content/Intent.java
//Synthetic comment -- index 7447ed1..bdf443d 100644

//Synthetic comment -- @@ -1369,6 +1369,31 @@
*/
public static final String METADATA_SETUP_VERSION = "android.SETUP_VERSION";

// ---------------------------------------------------------------------
// ---------------------------------------------------------------------
// Standard intent broadcast actions (see action variable).







