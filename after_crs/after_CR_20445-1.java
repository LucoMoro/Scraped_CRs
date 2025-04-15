/*Improved conversion accuracy of exif geotag data

Changed degrees and minutes of geotag data into double to avoid
data loss during cast.

Also improved error handling if geotag data can't be parsed.

Change-Id:I864843c7fc699fe81e6acba801fe46d10a01925b*/




//Synthetic comment -- diff --git a/media/java/android/media/ExifInterface.java b/media/java/android/media/ExifInterface.java
//Synthetic comment -- index 74488c5..8a79040 100644

//Synthetic comment -- @@ -293,12 +293,17 @@
String lngRef = mAttributes.get(ExifInterface.TAG_GPS_LONGITUDE_REF);

if (latValue != null && latRef != null && lngValue != null && lngRef != null) {
            try {
                output[0] = convertRationalLatLonToFloat(latValue, latRef);
                output[1] = convertRationalLatLonToFloat(lngValue, lngRef);
                return true;
            } catch (RuntimeException ex) {
                // if for whatever reason we can't parse the lat long then return
                // false
            }
}

        return false;
}

/**
//Synthetic comment -- @@ -361,33 +366,27 @@
}

private static float convertRationalLatLonToFloat(
            String rationalString, String ref) throws RuntimeException {
        String [] parts = rationalString.split(",");

        String [] pair;
        pair = parts[0].split("/");
        double degrees = Double.parseDouble(pair[0].trim())
                / Double.parseDouble(pair[1].trim());

        pair = parts[1].split("/");
        double minutes = Double.parseDouble(pair[0].trim())
                / Double.parseDouble(pair[1].trim());

        pair = parts[2].split("/");
        double seconds = Double.parseDouble(pair[0].trim())
                / Double.parseDouble(pair[1].trim());

        double result = degrees + (minutes / 60.0) + (seconds / 3600.0);
        if ((ref.equals("S") || ref.equals("W"))) {
            return (float) -result;
}
        return (float) result;
}

private native boolean appendThumbnailNative(String fileName,







