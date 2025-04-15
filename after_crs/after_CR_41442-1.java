/*Add a few more calls to BlockGuardOs

While replacing frameworks/base calls with direct calls to
Libcore.os.stat, I noticed that it started failing tests that check that
the StrictMode violations are triggered.

Adding the missing file-based calls to BlockGuardOs restores the tests
to working and will help developers catch StrictMode violations in the
future.

Change-Id:Iefaac96591481f15ce9eaa9eb1ef27edc732d8fe*/




//Synthetic comment -- diff --git a/luni/src/main/java/libcore/io/BlockGuardOs.java b/luni/src/main/java/libcore/io/BlockGuardOs.java
//Synthetic comment -- index 61c9765..ece29d8 100644

//Synthetic comment -- @@ -195,4 +195,88 @@
BlockGuard.getThreadPolicy().onWriteToDisk();
return os.writev(fd, buffers, offsets, byteCounts);
}

    @Override
    public void chmod(String path, int mode) throws ErrnoException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        os.chmod(path, mode);
    }

    @Override
    public void chown(String path, int uid, int gid) throws ErrnoException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        os.chown(path, uid, gid);
    }

    @Override
    public void fchmod(FileDescriptor fd, int mode) throws ErrnoException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        os.fchmod(fd, mode);
    }

    @Override
    public void fchown(FileDescriptor fd, int uid, int gid) throws ErrnoException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        os.fchown(fd, uid, gid);
    }

    @Override
    public StructStat fstat(FileDescriptor fd) throws ErrnoException {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        return os.fstat(fd);
    }

    @Override
    public StructStatFs fstatfs(FileDescriptor fd) throws ErrnoException {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        return os.fstatfs(fd);
    }

    @Override
    public void lchown(String path, int uid, int gid) throws ErrnoException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        os.lchown(path, uid, gid);
    }

    @Override
    public long lseek(FileDescriptor fd, long offset, int whence) throws ErrnoException {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        return os.lseek(fd, offset, whence);
    }

    @Override
    public StructStat lstat(String path) throws ErrnoException {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        return os.lstat(path);
    }

    @Override
    public void mkdir(String path, int mode) throws ErrnoException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        os.mkdir(path, mode);
    }

    @Override
    public void rename(String oldPath, String newPath) throws ErrnoException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        os.rename(oldPath, newPath);
    }

    @Override
    public StructStat stat(String path) throws ErrnoException {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        return os.stat(path);
    }

    @Override
    public StructStatFs statfs(String path) throws ErrnoException {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        return os.statfs(path);
    }

    @Override
    public void symlink(String oldPath, String newPath) throws ErrnoException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        os.symlink(oldPath, newPath);
    }
}







