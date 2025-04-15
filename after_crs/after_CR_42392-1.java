/*Update Korean consonant name lookup inserting method

Existent solution is to create one consonant key
for just Korean leading name.

The modification extends the algorithm to calculate
the lookup keys for all characters.
Korean would be converted to consonant which is
compatible with the origin implementation, and other
characters will be reserved.
The behavior is same as generating the lookup keys.

Change-Id:I4f44a937e25c7d1054a18bbeb00d20d79169c017*/




//Synthetic comment -- diff --git a/src/com/android/providers/contacts/NameLookupBuilder.java b/src/com/android/providers/contacts/NameLookupBuilder.java
//Synthetic comment -- index 5ebbcd1..2d75a8f 100644

//Synthetic comment -- @@ -170,30 +170,26 @@
}

/**
     * Inserts name lookup records for the given Korean structured name.
     *
     * Create a key for each sub string that begins from any position of
     * the name and ends to the end of it. The key is converted from its
     * related sub string, Korean character will change to its lead consonant,
     * other characters will not change.
*/
private void appendKoreanNameConsonantsLookup(IndexBuilder builder, String name) {
        int position = name.length();
int character;

mStringBuilder.setLength(0);
do {
            character = name.codePointAt(--position);
if ((character == 0x20) || (character == 0x2c) || (character == 0x2E)) {
// Skip spaces, commas and periods.
continue;
}
// Decompose and take a only lead-consonant for composed Korean characters.
            if (character >= 0xAC00 && character <= 0xD7A3) {
// Lead consonant = "Lead consonant base" +
//      (character - "Korean Character base") /
//          ("Lead consonant count" * "middle Vowel count")
//Synthetic comment -- @@ -201,25 +197,18 @@
} else if (character >= 0x3131) {
// Hangul Compatibility Jamo area 0x3131 ~ 0x314E :
// Convert to Hangul Jamo area 0x1100 ~ 0x1112
                if (character - 0x3131 < KOREAN_JAUM_CONVERT_MAP.length) {
                    int consonant = KOREAN_JAUM_CONVERT_MAP[character - 0x3131];
                    if (consonant != 0) {
                        // This is a Hangul Compatibility Jamo character
                        // Replace it with the related lead-consonant
                        character = consonant;
                    }
}
}
            mStringBuilder.insert(0, Character.toChars(character));
builder.appendName(mStringBuilder.toString());
        } while (position > 0);
}

protected String normalizeName(String name) {







