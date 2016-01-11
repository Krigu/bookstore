package org.books.data.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
        @NamedQuery(name = SequenceGenerator.FIND_BY_SEQUENCE_NAME, query = "select s from SequenceGenerator s where s.sequenceName = :sequenceName")
})
public class SequenceGenerator extends BaseEntity {

    public static final String FIND_BY_SEQUENCE_NAME = "SequenceGenerator.findBySequenceName";

    @Column(nullable = false, unique = true)
    private String sequenceName;

    @Column(nullable = false)
    private Long counter;

    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public Long getCounter() {
        return counter;
    }

    public void setCounter(Long counter) {
        this.counter = counter;
    }
}
