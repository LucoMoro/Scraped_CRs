/*Make hasIccCard return true if RUIM card is present.

Currently hasIccCard method works only for GSM phones with SIM/USIM
cards. Extend that to RUIM as well.

Change-Id:Iefbfc35025074e7414f4abdffea844e9e0325053*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccCard.java b/telephony/java/com/android/internal/telephony/IccCard.java
//Synthetic comment -- index d3a34ec..90f9e8c 100644

//Synthetic comment -- @@ -672,12 +672,11 @@
* @return true if a ICC card is present
*/
public boolean hasIccCard() {
        if (mIccCardStatus == null) {
return false;
        } else {
            // Returns ICC card status for both GSM and CDMA mode
            return mIccCardStatus.getCardState().isCardPresent();
}
}








