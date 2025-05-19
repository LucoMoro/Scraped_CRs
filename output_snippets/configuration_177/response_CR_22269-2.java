//<Beginning of snippet n. 0>
@Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    PeriodicSync other = (PeriodicSync) obj;
    return period == other.period && SyncStorageEngine.equals(extras, other.extras);
}

@Override
public int hashCode() {
    int result = Integer.hashCode(period);
    result = 31 * result + (extras != null ? extras.hashCode() : 0);
    return result;
}
//<End of snippet n. 0>