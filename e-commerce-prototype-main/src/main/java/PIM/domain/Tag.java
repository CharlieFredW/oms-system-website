package PIM.domain;

import java.util.UUID;

public class Tag {
    private String tag_id;
    private String name;

    // Constructor for creating a brand-new Tag to add to the database
    public Tag(String name) {
        this.tag_id = String.valueOf(UUID.randomUUID());
        this.name = name.trim().toLowerCase();
    }

    // Constructor for fetching Tag from the database
    public Tag(String tag_id, String name) {
        this.tag_id = tag_id;
        this.name = name.trim().toLowerCase();
    }

    public String getTag_id() {
        return tag_id;
    }

    public String getName() {
        return name;
    }
}
