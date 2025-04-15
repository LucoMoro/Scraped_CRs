/*fix automatic timezone failing on some GSM networks

in the code that parses the NITZ string, someone made the assumption that if
any data follows the DST flag, it must be be the android emulator appending
an actual timezone string in "Area!Location" format.  However, it seems that
some carriers or perhaps in some locations, the carrier sends an additional
field in the NITZ string.  For example, this is a string from AT&T:

12/07/01,09:37:43-16,01,310

The extra field is "310" above.  This patch will only assume that the field
in question is a textual timezone if and only if it contains an exclamation
point.  Otherwise, the extra field is ignored and the timezone is parsed
as it should be.

Change-Id:I6cc4278027a6de5545d76b803f5359923e60878aSigned-off-by: Gary Dezern <garyd9@gmail.com>*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index 6e2b262d..b5fecee 100644

//Synthetic comment -- @@ -1354,8 +1354,10 @@
// As a special extension, the Android emulator appends the name of
// the host computer's timezone to the nitz string. this is zoneinfo
// timezone name of the form Area!Location or Area!Location!SubLocation
            // so we need to convert the ! into /.  If there's no "!", then maybe
            // the carrier is appending something extra (as AT&T does) and it
            // should be ignored
            if ((nitzSubs.length >= 9) && (nitzSubs[8].indexOf('!') != -1)) {
String  tzname = nitzSubs[8].replace('!','/');
zone = TimeZone.getTimeZone( tzname );
}







