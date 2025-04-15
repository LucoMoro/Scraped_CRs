/*Visibility and unused code fixes

Modify visibility of variables and functions according to report from UCDetector.

Also remove unused constructor SyncStateCheckBoxPreference(Context, AttributeSet).
This in turn makes android.util.AttributeSet an unnecessary import.

Change-Id:Ia0a182c2826f619d7ec4e5c0604c1292ab4794ae*/




//Synthetic comment -- diff --git a/src/com/android/settings/AccountPreference.java b/src/com/android/settings/AccountPreference.java
//Synthetic comment -- index cba267e..2e53d92 100644

//Synthetic comment -- @@ -35,9 +35,9 @@
*/
public class AccountPreference extends Preference {
private static final String TAG = "AccountPreference";
    protected static final int SYNC_ENABLED = 0; // all know sync adapters are enabled and OK
    protected static final int SYNC_DISABLED = 1; // no sync adapters are enabled
    protected static final int SYNC_ERROR = 2; // one or more sync adapters have a problem
private int mStatus;
private Account mAccount;
private ArrayList<String> mAuthorities;
//Synthetic comment -- @@ -45,7 +45,7 @@
private ImageView mSyncStatusIcon;
private ImageView mProviderIconView;

    protected AccountPreference(Context context, Account account, Drawable icon,
ArrayList<String> authorities) {
super(context);
mAccount = account;








//Synthetic comment -- diff --git a/src/com/android/settings/ProviderPreference.java b/src/com/android/settings/ProviderPreference.java
//Synthetic comment -- index 2eb1f5b..01bb517 100644

//Synthetic comment -- @@ -34,7 +34,7 @@
private CharSequence mProviderName;
private String mAccountType;

    protected ProviderPreference(Context context, String accountType, Drawable icon, CharSequence providerName) {
super(context);
mAccountType = accountType;
mProviderIcon = icon;








//Synthetic comment -- diff --git a/src/com/android/settings/SyncStateCheckBoxPreference.java b/src/com/android/settings/SyncStateCheckBoxPreference.java
//Synthetic comment -- index 287979e..644f97b 100644

//Synthetic comment -- @@ -21,7 +21,6 @@
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.preference.CheckBoxPreference;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
//Synthetic comment -- @@ -41,14 +40,14 @@
*/
private boolean mOneTimeSyncMode = false;

//     public SyncStateCheckBoxPreference(Context context, AttributeSet attrs) {
//         super(context, attrs);
//         setWidgetLayoutResource(R.layout.preference_widget_sync_toggle);
//         mAccount = null;
//         mAuthority = null;
//     }

    protected SyncStateCheckBoxPreference(Context context, Account account, String authority) {
super(context, null);
mAccount = account;
mAuthority = authority;







