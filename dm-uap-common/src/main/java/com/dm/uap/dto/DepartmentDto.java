package com.dm.uap.dto;

import com.dm.uap.entity.Department.Types;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@JsonInclude(value = Include.NON_EMPTY)
public class DepartmentDto implements Serializable {

    public interface New {

    }

    public interface ReferenceBy {

    }

    private static final long serialVersionUID = -4966481409754529111L;

    @NotNull(groups = ReferenceBy.class)
    private Long id;

    @NotNull(groups = {New.class})
    private String fullname;

    @NotNull(groups = {New.class})
    private String shortname;

    private String description;

    @NotNull(groups = {New.class})
    private Types type;

    public DepartmentDto() {
        super();
    }

    private boolean hasChildren = false;

    private long childrenCount = 0;

    public DepartmentDto(Long id) {
        super();
        this.id = id;
    }

    @JsonIgnoreProperties({"parent", "description", "parents"})
    private DepartmentDto parent;

    private String logo;

}
