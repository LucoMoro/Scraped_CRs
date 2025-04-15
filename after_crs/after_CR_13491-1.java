/*Changed FDN delete to abort when number is empty rather than when name is empty

3GPP TS 31.102 (USIM application) v9.1.0 states in chapter 4.2.24 EFFDN (Fixed Dialling Number)
that the Dialling Number field is mandatory whereas the Alpha Identifier is optional.
The current implementation of the user interface for editing the fixed dialling list blocks
creation and deletion list entries without a name (corresponding to the Alpha Identifier) but
instead permits entries without a number. This is incompatible with the USIM specification and
implementation.

This contribution corrects the implementation in line with the USIM specification.*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccProvider.java b/telephony/java/com/android/internal/telephony/IccProvider.java
//Synthetic comment -- index 8b54ca8..ff0eca9 100644

//Synthetic comment -- @@ -273,7 +273,7 @@
}
}

        if (TextUtils.isEmpty(number)) {
return 0;
}








