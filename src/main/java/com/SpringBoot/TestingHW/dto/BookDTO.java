package com.SpringBoot.TestingHW.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {

    private Long id;

    @NotBlank(message = "Title of book must not be Blank after trim")
    @Size(min = 3, message = "Name of the book must be at least 3 characters")
    private String title;

    @NotBlank(message = "Description of book must not be Blank after trim")
    private String description;

    private AuthorDTO authored_by;

    @PastOrPresent(message = "Book publish date should be Past or Present")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishedOn;

}
