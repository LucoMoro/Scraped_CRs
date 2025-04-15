/*Open DrmFramework fix: Remove whitespace in contraints.

Remove whitespaces in the beginning and
ending of constraints value through the trim method instead
of just checking an empty string.

Change-Id:Id502904832a3576498d74e90a41e47af7e1e3a12*/




//Synthetic comment -- diff --git a/drm/java/android/drm/DrmUtils.java b/drm/java/android/drm/DrmUtils.java
//Synthetic comment -- index 4f7cb22..924cc7f 100644

//Synthetic comment -- @@ -167,11 +167,14 @@

//Fetch Value
String strValue = readMultipleBytes(constraintData, valueLength, index);

                if (strValue != null) {
                    String trimString = strValue.trim();
                    if (trimString.length() > 0) {
                        mMap.put(strKey, trimString);
                    }
}
index += valueLength;
}
}








