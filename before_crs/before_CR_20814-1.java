/*The problem is that attached files(ex. Image, Movie) on MMS are not displayed among other devices.

- Symptom
	1. Attached Movie, Image files are not displayed
	2. Attached MP4 file is not displayed : NexusS to NexusOne
	3. Attached 3GP files can't be played : NexusS to A877/A797
	4. Attached JPG file is not shown : NexusS to A877
	5. Attached JPG file is not shown : Iphone to NexusS

Change-Id:I443bbcca6b6a678cd816a3d0c2bf72f544d8fd56Signed-off-by: Sang-Jun Park <sj2202.park@samsung.com>*/
//Synthetic comment -- diff --git a/src/com/android/mms/model/ImageModel.java b/src/com/android/mms/model/ImageModel.java
//Synthetic comment -- index d241de9..a751643 100644

//Synthetic comment -- @@ -212,6 +212,14 @@
throw new ExceedMessageSizeException("Not enough memory to turn image into part: " +
getUri());
}
PduPersister persister = PduPersister.getPduPersister(mContext);
this.mSize = part.getData().length;
Uri newUri = persister.persistPart(part, messageId);








//Synthetic comment -- diff --git a/src/com/android/mms/ui/UriImage.java b/src/com/android/mms/ui/UriImage.java
//Synthetic comment -- index fae3acf..3f391a4 100644

//Synthetic comment -- @@ -67,6 +67,10 @@

mSrc = mPath.substring(mPath.lastIndexOf('/') + 1);

// Some MMSCs appear to have problems with filenames
// containing a space.  So just replace them with
// underscores in the name, which is typically not
//Synthetic comment -- @@ -185,13 +189,6 @@

part.setData(data);
part.setContentType(getContentType().getBytes());
        String src = getSrc();
        byte[] srcBytes = src.getBytes();
        part.setContentLocation(srcBytes);
        part.setFilename(srcBytes);
        int period = src.lastIndexOf(".");
        byte[] contentId = period != -1 ? src.substring(0, period).getBytes() : srcBytes;
        part.setContentId(contentId);

return part;
}







