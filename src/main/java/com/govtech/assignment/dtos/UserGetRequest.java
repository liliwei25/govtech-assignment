package com.govtech.assignment.dtos;

import com.govtech.assignment.enums.SortKey;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import java.io.Serializable;

@Getter
@Setter
public class UserGetRequest implements Serializable {
    @Min(0)
    private Double min = 0.0;

    @Min(0)
    private Double max = 4000.0;

    @Min(0)
    private Integer offset = 0;

    @Min(0)
    private Integer limit;

    private SortKey sort;

    public String getSortKey() {
        return sort == null ? null : sort.toString();
    }
}

