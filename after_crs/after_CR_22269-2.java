/*Adding proper hashCode() implementation for PeriodicSync class.

This class overrides equals(Object), but does not
override hashCode(), and inherits the implementation
of hashCode() from java.lang.Object. Therefore, the
class is very likely to violate the invariant that
equal objects must have equal hashcodes.

This patch adds a propert hashCode() method implementaion
compliant with general contract of equals() and hashCode()
methods and compliant with weak definition of equality of
two Bundle objects implemented in SyncStorageEnging.equals(Bundle, Bundle).

Change-Id:I6f17daf95bed785ed36c4afaed53ecbd508ea434*/




//Synthetic comment -- diff --git a/core/java/android/content/PeriodicSync.java b/core/java/android/content/PeriodicSync.java
//Synthetic comment -- index 17813ec..0006257 100644

//Synthetic comment -- @@ -81,4 +81,31 @@
&& period == other.period
&& SyncStorageEngine.equals(extras, other.extras);
}

    @Override
    public int hashCode() {
        int result = account != null ? account.hashCode() : 0;
        result = 31 * result + (authority != null ? authority.hashCode() : 0);
        result = 31 * result + getExtrasHashCode(extras); // Why? See method documentation.
        result = 31 * result + (int) (period ^ (period >>> 32));
        return result;
    }

    /**
     * Computes weak hash code for extras, which is consistent with
     * {@code SyncStorageEngine.equals(Bundle, Bundle)}. Ordinary
     * hash code taken from Bundle class cannot be used here, because
     * it uses the default implementation from  {@code java.lang.Object},
     * but here - in {@code PeriodicSync} and {@code SyncStorage} classes -
     * equality of bundles is defined a little bit weaker.
     *
     * @param extras object for hash code evaluation.
     * @return hash code-like value for given extras, or 0 if {@code extras} is {@code null}.
     */
    private static int getExtrasHashCode(final Bundle extras) {
        if (extras == null) {
            return 0;
        }
        return extras.size();
    }
}








//Synthetic comment -- diff --git a/core/java/android/content/SyncStorageEngine.java b/core/java/android/content/SyncStorageEngine.java
//Synthetic comment -- index faf1365..bfff75e 100644

//Synthetic comment -- @@ -991,7 +991,7 @@
return id;
}

    static boolean equals(Bundle b1, Bundle b2) {
if (b1.size() != b2.size()) {
return false;
}







