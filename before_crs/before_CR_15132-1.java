/*Disabled verbose logging

Change-Id:I22d4175a9ba233809ba56481d9bde234ff44dcd1*/
//Synthetic comment -- diff --git a/src/com/android/contacts/model/EntityDelta.java b/src/com/android/contacts/model/EntityDelta.java
//Synthetic comment -- index ae30806..539b67a 100644

//Synthetic comment -- @@ -59,7 +59,7 @@
// TODO: optimize by using contentvalues pool, since we allocate so many of them

private static final String TAG = "EntityDelta";
    private static final boolean LOGV = true;

/**
* Direct values from {@link Entity#getEntityValues()}.








//Synthetic comment -- diff --git a/src/com/android/contacts/ui/QuickContactActivity.java b/src/com/android/contacts/ui/QuickContactActivity.java
//Synthetic comment -- index b5e445f..4ab67a0 100644

//Synthetic comment -- @@ -33,7 +33,7 @@
QuickContactWindow.OnDismissListener {
private static final String TAG = "QuickContactActivity";

    static final boolean LOGV = true;
static final boolean FORCE_CREATE = false;

private QuickContactWindow mQuickContact;







