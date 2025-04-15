/*External: Avoid '0' being discarded in dial string.

GCF test case requires '+0123' to be dialed.
But when '+0123' is dialed, '0' gets discarded and number
changes to '+123'.
'+01' gets parsed as the countrycode of US & gets replaced
as '+1'. Hence avoid the replacement for this scenario.

Below GCF test cases were failing:
SAT TC 27.22.6.1.6
SAT TC 27.22.4.24.1.2
USAT TC 27.22.4.24.1.1

Change-Id:I340b8267112a20f8522de6e942bd88718eb4da20*/
//Synthetic comment -- diff --git a/java/src/com/android/i18n/phonenumbers/PhoneNumberUtil.java b/java/src/com/android/i18n/phonenumbers/PhoneNumberUtil.java
//Synthetic comment -- index d65de33..4f349a1 100644

//Synthetic comment -- @@ -2037,7 +2037,14 @@
potentialCountryCode = Integer.parseInt(fullNumber.substring(0, i));
if (countryCallingCodeToRegionCodeMap.containsKey(potentialCountryCode)) {
nationalNumber.append(fullNumber.substring(i));
        return potentialCountryCode;
}
}
return 0;







