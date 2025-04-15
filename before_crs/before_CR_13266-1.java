/*Include the API level 4 fields in the Parcel.*/
//Synthetic comment -- diff --git a/location/java/android/location/Address.java b/location/java/android/location/Address.java
//Synthetic comment -- index ac275c6..b152f48 100644

//Synthetic comment -- @@ -500,7 +500,10 @@
a.mAdminArea = in.readString();
a.mSubAdminArea = in.readString();
a.mLocality = in.readString();
a.mThoroughfare = in.readString();
a.mPostalCode = in.readString();
a.mCountryCode = in.readString();
a.mCountryName = in.readString();
//Synthetic comment -- @@ -544,7 +547,10 @@
parcel.writeString(mAdminArea);
parcel.writeString(mSubAdminArea);
parcel.writeString(mLocality);
parcel.writeString(mThoroughfare);
parcel.writeString(mPostalCode);
parcel.writeString(mCountryCode);
parcel.writeString(mCountryName);







