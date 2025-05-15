//<Beginning of snippet n. 0>


/**
* Returns whether or not this instance matches the given otherSpec.
* 
         * @param otherSpec non-null; the otherSpec in question
* @return <code>true</code> iff this instance matches
         * <code>otherSpec</code>
*/
public boolean matches(RegisterSpec otherSpec) {
    return this.equalsUsingSimpleType(otherSpec);

//<End of snippet n. 0>