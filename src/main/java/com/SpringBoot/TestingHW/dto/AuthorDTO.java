package com.SpringBoot.TestingHW.dto;

import com.SpringBoot.TestingHW.entity.bookEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorDTO {

    private Long id;

    @NotBlank(message = "Name of the author must not be Blank after trim")
    @Size(min = 3, message = "Name of the author must be at least 3 characters")
    private String name;

    @JsonIgnore
    private List<bookEntity> bookEntityList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorDTO authorDto = (AuthorDTO) o;
        return Objects.equals(id, authorDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
