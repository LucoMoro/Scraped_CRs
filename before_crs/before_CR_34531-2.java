/*WLAN: Fix Connect Button disable state

Makes connect button disabled when
starting connect dialog for secured network.

Change-Id:I4a4194ba10897e1fd787489300db2f92ab102891Signed-off-by: Christian Bejram <christian.bejram@stericsson.com>*/
//Synthetic comment -- diff --git a/src/com/android/settings/wifi/WifiConfigController.java b/src/com/android/settings/wifi/WifiConfigController.java
//Synthetic comment -- index c64a225..7c359e3 100644

//Synthetic comment -- @@ -262,11 +262,7 @@
}
}


mConfigUi.setCancelButton(context.getString(R.string.wifi_cancel));
        if (mConfigUi.getSubmitButton() != null) {
            enableSubmitIfAppropriate();
        }
}

private void addRow(ViewGroup group, int name, String value) {








//Synthetic comment -- diff --git a/src/com/android/settings/wifi/WifiDialog.java b/src/com/android/settings/wifi/WifiDialog.java
//Synthetic comment -- index f980d0c..048060a 100644

//Synthetic comment -- @@ -59,6 +59,17 @@
}

@Override
public boolean isEdit() {
return mEdit;
}







