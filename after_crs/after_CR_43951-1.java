/*Make ASN1 time locale safe.

The ASN1 classes were using SimpleDateFormat with the
default locale which caused the to produce time strings
that did not follow the spec in some locales.

Change-Id:I74979e0b59db075c420b8281497e491b03621cca*/




//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/asn1/ASN1GeneralizedTime.java b/luni/src/main/java/org/apache/harmony/security/asn1/ASN1GeneralizedTime.java
//Synthetic comment -- index e64ebe0..64d7ced 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import java.io.IOException;
import java.nio.charset.Charsets;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
//Synthetic comment -- @@ -83,7 +84,7 @@
private static final String GEN_PATTERN = "yyyyMMddHHmmss.SSS";

public void setEncodingContent(BerOutputStream out) {
        SimpleDateFormat sdf = new SimpleDateFormat(GEN_PATTERN, Locale.US);
sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
String temp = sdf.format(out.content);
// cut off trailing 0s








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/asn1/ASN1UTCTime.java b/luni/src/main/java/org/apache/harmony/security/asn1/ASN1UTCTime.java
//Synthetic comment -- index 2bc8f4b..7c355f8 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import java.io.IOException;
import java.nio.charset.Charsets;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
//Synthetic comment -- @@ -94,7 +95,7 @@
private static final String UTC_PATTERN = "yyMMddHHmmss'Z'";

@Override public void setEncodingContent(BerOutputStream out) {
        SimpleDateFormat sdf = new SimpleDateFormat(UTC_PATTERN, Locale.US);
sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
out.content = sdf.format(out.content).getBytes(Charsets.UTF_8);
out.length = ((byte[]) out.content).length;







