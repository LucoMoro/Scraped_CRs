//<Beginning of snippet n. 0>
@Override
public int hashCode() {
    return Objects.hash(period, extras);
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
return id;
}

public static boolean equals(Bundle b1, Bundle b2) {
    if (b1.size() != b2.size()) {
        return false;
    }
//<End of snippet n. 1>