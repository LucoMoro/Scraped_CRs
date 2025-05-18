//<Beginning of snippet n. 0>
Long time = exif.getGpsDateTime();
if (time != null) {
    values.put(Images.Media.DATE_TAKEN, time);
    Log.i("ExifData", "Using GPS DateTime: " + time);
} else {
    Long originalTime = exif.getOriginalDateTime();
    if (originalTime != null) {
        values.put(Images.Media.DATE_TAKEN, originalTime);
        Log.i("ExifData", "Using Original DateTime: " + originalTime);
    } else {
        Log.e("ExifData", "Error: Both GPS and Original DateTime are unavailable.");
        // Implement fallback strategy or logging mechanism instead of throwing an exception
    }
}

int orientation = exif.getAttributeInt(
//<End of snippet n. 0>