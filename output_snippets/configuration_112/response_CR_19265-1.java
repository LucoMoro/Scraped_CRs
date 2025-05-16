//<Beginning of snippet n. 0>

long time = exif.getGpsDateTime();
if (time != -1) {
    values.put(Images.Media.DATE_TAKEN, time);
} else {
    long originalDateTaken = exif.getOriginalDateTime(); // Assuming this method retrieves the original datetime
    values.put(Images.Media.DATE_TAKEN, originalDateTaken);
    Log.d("ExifHandling", "GPS datetime unavailable, using original datetime: " + originalDateTaken);
}

int orientation = exif.getAttributeInt(

//<End of snippet n. 0>