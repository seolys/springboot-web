package me.seolnavy.study.web.dto;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class HelloResponseDto {
    private String name;
    private int amount;

    @Builder
    public HelloResponseDto(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }
}
