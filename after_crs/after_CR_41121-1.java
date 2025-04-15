/*Expose owner and group of FileEntry.

Change-Id:Ib072508c9e00f5ac227bda89b943e217292d9640*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/FileListingService.java b/ddms/libs/ddmlib/src/com/android/ddmlib/FileListingService.java
//Synthetic comment -- index 8d804d2..d731a4d 100644

//Synthetic comment -- @@ -201,6 +201,20 @@
}

/**
         * Returns the owner string of the entry, as returned by <code>ls</code>.
         */
        public String getOwner() {
            return owner;
        }

        /**
         * Returns the group owner of the entry, as returned by <code>ls</code>.
         */
        public String getGroup() {
            return group;
        }

        /**
* Returns the extra info for the entry.
* <p/>For a link, it will be a description of the link.
* <p/>For an application apk file it will be the application package as returned







