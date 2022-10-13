package com.example.bilingual_c.entity;

import com.example.bilingual_c.entity.enums.OptionType;
import com.example.bilingual_c.entity.enums.QuestionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.Mergeable;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(generator = "question_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "question_generator", sequenceName = "question_id_sequence", allocationSize = 1)
    private Long id;

    private String title;

    private String statement;

    private String passage;

    private Boolean isActive;

    private Integer numberOfReplays;

    private Integer duration;

    private String shortDescription;

    private Integer minNumberOfWords;

    private String correctAnswer;

    @OneToOne(cascade = ALL)
    private Content content;

    @Enumerated(EnumType.STRING)
    private OptionType optionType;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @ManyToOne(cascade = {MERGE, DETACH, PERSIST, REFRESH})
    private Test test;

    @OneToMany(cascade = ALL, mappedBy = "question")
    private List<Option> options;
}