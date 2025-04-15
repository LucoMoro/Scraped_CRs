/*Fix camera extra settings menu
If the list of settings entries is scrolled, the adapter returns a
reused View instead of creating a new one. This makes the last
entries unusable. This has been broken for ages but only shows up
if the camera has more than a few settings (which the stock camera
doesn't)

Change-Id:I7b7a4b08040e2c148ab84dbd9e19143409d42b65*/




//Synthetic comment -- diff --git a/src/com/android/camera/ui/OtherSettingsPopup.java b/src/com/android/camera/ui/OtherSettingsPopup.java
//Synthetic comment -- index e2920f5..d0742a7 100644

//Synthetic comment -- @@ -70,7 +70,6 @@

@Override
public View getView(int position, View convertView, ViewGroup parent) {

ListPreference pref = mListItem.get(position);








