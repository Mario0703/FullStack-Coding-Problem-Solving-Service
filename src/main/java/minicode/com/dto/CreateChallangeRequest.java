package minicode.com.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import minicode.com.entity.Catagory;
import minicode.com.entity.Difficulty;

import java.util.UUID;

public class CreateChallangeRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;
    @NotBlank(message = "Description is required")
    @Size(min = 3, max = 500, message = "Description must be between 3 and 500 characters")
    private String description;

    @NotBlank(message = "Difficulty is required")
    private Difficulty difficulty;
    @NotBlank(message = "Catagory is required")
    private Catagory catagory;
}
