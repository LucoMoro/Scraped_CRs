/** Made View activity robust to null and unknown types of IM provider*/




//Synthetic comment -- diff --git a/src/com/android/contacts/ViewContactActivity.java b/src/com/android/contacts/ViewContactActivity.java
//Synthetic comment -- index 28a82b4..ea5de3a 100644

//Synthetic comment -- @@ -726,11 +726,13 @@
|| protocol == ContactMethods.PROTOCOL_MSN) {
entry.maxLabelLines = 2;
}
                        } else if (protocolObj instanceof String) {
String providerName = (String) protocolObj;
entry.label = buildActionString(R.string.actionChat,
providerName, false);
host = providerName.toLowerCase();
                        } else {
                        	host = "";
}

// Only add the intent if there is a valid host







