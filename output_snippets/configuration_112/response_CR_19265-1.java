//<Beginning of snippet n. 0>

long time = exif.getGpsDateTime();
long originalDateTime = exif.getOriginalDateTime();

if (time != 0 || originalDateTime != 0) {
    if (time != 0) {
        values.put(Images.Media.DATE_TAKEN, time);
    } else if (originalDateTime != 0) {
        values.put(Images.Media.DATE_TAKEN, originalDateTime);
    }
} else {
    long currentTime = System.currentTimeMillis();
    values.put(Images.Media.DATE_TAKEN, currentTime);
    // Logging or error handling can be added here for traceability
}

int orientation = exif.getAttributeInt(

//<End of snippet n. 0>