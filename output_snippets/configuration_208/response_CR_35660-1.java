//<Beginning of snippet n. 0>
if (!parentPreferenceGroup.hasPreference(preference)) {
    return false;
}

parentPreferenceGroup.removePreference(preference);

return !parentPreferenceGroup.hasPreference(preference);
//<End of snippet n. 0>