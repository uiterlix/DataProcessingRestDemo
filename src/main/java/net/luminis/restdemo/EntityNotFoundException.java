package net.luminis.restdemo;

public class EntityNotFoundException extends Exception {
    private final String entityId;

    public EntityNotFoundException(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityId() {
        return entityId;
    }
}

