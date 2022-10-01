package com.pet.parser.model;


import javax.persistence.*;

@Entity
@Table(name = "data")
public class DataEntity {
    private Long id;
    private byte [] contents;

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "contents")
    public byte [] getContents() {
        return contents;
    }
    public void setContents(byte [] contents) {
        this.contents = contents;
    }


    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return
                "id=" + id +
                ", contents='" + new String(contents);
    }
}
