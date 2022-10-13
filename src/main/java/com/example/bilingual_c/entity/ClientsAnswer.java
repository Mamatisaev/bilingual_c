package com.example.bilingual_c.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "clients_answers")
@Getter
@Setter
@NoArgsConstructor
public class ClientsAnswer {

    @Id
    @GeneratedValue(generator = "clients_answer_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "clientS_answer_generator", sequenceName = "clients_answer_id_sequence", allocationSize = 1)
    private Long id;

    private Integer numberOfWords;

    private Integer score;

    @OneToOne(cascade = CascadeType.ALL)
    private Content content;

    @ManyToOne(cascade = {MERGE, DETACH, PERSIST, REFRESH})
    private Question question;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clientsAnswer")
    private List<Option> optionsAnswer;

    @ManyToOne(cascade = CascadeType.ALL)
    private Result result;
}
