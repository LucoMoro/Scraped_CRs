/*Sdkman2: sort Extra packages by vendor+path.

Change-Id:I208632674a58b7a95c184beda78d13cb415bd619*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ExtraPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ExtraPackage.java
//Synthetic comment -- index c142faf..5d35d3f 100755

//Synthetic comment -- @@ -526,6 +526,23 @@
return false;
}


    /**
     * For extra packages, sort using the vendor|path name.
     */
    @Override
    public int compareTo(Package other) {
        if (other instanceof ExtraPackage) {
            ExtraPackage ep = (ExtraPackage) other;
            String s1 = this.getVendor() + '|' + this.getPath();
            String s2 = ep.getVendor()   + '|' + ep.getPath();
            return s1.compareTo(s2);
        }

        return super.compareTo(other);
    }


// ---

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java
//Synthetic comment -- index baa6706..33498f3 100755

//Synthetic comment -- @@ -561,7 +561,14 @@
public int compareTo(Package other) {
int s1 = this.sortingScore();
int s2 = other.sortingScore();

        // First sort by type.
        if (s1 != s2) {
            return s1 - s2;
        }

        // If 2 packages are of the same type, sort by short description.
        return this.getShortDescription().compareTo(other.getShortDescription());
}

/**







