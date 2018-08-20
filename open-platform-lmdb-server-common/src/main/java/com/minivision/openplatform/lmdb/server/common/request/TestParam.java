package com.minivision.openplatform.lmdb.server.common.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
public class TestParam {

    @NotNull(message = "id mast not be null or empty")
    private String id;

    @NotNull(message = "name mast not be null or empty")
    private String name;

    @Range(min = 18L,max = 24L,message = "the age range is 18-24")
    private int age;

}
