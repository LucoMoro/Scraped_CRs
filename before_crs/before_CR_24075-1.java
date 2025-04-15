/*Sdkman2: sort Extra packages by vendor+path.

Change-Id:I208632674a58b7a95c184beda78d13cb415bd619*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ExtraPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ExtraPackage.java
//Synthetic comment -- index c142faf..5d35d3f 100755

//Synthetic comment -- @@ -526,6 +526,23 @@
return false;
}

// ---

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java
//Synthetic comment -- index baa6706..33498f3 100755

//Synthetic comment -- @@ -561,7 +561,14 @@
public int compareTo(Package other) {
int s1 = this.sortingScore();
int s2 = other.sortingScore();
        return s1 - s2;
}

/**







