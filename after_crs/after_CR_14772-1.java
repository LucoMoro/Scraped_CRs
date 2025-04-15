/*fix build

Change-Id:I86ee569ec81e6c690f7697237be7d94307014142*/




//Synthetic comment -- diff --git a/telephony/java/android/telephony/ServiceState.java b/telephony/java/android/telephony/ServiceState.java
//Synthetic comment -- index 71e258f..bf3dcc7 100644

//Synthetic comment -- @@ -307,10 +307,11 @@
* In GSM/UMTS, numeric format is 3 digit country code plus 2 or 3 digit
* network code.
*
     * @return numeric format of operator, null if unregistered or unknown
     */
    /*
* The country code can be decoded using
* {@link com.android.internal.telephony.MccTable#countryCodeForMcc(int)}.
*/
public String getOperatorNumeric() {
return mOperatorNumeric;







