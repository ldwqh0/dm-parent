package com.dm.code;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "dm_code_")
public class Code {

    public Code() {
    }

    public Code(CodeType type, String code) {
        this.type = type;
        this.code = code;
    }

    @ManyToOne
    @JoinColumn(name = "code_type_")
    private CodeType type;

    @Id
    @Column(name = "code_")
    private String code;

    public CodeType getType() {
        return type;
    }

    public void setType(CodeType type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Code code1 = (Code) o;
        return Objects.equals(type, code1.type) && Objects.equals(code, code1.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, code);
    }

    @Override
    public String toString() {
        return "Code{" +
            "type=" + type +
            ", code='" + code + '\'' +
            '}';
    }

}
