package me.seolnavy.study.web.dto;

import lombok.Getter;
import me.seolnavy.study.domain.posts.Posts;

@Getter
public class PostsResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String fileName;

    public PostsResponseDto(Posts entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
        this.fileName = entity.getFileName();
    }
}
