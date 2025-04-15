/*Look in the profile db when doing a caller info query

Check the profile db if no contact is found when doing a phone lookup.

Android Bug: 23153

Change-Id:Ia4396ee4aa384f0023f337a0da145a64713c697d*/
//Synthetic comment -- diff --git a/core/java/android/provider/ContactsContract.java b/core/java/android/provider/ContactsContract.java
//Synthetic comment -- index 83acef8..bdb2b74 100644

//Synthetic comment -- @@ -4665,6 +4665,14 @@
"phone_lookup");

/**
* The MIME type of {@link #CONTENT_FILTER_URI} providing a directory of phone lookup rows.
*
* @hide








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/CallerInfo.java b/telephony/java/com/android/internal/telephony/CallerInfo.java
//Synthetic comment -- index 5d1f758..da98970 100644

//Synthetic comment -- @@ -263,6 +263,7 @@
Uri contactUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

CallerInfo info = getCallerInfo(context, contactUri);
info = doSecondaryLookupIfNecessary(context, number, info);

// if no query results were returned with a viable number,
//Synthetic comment -- @@ -299,6 +300,26 @@
}

/**
* getCallerId: a convenience method to get the caller id for a given
* number.
*








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/CallerInfoAsyncQuery.java b/telephony/java/com/android/internal/telephony/CallerInfoAsyncQuery.java
//Synthetic comment -- index 4912749..7cdd269 100644

//Synthetic comment -- @@ -237,6 +237,8 @@
mCallerInfo = new CallerInfo().markAsVoiceMail();
} else {
mCallerInfo = CallerInfo.getCallerInfo(mQueryContext, mQueryUri, cursor);
if (DBG) Log.d(LOG_TAG, "==> Got mCallerInfo: " + mCallerInfo);

CallerInfo newCallerInfo = CallerInfo.doSecondaryLookupIfNecessary(







