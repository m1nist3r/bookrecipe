package com.portfolio.recipebook.command;

import com.portfolio.recipebook.validator.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeCommand {

    private Long id;

    @NotBlank
    @Size(max = 34)
    private String title;

    @NotBlank
    @Type(type = "text")
    @Size(max = 350)
    private String description;

    @Min(1)
    @Max(360)
    private int cookTime;

    @Image
    private MultipartFile image;

    private String difficulty;

    @NotNull
    private Long userId;
}
