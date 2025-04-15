/*Movie studio: The vertical picture taken in the Movie studio is horizontal in Gallery

Root cause: Movie Studio does the rotation of the image accroding to EXIF data,
but forgets to update the image in gallery after rotation, the fix is to update
the original image in the gallery after rotation.

Change-Id:I8d95877f5f091ae71b2a29bfdc746beebcae2bcdAuthor: Wangyi Gu <wangyi.gu@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 36133*/




//Synthetic comment -- diff --git a/src/com/android/videoeditor/service/ApiService.java b/src/com/android/videoeditor/service/ApiService.java
//Synthetic comment -- index f4769fe..9f63872 100755

//Synthetic comment -- @@ -1850,6 +1850,8 @@
final File outputFile = new File(projectPath,
"gallery_image_" + generateId() + ".jpg");
if (ImageUtils.transformJpeg(filename, outputFile)) {
                                        final File transfilename = new File(filename);
                                        ImageUtils.transformJpeg(filename,transfilename);
filename = outputFile.getAbsolutePath();
}
} catch (Exception ex) {







