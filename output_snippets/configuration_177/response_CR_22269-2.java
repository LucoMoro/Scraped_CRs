<<Beginning of snippet n. 0>>
public class PeriodicSync {
    private final long period;
    private final Bundle extras;

    public PeriodicSync(long period, Bundle extras) {
        this.period = period;
        this.extras = extras;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PeriodicSync)) return false;
        PeriodicSync other = (PeriodicSync) obj;
        return period == other.period && SyncStorageEngine.equals(extras, other.extras);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(period, extras);
    }
}
<<End of snippet n. 0>>

<<Beginning of snippet n. 1>>
public static boolean equals(Bundle b1, Bundle b2) {
    if (b1 == null || b2 == null) return false;
    if (b1.size() != b2.size()) {
        return false;
    }
    // Additional equality checks can be implemented here.
    return true; // Placeholder for actual equality implementation.
}
<<End of snippet n. 1>>