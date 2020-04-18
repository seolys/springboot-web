package me.seolnavy.study.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.seolnavy.study.domain.BaseTimeEntity;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Posts extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    private String fileName;

    @Builder
    public Posts(String title, String content, String author, String fileName) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.fileName = fileName;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }


}
