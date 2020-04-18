package me.seolnavy.study.web.dto;

import lombok.*;
import me.seolnavy.study.domain.posts.Posts;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PostsSaveRequestDto {
    private MultipartFile picture;
    private String title;
    private String content;
    private String author;

    public PostsSaveRequestDto(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    @Builder
    public PostsSaveRequestDto(String title, String content, String author, MultipartFile picture) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.picture = picture;
    }

    public Posts toEntity() {
        return Posts.builder()
                    .title(title)
                    .content(content)
                    .author(author)
                    .fileName(picture != null ? picture.getOriginalFilename() : null)
                    .build();
    }
}
