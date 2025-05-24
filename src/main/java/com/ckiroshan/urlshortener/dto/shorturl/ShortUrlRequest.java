package com.ckiroshan.urlshortener.dto.shorturl;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ShortUrlRequest {
    @NotBlank
    @URL
    private String originalUrl;
    @Size(min = 3, max = 10)
    private String customShortCode;
}
