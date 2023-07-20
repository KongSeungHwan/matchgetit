package com.matchgetit.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "INQUIRY")
@Getter @Setter @ToString
public class InquiryEntity extends BaseEntity{
    @Id
    @Column(name = "inquiry_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String content;

    private String category;
    private String state;

    @OneToMany(mappedBy = "inquiry", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<InquiryCommentEntity> comments = new ArrayList<>();

    private String writerName;

}
