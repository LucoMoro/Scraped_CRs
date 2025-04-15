/*Improved conversion accuracy of exif geotag data

Changed degrees and minutes of geotag data into double to avoid
data loss during cast.

Also improved error handling if geotag data can't be parsed.

Change-Id:I864843c7fc699fe81e6acba801fe46d10a01925b*/




//Synthetic comment -- diff --git a/media/java/android/media/ExifInterface.java b/media/java/android/media/ExifInterface.java
//Synthetic comment -- index 74488c5..a08f388 100644

//Synthetic comment -- @@ -293,12 +293,16 @@
String lngRef = mAttributes.get(ExifInterface.TAG_GPS_LONGITUDE_REF);

if (latValue != null && latRef != null && lngValue != null && lngRef != null) {
            try {
                output[0] = convertRationalLatLonToFloat(latValue, latRef);
                output[1] = convertRationalLatLonToFloat(lngValue, lngRef);
                return true;
            } catch (IllegalArgumentException e) {
                // if values are not parseable
            }
}

        return false;
}

/**
//Synthetic comment -- @@ -367,12 +371,12 @@

String [] pair;
pair = parts[0].split("/");
            double degrees = Double.parseDouble(pair[0].trim())
                    / Double.parseDouble(pair[1].trim());

pair = parts[1].split("/");
            double minutes = Double.parseDouble(pair[0].trim())
                    / Double.parseDouble(pair[1].trim());

pair = parts[2].split("/");
double seconds = Double.parseDouble(pair[0].trim())
//Synthetic comment -- @@ -383,10 +387,12 @@
return (float) -result;
}
return (float) result;
        } catch (NumberFormatException e) {
            // Some of the nubmers are not valid
            throw new IllegalArgumentException();
        } catch (ArrayIndexOutOfBoundsException e) {
            // Some of the rational does not follow the correct format
            throw new IllegalArgumentException();
}
}








