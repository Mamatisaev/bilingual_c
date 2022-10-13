package com.example.bilingual_c.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "options")
public class Option {

    @Id
    @GeneratedValue(generator = "option_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "option_generator", sequenceName = "option_id_sequence", allocationSize = 1)
    private Long id;

    private String option;

    private Boolean isTrue;

    @ManyToOne(cascade = {MERGE, DETACH, PERSIST, REFRESH})
    private Question question;
    @ManyToOne(cascade = CascadeType.ALL)
    private ClientsAnswer clientsAnswer;
}
