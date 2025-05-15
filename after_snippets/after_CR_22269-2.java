
//<Beginning of snippet n. 0>


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

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


return id;
}

    static boolean equals(Bundle b1, Bundle b2) {
if (b1.size() != b2.size()) {
return false;
}

//<End of snippet n. 1>








