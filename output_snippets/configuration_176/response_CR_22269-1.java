<<Beginning of snippet n. 0>>
public class PeriodicSync {
    private long period;
    private Bundle extras;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PeriodicSync)) return false;
        PeriodicSync other = (PeriodicSync) o;
        return period == other.period && SyncStorageEngine.equals(extras, other.extras);
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(period);
        result = 31 * result + (extras != null ? extras.hashCode() : 0);
        return result;
    }
}
//<End of snippet n. 0>>