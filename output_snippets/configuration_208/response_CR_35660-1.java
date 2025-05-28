//<Beginning of snippet n. 0>
if (!parentPreferenceGroup.containsPreference(preference)) {
    return false;
}
boolean removed = parentPreferenceGroup.removePreference(preference);
return parentPreferenceGroup.containsPreference(preference) ? false : removed;
//<End of snippet n. 0>