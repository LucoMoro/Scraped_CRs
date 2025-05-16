```
//<Beginning of snippet n. 0>


/**
 * Constructor does not include 'present' because that's a detected
 * value, and not set during creation.
 *
 * @param name value for this.name
 * @param required value for this.required
 */
public class FeatureVerifier {
    private String name;
    private boolean required;
    private int featureCount;

    public FeatureVerifier(String name, boolean required) {
        this.name = name;
        this.required = required;
    }

    public void verifyFeatures(int expectedCount) {
        if (this.featureCount != expectedCount) {
            System.out.println("Warning: Feature count out of sync. Expected: " + expectedCount + ", Actual: " + this.featureCount);
        }
    }

    public void setFeatureCount(int count) {
        this.featureCount = count;
    }

    public int getFeatureCount() {
        return this.featureCount;
    }
}

//<End of snippet n. 0>