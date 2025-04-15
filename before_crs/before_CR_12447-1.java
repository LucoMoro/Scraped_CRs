/*Added intents for hangup, hold and conference*/
//Synthetic comment -- diff --git a/core/java/android/content/Intent.java b/core/java/android/content/Intent.java
//Synthetic comment -- index dfda09c..c41822d 100644

//Synthetic comment -- @@ -939,6 +939,28 @@
@SdkConstant(SdkConstantType.ACTIVITY_INTENT_ACTION)
public static final String ACTION_ANSWER = "android.intent.action.ANSWER";
/**
* Activity Action: Insert an empty item into the given container.
* <p>Input: {@link #getData} is URI of the directory (vnd.android.cursor.dir/*)
* in which to place the data.







