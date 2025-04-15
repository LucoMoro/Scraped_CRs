/*AlbumArt image of songs in the non-primary storage root dir are images in the same dir

MediaProvider will fall back to the image in the same folder of music,
if MetadataRetriver failed to extract the AlbumArt image from those mp3 files.
MediaProvider will only bypass the albumArt image search if the music located
in the root director of the primary external storage. Add this bypass for all
external storages.

Change-Id:I972586b2f92bd439f51ee932f09e824fc5507236Signed-off-by: guoyin.chen <guoyin.chen@freescale.com>*/
//Synthetic comment -- diff --git a/src/com/android/providers/media/MediaProvider.java b/src/com/android/providers/media/MediaProvider.java
//Synthetic comment -- index c5d15b1..ce7e2b9 100644

//Synthetic comment -- @@ -4421,11 +4421,20 @@
msg.sendToTarget();
}

// Extract compressed image data from the audio file itself or, if that fails,
// look for a file "AlbumArt.jpg" in the containing directory.
private static byte[] getCompressedAlbumArt(Context context, String path) {
byte[] compressed = null;

try {
File f = new File(path);
ParcelFileDescriptor pfd = ParcelFileDescriptor.open(f,
//Synthetic comment -- @@ -4457,7 +4466,8 @@
synchronized (sFolderArtMap) {
if (sFolderArtMap.containsKey(artPath)) {
bestmatch = sFolderArtMap.get(artPath);
                        } else if (!artPath.equalsIgnoreCase(sdroot) &&
!artPath.equalsIgnoreCase(dwndir)) {
File dir = new File(artPath);
String [] entrynames = dir.list();







