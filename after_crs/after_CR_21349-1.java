/*Lazy initialization must be synchronized to avoid parallel instances cretation.

Change-Id:I9c6887c714f42534a465c266689dc03ee7298900*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CatService.java b/telephony/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index 1e23e34..cf37df6 100644

//Synthetic comment -- @@ -513,7 +513,7 @@
* @param ic Icc card
* @return The only Service object in the system
*/
    public static synchronized CatService getInstance(CommandsInterface ci, IccRecords ir,
Context context, IccFileHandler fh, IccCard ic) {
if (sInstance == null) {
if (ci == null || ir == null || context == null || fh == null







