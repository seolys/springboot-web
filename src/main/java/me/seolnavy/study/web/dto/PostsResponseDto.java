package me.seolnavy.study.web.dto;

import lombok.Getter;
import me.seolnavy.study.domain.posts.Posts;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class PostsResponseDto implements Serializable {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String fileName;
    private LocalDateTime modifiedDate;

    public PostsResponseDto(Posts entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
        this.fileName = entity.getFileName();
        this.modifiedDate = entity.getModifiedDate();
    }
}
