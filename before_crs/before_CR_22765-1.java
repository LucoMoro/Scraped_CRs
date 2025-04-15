/*I think mobile phone number more popular than home number, set mobile number as default

Change-Id:If011612ca03b6cb0f290c526b567682f3ba7b04e*/
//Synthetic comment -- diff --git a/src/com/android/contacts/model/ExchangeSource.java b/src/com/android/contacts/model/ExchangeSource.java
//Synthetic comment -- index 3f2ab6c..153d99d 100644

//Synthetic comment -- @@ -127,8 +127,8 @@
if (inflateLevel >= ContactsSource.LEVEL_CONSTRAINTS) {
kind.typeColumn = Phone.TYPE;
kind.typeList = Lists.newArrayList();
            kind.typeList.add(buildPhoneType(Phone.TYPE_HOME).setSpecificMax(2));
kind.typeList.add(buildPhoneType(Phone.TYPE_MOBILE).setSpecificMax(1));
kind.typeList.add(buildPhoneType(Phone.TYPE_WORK).setSpecificMax(2));
kind.typeList.add(buildPhoneType(Phone.TYPE_FAX_WORK).setSecondary(true)
.setSpecificMax(1));








//Synthetic comment -- diff --git a/src/com/android/contacts/model/FallbackSource.java b/src/com/android/contacts/model/FallbackSource.java
//Synthetic comment -- index f36752a..71a72f2 100644

//Synthetic comment -- @@ -194,8 +194,8 @@
if (inflateLevel >= ContactsSource.LEVEL_CONSTRAINTS) {
kind.typeColumn = Phone.TYPE;
kind.typeList = Lists.newArrayList();
            kind.typeList.add(buildPhoneType(Phone.TYPE_HOME));
kind.typeList.add(buildPhoneType(Phone.TYPE_MOBILE));
kind.typeList.add(buildPhoneType(Phone.TYPE_WORK));
kind.typeList.add(buildPhoneType(Phone.TYPE_FAX_WORK).setSecondary(true));
kind.typeList.add(buildPhoneType(Phone.TYPE_FAX_HOME).setSecondary(true));








//Synthetic comment -- diff --git a/src/com/android/contacts/model/GoogleSource.java b/src/com/android/contacts/model/GoogleSource.java
//Synthetic comment -- index d6dfbb6..883f182 100644

//Synthetic comment -- @@ -81,8 +81,8 @@
if (inflateLevel >= ContactsSource.LEVEL_CONSTRAINTS) {
kind.typeColumn = Phone.TYPE;
kind.typeList = Lists.newArrayList();
            kind.typeList.add(buildPhoneType(Phone.TYPE_HOME));
kind.typeList.add(buildPhoneType(Phone.TYPE_MOBILE));
kind.typeList.add(buildPhoneType(Phone.TYPE_WORK));
kind.typeList.add(buildPhoneType(Phone.TYPE_FAX_WORK).setSecondary(true));
kind.typeList.add(buildPhoneType(Phone.TYPE_FAX_HOME).setSecondary(true));







