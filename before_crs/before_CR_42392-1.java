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
     * Inserts Korean lead consonants records of name for the given structured name.
*/
private void appendKoreanNameConsonantsLookup(IndexBuilder builder, String name) {
        int position = 0;
        int consonantLength = 0;
int character;

        final int stringLength = name.length();
mStringBuilder.setLength(0);
do {
            character = name.codePointAt(position++);
if ((character == 0x20) || (character == 0x2c) || (character == 0x2E)) {
// Skip spaces, commas and periods.
continue;
}
            // Exclude characters that are not in Korean leading consonants area
            // and Korean characters area.
            if ((character < 0x1100) || (character > 0x1112 && character < 0x3131) ||
                    (character > 0x314E && character < 0xAC00) ||
                    (character > 0xD7A3)) {
                break;
            }
// Decompose and take a only lead-consonant for composed Korean characters.
            if (character >= 0xAC00) {
// Lead consonant = "Lead consonant base" +
//      (character - "Korean Character base") /
//          ("Lead consonant count" * "middle Vowel count")
//Synthetic comment -- @@ -201,25 +197,18 @@
} else if (character >= 0x3131) {
// Hangul Compatibility Jamo area 0x3131 ~ 0x314E :
// Convert to Hangul Jamo area 0x1100 ~ 0x1112
                if (character - 0x3131 >= KOREAN_JAUM_CONVERT_MAP.length) {
                    // This is not lead-consonant
                    break;
                }
                character = KOREAN_JAUM_CONVERT_MAP[character - 0x3131];
                if (character == 0) {
                    // This is not lead-consonant
                    break;
}
}
            mStringBuilder.appendCodePoint(character);
            consonantLength++;
        } while (position < stringLength);

        // At least, insert consonants when Korean characters are two or more.
        // Only one character cases are covered by NAME_COLLATION_KEY
        if (consonantLength > 1) {
builder.appendName(mStringBuilder.toString());
        }
}

protected String normalizeName(String name) {







