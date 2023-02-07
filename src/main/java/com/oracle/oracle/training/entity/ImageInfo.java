package com.oracle.oracle.training.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AttributeOverrides({
        @AttributeOverride(
                name = "name",
                column = @Column(name = "image_name")
        ),
        @AttributeOverride(
                name = "type",
                column = @Column(name = "image_type")
        )}
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class ImageInfo {
    private String name;
    private String type;
    @Lob
    @Column(name = "imagedata", length = 1000)
    private byte[] imageData;
}
