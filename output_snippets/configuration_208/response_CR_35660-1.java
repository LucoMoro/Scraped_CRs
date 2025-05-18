<<Beginning of snippet n. 0>>
if (!parentPreferenceGroup.hasPreference(preference)) {
    return false;
}

boolean removed = parentPreferenceGroup.removePreference(preference);
if (!removed) {
    return false;
}
return true;
<<End of snippet n. 0>>