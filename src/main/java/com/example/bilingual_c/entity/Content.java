package com.example.bilingual_c.entity;

import com.example.bilingual_c.entity.enums.ContentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "contents")
@Getter
@Setter
@NoArgsConstructor
public class Content {

    @Id
    @GeneratedValue(generator = "content_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "content_generator", sequenceName = "content_id_sequence", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    private String content;
}