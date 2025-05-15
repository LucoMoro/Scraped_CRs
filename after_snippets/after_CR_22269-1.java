
//<Beginning of snippet n. 0>


&& period == other.period
&& SyncStorageEngine.equals(extras, other.extras);
}

    @Override
    public int hashCode() {
        int result = account != null ? account.hashCode() : 0;
        result = 31 * result + (authority != null ? authority.hashCode() : 0);
        result = 31 * result + (extras != null ? extras.hashCode() : 0);
        result = 31 * result + (int) (period ^ (period >>> 32));
        return result;
    }
}

//<End of snippet n. 0>








