<<Beginning of snippet n. 0>>
@Override
public int hashCode() {
    int result = 17;
    result = 31 * result + (period != null ? period.hashCode() : 0);
    result = 31 * result + (extras != null ? Arrays.hashCode(extras) : 0);
    return result;
}
<<End of snippet n. 0>>