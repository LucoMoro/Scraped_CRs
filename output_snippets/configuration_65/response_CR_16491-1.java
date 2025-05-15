//<Beginning of snippet n. 0>

public class Feature {
    private String name;
    private boolean required;
    private int presentCount;
    private int expectedCount;

    public Feature(String name, boolean required) {
        this.name = name;
        this.required = required;
        this.presentCount = 0;
        this.expectedCount = 0; // Assuming default expected count is initialized to 0
    }
    
    public void incrementPresentCount() {
        presentCount++;
    }

    public void setExpectedCount(int expectedCount) {
        this.expectedCount = expectedCount;
    }

    public int getPresentCount() {
        return presentCount;
    }

    public int getExpectedCount() {
        return expectedCount;
    }
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class FeatureTest {
    private Feature feature;

    @Before
    public void setUp() {
        feature = new Feature("TestFeature", true);
    }

    @Test
    public void testFeatureCountSync() {
        feature.setExpectedCount(5);
        for (int i = 0; i < 5; i++) {
            feature.incrementPresentCount();
        }
        assertEquals("Feature counts are not in sync!", feature.getExpectedCount(), feature.getPresentCount());
    }
}

//<End of snippet n. 1>