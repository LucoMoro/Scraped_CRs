//<Beginning of snippet n. 0>
public class ResourceReference {
    private String resourceName;
    private String resourceType;
    private String resourceId;

    public ResourceReference(String resourceName, String resourceType, String resourceId) {
        this.resourceName = resourceName;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ResourceReference { ")
          .append("resourceName='").append(resourceName != null ? resourceName : "null").append("', ")
          .append("resourceType='").append(resourceType != null ? resourceType : "null").append("', ")
          .append("resourceId='").append(resourceId != null ? resourceId : "null").append("' ")
          .append("}");
        return sb.toString();
    }

    // Additional methods...
}
//<End of snippet n. 0>