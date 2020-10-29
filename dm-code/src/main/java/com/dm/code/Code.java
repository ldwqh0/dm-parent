package com.dm.code;

import javax.persistence.*;

@Entity
@Table(name = "dm_code_")
public class Code {

    @ManyToOne
    @JoinColumn(name = "code_type_")
    private CodeType type;

    @Id
    @Column(name = "code_")
    private String code;
}
