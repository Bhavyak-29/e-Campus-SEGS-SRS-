// src/main/java/com/example/dto/CourseDTO.java
package com.segs.demo.model;

public class CourseDTO {
    private Long id;
    private String code;
    private String name;

    public CourseDTO() {
    }

    public CourseDTO(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }
}
