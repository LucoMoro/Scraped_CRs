<<Beginning of snippet n. 0>>
boolean updatePreferenceToSpecificActivityOrRemove(PreferenceGroup parentPreferenceGroup, Preference preference) {
    if (parentPreferenceGroup.findPreference(preference.getKey()) == null) {
        return false;
    }

    parentPreferenceGroup.removePreference(preference);

    if (parentPreferenceGroup.findPreference(preference.getKey()) != null) {
        return false;
    }

    return true;
}
<<End of snippet n. 0>>