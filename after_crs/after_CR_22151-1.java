/*To prevent the reference to null pointer

It is possible that the referencing to "file.getParentFile().mkdirs()" is occured when getParentFile() function return null.
To prevent the referencing to null pointer, check if the return value of getParentFile() is null or not.

Change-Id:Id77a29c6d22db9923b2a350b14da2f3ff3e9c099*/




//Synthetic comment -- diff --git a/core/java/android/app/ActivityThread.java b/core/java/android/app/ActivityThread.java
old mode 100644
new mode 100755
//Synthetic comment -- index 162d9eb..9d83653

//Synthetic comment -- @@ -3238,8 +3238,11 @@
if (data.profileFile != null && !ii.handleProfiling) {
data.handlingProfiling = true;
File file = new File(data.profileFile);
                File parentFile = file.getParentFile();
                if (parentFile != null){
                    parentFile.mkdirs();
                    Debug.startMethodTracing(file.toString(), 8 * 1024 * 1024);
                }
}

try {







