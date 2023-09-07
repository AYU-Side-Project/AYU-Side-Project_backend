package com.example.streetfoodfinder.client;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Data

public class SendMailForm {
    private String from;
    private String to;
    private String subjects;
    private String text;
}