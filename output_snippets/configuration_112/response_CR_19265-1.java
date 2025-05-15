//<Beginning of snippet n. 0>

long time = exif.getGpsDateTime();
if (time != -1) {
    values.put(Images.Media.DATE_TAKEN, time);
} else {
    long originalDateTime = exif.getOriginalDateTime(); // Assuming this method exists
    if (originalDateTime != -1) {
        values.put(Images.Media.DATE_TAKEN, originalDateTime);
    } else {
        // Handle case where neither DateTime is available
        Log.w("DateTimeWarning", "Both GPS DateTime and original DateTime are unavailable.");
    }
}

int orientation = exif.getAttributeInt(

//<End of snippet n. 0>