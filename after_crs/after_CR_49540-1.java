/*Capitalised words in android.content.Intent documentation.

Capitalised the word "nothing" in android.content.Intent documentation, for sexy consistency.

Change-Id:I42ea3c503eee968cc603d70b29a5600022da4344*/




//Synthetic comment -- diff --git a/core/java/android/content/Intent.java b/core/java/android/content/Intent.java
//Synthetic comment -- index 89b1bbd..7447ed1 100644

//Synthetic comment -- @@ -601,8 +601,8 @@
/**
*  Activity Action: Start as a main entry point, does not expect to
*  receive data.
     *  <p>Input: Nothing
     *  <p>Output: Nothing
*/
@SdkConstant(SdkConstantType.ACTIVITY_INTENT_ACTION)
public static final String ACTION_MAIN = "android.intent.action.MAIN";
//Synthetic comment -- @@ -616,7 +616,7 @@
* supplied by the URI; when used with a tel: URI it will invoke the
* dialer.
* <p>Input: {@link #getData} is URI from which to retrieve data.
     * <p>Output: Nothing.
*/
@SdkConstant(SdkConstantType.ACTIVITY_INTENT_ACTION)
public static final String ACTION_VIEW = "android.intent.action.VIEW";
//Synthetic comment -- @@ -633,7 +633,7 @@
* to the recipient to decide where the data should be attached; the intent
* does not specify the ultimate destination.
* <p>Input: {@link #getData} is URI of data to be attached.
     * <p>Output: Nothing.
*/
@SdkConstant(SdkConstantType.ACTIVITY_INTENT_ACTION)
public static final String ACTION_ATTACH_DATA = "android.intent.action.ATTACH_DATA";
//Synthetic comment -- @@ -641,7 +641,7 @@
/**
* Activity Action: Provide explicit editable access to the given data.
* <p>Input: {@link #getData} is URI of data to be edited.
     * <p>Output: Nothing.
*/
@SdkConstant(SdkConstantType.ACTIVITY_INTENT_ACTION)
public static final String ACTION_EDIT = "android.intent.action.EDIT";
//Synthetic comment -- @@ -936,7 +936,7 @@
* <p>Input: If nothing, an empty dialer is started; else {@link #getData}
* is URI of a phone number to be dialed or a tel: URI of an explicit phone
* number.
     * <p>Output: Nothing.
*/
@SdkConstant(SdkConstantType.ACTIVITY_INTENT_ACTION)
public static final String ACTION_DIAL = "android.intent.action.DIAL";
//Synthetic comment -- @@ -945,7 +945,7 @@
* <p>Input: If nothing, an empty dialer is started; else {@link #getData}
* is URI of a phone number to be dialed or a tel: URI of an explicit phone
* number.
     * <p>Output: Nothing.
*
* <p>Note: there will be restrictions on which applications can initiate a
* call; most applications should use the {@link #ACTION_DIAL}.
//Synthetic comment -- @@ -960,7 +960,7 @@
* data.
* <p>Input: {@link #getData} is URI of a phone number to be dialed or a
* tel: URI of an explicit phone number.
     * <p>Output: Nothing.
* @hide
*/
public static final String ACTION_CALL_EMERGENCY = "android.intent.action.CALL_EMERGENCY";
//Synthetic comment -- @@ -969,14 +969,14 @@
* specified by the data.
* <p>Input: {@link #getData} is URI of a phone number to be dialed or a
* tel: URI of an explicit phone number.
     * <p>Output: Nothing.
* @hide
*/
public static final String ACTION_CALL_PRIVILEGED = "android.intent.action.CALL_PRIVILEGED";
/**
* Activity Action: Send a message to someone specified by the data.
* <p>Input: {@link #getData} is URI describing the target.
     * <p>Output: Nothing.
*/
@SdkConstant(SdkConstantType.ACTIVITY_INTENT_ACTION)
public static final String ACTION_SENDTO = "android.intent.action.SENDTO";
//Synthetic comment -- @@ -1013,7 +1013,7 @@
* appropriate, are: {@link #EXTRA_EMAIL}, {@link #EXTRA_CC},
* {@link #EXTRA_BCC}, {@link #EXTRA_SUBJECT}.
* <p>
     * Output: Nothing.
*/
@SdkConstant(SdkConstantType.ACTIVITY_INTENT_ACTION)
public static final String ACTION_SEND = "android.intent.action.SEND";
//Synthetic comment -- @@ -1051,14 +1051,14 @@
* appropriate, are: {@link #EXTRA_EMAIL}, {@link #EXTRA_CC},
* {@link #EXTRA_BCC}, {@link #EXTRA_SUBJECT}.
* <p>
     * Output: Nothing.
*/
@SdkConstant(SdkConstantType.ACTIVITY_INTENT_ACTION)
public static final String ACTION_SEND_MULTIPLE = "android.intent.action.SEND_MULTIPLE";
/**
* Activity Action: Handle an incoming phone call.
     * <p>Input: Nothing.
     * <p>Output: Nothing.
*/
@SdkConstant(SdkConstantType.ACTIVITY_INTENT_ACTION)
public static final String ACTION_ANSWER = "android.intent.action.ANSWER";
//Synthetic comment -- @@ -1082,14 +1082,14 @@
/**
* Activity Action: Delete the given data from its container.
* <p>Input: {@link #getData} is URI of data to be deleted.
     * <p>Output: Nothing.
*/
@SdkConstant(SdkConstantType.ACTIVITY_INTENT_ACTION)
public static final String ACTION_DELETE = "android.intent.action.DELETE";
/**
* Activity Action: Run the data, whatever that means.
* <p>Input: ?  (Note: this is currently specific to the test harness.)
     * <p>Output: Nothing.
*/
@SdkConstant(SdkConstantType.ACTIVITY_INTENT_ACTION)
public static final String ACTION_RUN = "android.intent.action.RUN";
//Synthetic comment -- @@ -1115,7 +1115,7 @@
* <p>Input: {@link android.app.SearchManager#QUERY getStringExtra(SearchManager.QUERY)}
* is the text to search for.  If empty, simply
* enter your search results Activity with the search UI activated.
     * <p>Output: Nothing.
*/
@SdkConstant(SdkConstantType.ACTIVITY_INTENT_ACTION)
public static final String ACTION_SEARCH = "android.intent.action.SEARCH";
//Synthetic comment -- @@ -1124,7 +1124,7 @@
* <p>Input: {@link android.app.SearchManager#QUERY getStringExtra(SearchManager.QUERY)}
* is the text to search for.  If empty, simply
* enter your search results Activity with the search UI activated.
     * <p>Output: Nothing.
*/
@SdkConstant(SdkConstantType.ACTIVITY_INTENT_ACTION)
public static final String ACTION_SYSTEM_TUTORIAL = "android.intent.action.SYSTEM_TUTORIAL";
//Synthetic comment -- @@ -1136,22 +1136,23 @@
* a url starts with http or https, the site will be opened. If it is plain
* text, Google search will be applied.
* <p>
     * Output: Nothing.
*/
@SdkConstant(SdkConstantType.ACTIVITY_INTENT_ACTION)
public static final String ACTION_WEB_SEARCH = "android.intent.action.WEB_SEARCH";
/**
* Activity Action: Perform assist action.
* <p>
     * Input: Nothing
     * <p>
     * Output: Nothing.
*/
@SdkConstant(SdkConstantType.ACTIVITY_INTENT_ACTION)
public static final String ACTION_ASSIST = "android.intent.action.ASSIST";
/**
* Activity Action: List all available applications
* <p>Input: Nothing.
     * <p>Output: Nothing.
*/
@SdkConstant(SdkConstantType.ACTIVITY_INTENT_ACTION)
public static final String ACTION_ALL_APPS = "android.intent.action.ALL_APPS";







