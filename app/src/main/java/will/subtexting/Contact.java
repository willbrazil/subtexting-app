package will.subtexting;

/**
 * Created by william on 4/17/15.
 */
public class Contact {

    private Long localId;
    private String name;

    public Contact(Long localId, String name) {
        this.localId = localId;
        this.name = name;
    }

    public Long getLocalId() {
        return localId;
    }

    public void setLocalId(Long localId) {
        this.localId = localId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
