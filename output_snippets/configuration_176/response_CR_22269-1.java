<<Beginning of snippet n. 0>>
public class PeriodicSync {
    private long period;
    private Bundle extras;

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
        return Objects.hash(period, extras);
    }
}
//<End of snippet n. 0>>