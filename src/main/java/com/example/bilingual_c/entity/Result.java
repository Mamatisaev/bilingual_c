package com.example.bilingual_c.entity;

import com.example.bilingual_c.entity.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "results")
public class Result {

    @Id
    @GeneratedValue(generator = "result_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "result_generator", sequenceName = "result_id_sequence", allocationSize = 1)
    private Long id;

    private LocalDateTime dateOfSubmission;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Integer finalScore;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "result")
    private List<ClientsAnswer> clientsAnswers;

    @ManyToOne(cascade = {DETACH, PERSIST, MERGE, REFRESH})
    private Client client;

    @ManyToOne(cascade = {MERGE, DETACH, PERSIST, REFRESH})
    private Test test;
}