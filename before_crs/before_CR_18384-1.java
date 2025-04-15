/*Change PhoneNumberUtils MIN_MATCH to match 6 digits.

Argentina and some other Latin American countries
use 6 digits phone numbers. This change extends the
correct number matching digits for these countries.

Change-Id:I3d410c26fb285e5bd5fcd6d235e5ce492b1b2ebaSigned-off-by: David Sobreira <davidps.marques@lge.com>*/
//Synthetic comment -- diff --git a/telephony/java/android/telephony/PhoneNumberUtils.java b/telephony/java/android/telephony/PhoneNumberUtils.java
//Synthetic comment -- index 1d3ad81..e69d975 100644

//Synthetic comment -- @@ -396,7 +396,7 @@
* enough for caller ID purposes.
*
* - Compares from right to left
     * - requires MIN_MATCH (7) characters to match
* - handles common trunk prefixes and international prefixes
*   (basically, everything except the Russian trunk prefix)
*
//Synthetic comment -- @@ -654,7 +654,7 @@
}

/**
     * Returns the rightmost MIN_MATCH (5) characters in the network portion
* in *reversed* order
*
* This can be used to do a database lookup against the column
//Synthetic comment -- @@ -1334,9 +1334,9 @@
//
// see http://en.wikipedia.org/wiki/Short_code#Regional_differences for reference
//
    // However, in order to loose match 650-555-1212 and 555-1212, we need to set the min match
    // to 7.
    static final int MIN_MATCH = 7;

/**
* isEmergencyNumber: checks a given number against the list of







