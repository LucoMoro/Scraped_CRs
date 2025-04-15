/*Settings: Display EONS name in phone status.

Change-Id:Iad74561e42f022719b180547f8ef849aa2639e8f*/




//Synthetic comment -- diff --git a/src/com/android/settings/deviceinfo/Status.java b/src/com/android/settings/deviceinfo/Status.java
//Synthetic comment -- index 99a8975..9adbd0a 100644

//Synthetic comment -- @@ -340,7 +340,9 @@
} else {
setSummaryText("roaming_state", mRes.getString(R.string.radioInfo_roaming_not));
}
        String name = mPhone.getEons();
        if (name == null) name = serviceState.getOperatorAlphaLong();
        setSummaryText("operator_name", name);
}

void updateSignalStrength() {







