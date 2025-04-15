/*Fix preload classname for phone policy.

	PhoneWindow$ContextMenuCallback is renamed to PhoneWindow$DialogMenuCallback
	from commit 4267534d1c42af847ed0cefd1c88c99f66b36571
	    "Action Bar now supports submenus as popups."

Change-Id:Ie9165d9458358dabe03b499862fcced690845e6f*/




//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/Policy.java b/policy/src/com/android/internal/policy/impl/Policy.java
//Synthetic comment -- index a490729..153ef0f 100644

//Synthetic comment -- @@ -41,7 +41,7 @@
"com.android.internal.policy.impl.PhoneLayoutInflater",
"com.android.internal.policy.impl.PhoneWindow",
"com.android.internal.policy.impl.PhoneWindow$1",
        "com.android.internal.policy.impl.PhoneWindow$DialogMenuCallback",
"com.android.internal.policy.impl.PhoneWindow$DecorView",
"com.android.internal.policy.impl.PhoneWindow$PanelFeatureState",
"com.android.internal.policy.impl.PhoneWindow$PanelFeatureState$SavedState",







