//<Beginning of snippet n. 0>
if (!parentPreferenceGroup.hasPreference(preference)) {
    return false;
}

boolean removed = parentPreferenceGroup.removePreference(preference);
return removed;
//<End of snippet n. 0>