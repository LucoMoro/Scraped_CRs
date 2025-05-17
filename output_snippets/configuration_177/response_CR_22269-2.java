//<Beginning of snippet n. 0>
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PeriodicSync)) return false;
    PeriodicSync other = (PeriodicSync) o;
    return period == other.period && SyncStorageEngine.equals(extras, other.extras);
}

@Override
public int hashCode() {
    return Objects.hash(period, extras != null ? extras.hashCode() : 0);
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
return id;
}

public static boolean equals(Bundle b1, Bundle b2) {
    if (b1 == null || b2 == null) {
        return b1 == b2;
    }
    if (b1.size() != b2.size()) {
        return false;
    }
//<End of snippet n. 1>