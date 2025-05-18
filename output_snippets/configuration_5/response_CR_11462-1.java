//<Beginning of snippet n. 0>


/**
* Returns whether or not this instance matches the given spec.
* 
* @param spec non-null; the spec in question
* @return <code>true</code> iff this instance matches
* <code>spec</code>
*/
public boolean matches(RegisterSpec spec) {
    return this.otherSpec.equalsUsingSimpleType(spec);

//<End of snippet n. 0>