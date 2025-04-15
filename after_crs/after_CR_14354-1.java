/*Fixed FindBugs warnings in BrowserBackupAgent.java

This makes sure that streams are closed in corner cases.

Change-Id:I185c0dbddc09338de3a63eab22c57afb8d1a9c1a*/




//Synthetic comment -- diff --git a/src/com/android/browser/BrowserBackupAgent.java b/src/com/android/browser/BrowserBackupAgent.java
//Synthetic comment -- index 387555d..84672b1 100644

//Synthetic comment -- @@ -84,6 +84,10 @@
savedVersion = in.readInt();
} catch (EOFException e) {
// It means we had no previous state; that's fine
        } finally {
            if (in != null) {
                in.close();
            }
}

// Build a flattened representation of the bookmarks table
//Synthetic comment -- @@ -174,6 +178,10 @@
} catch (IOException ioe) {
Log.w(TAG, "Bad backup data; not restoring");
crc = -1;
                    } finally {
                        if (in != null) {
                            in.close();
                        }
}
}

//Synthetic comment -- @@ -187,7 +195,7 @@
}
}

    static class Bookmark {
public String url;
public int visits;
public long date;
//Synthetic comment -- @@ -258,13 +266,18 @@
data.writeEntityHeader(key, toCopy);

FileInputStream in = new FileInputStream(file);
        try {
            int nRead;
            while (toCopy > 0) {
                nRead = in.read(buf, 0, CHUNK);
                data.writeEntityData(buf, nRead);
                toCopy -= nRead;
            }
        } finally {
            if (in != null) {
                in.close();
            }
}
}

// Read the given file from backup to a file, calculating a CRC32 along the way
//Synthetic comment -- @@ -275,14 +288,18 @@
CRC32 crc = new CRC32();
FileOutputStream out = new FileOutputStream(file);

        try {
            while (toRead > 0) {
                int numRead = data.readEntityData(buf, 0, CHUNK);
                crc.update(buf, 0, numRead);
                out.write(buf, 0, numRead);
                toRead -= numRead;
            }
        } finally {
            if (out != null) {
                out.close();
            }
}
return crc.getValue();
}

//Synthetic comment -- @@ -291,8 +308,14 @@
throws IOException {
DataOutputStream out = new DataOutputStream(
new FileOutputStream(stateFile.getFileDescriptor()));
        try {
            out.writeLong(fileSize);
            out.writeLong(crc);
            out.writeInt(BACKUP_AGENT_VERSION);
        } finally {
            if (out != null) {
                out.close();
            }
        }
}
}







