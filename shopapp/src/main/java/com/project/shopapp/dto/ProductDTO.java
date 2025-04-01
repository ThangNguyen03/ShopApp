package com.project.shopapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 30,message = "Title must be between 3 and 300 characters")
    private String name;

    @Min(value = 0,message = "Discount must be greater than or equal to 0")
    @Max(value = 100000000,message = "Discount must be less than or equal to 10.000.000")
    private Float price;
//
//    @Min(value = 0,message = "Discount must be greater than or equal to 0")
//    @Max(value = 100,message = "Discount must be less than or equal to 100")
//    private float discount;

    private String thumbnail;
    private String description;
    @JsonProperty("category_id")
    private Long categoryId;
//    private List<MultipartFile> files;
}
