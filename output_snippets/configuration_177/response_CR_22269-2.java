//<Beginning of snippet n. 0>
@Override
public int hashCode() {
    return Objects.hash(period, extras);
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
@Override
public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
        return false;
    }
    YourClassName other = (YourClassName) obj;
    return period == other.period && SyncStorageEngine.equals(extras, other.extras);
}
//<End of snippet n. 1>