<<Beginning of snippet n. 0>>
public class ResourceReference {
    private String id;
    private String name;
    private String type;

    public ResourceReference(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return "ResourceReference{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

//<End of snippet n. 0>>