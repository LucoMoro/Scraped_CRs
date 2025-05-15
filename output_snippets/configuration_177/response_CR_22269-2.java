//<Beginning of snippet n. 0>
@Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof PeriodicSync)) return false;
    PeriodicSync other = (PeriodicSync) obj;
    return period == other.period
            && SyncStorageEngine.equals(extras, other.extras);
}

@Override
public int hashCode() {
    int result = 17;
    result = 31 * result + Long.hashCode(period);
    result = 31 * result + (extras != null ? extras.hashCode() : 0);
    return result;
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public static boolean equals(Bundle b1, Bundle b2) {
    if (b1.size() != b2.size()) {
        return false;
    }
//<End of snippet n. 1>