package org.books.data.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private int version;

    private Timestamp lastUpdated;

    private Timestamp inserted;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Timestamp getInserted() {
        return inserted;
    }

    public void setInserted(Timestamp inserted) {
        this.inserted = inserted;
    }

    @PrePersist
    public void prePersist() {
        this.inserted = new Timestamp(System.currentTimeMillis());
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpdated = new Timestamp(System.currentTimeMillis());
    }

}