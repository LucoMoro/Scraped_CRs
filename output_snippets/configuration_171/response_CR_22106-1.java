```
<<Beginning of snippet n. 0>>
public class ResourceReference {
    private String resourceId;
    private String resourceType;

    public ResourceReference(String resourceId, String resourceType) {
        this.resourceId = resourceId;
        this.resourceType = resourceType;
    }

    @Override
    public String toString() {
        return "ResourceReference{" +
                "resourceId='" + resourceId + '\'' +
                ", resourceType='" + resourceType + '\'' +
                '}';
    }

    // Additional methods can be added here, if necessary
}

// Unit Test for toString Method
import org.junit.Test;
import static org.junit.Assert.*;

public class ResourceReferenceTest {
    @Test
    public void testToString() {
        ResourceReference reference = new ResourceReference("123", "TypeA");
        assertEquals("ResourceReference{resourceId='123', resourceType='TypeA'}", reference.toString());
    }
}
<<End of snippet n. 0>>