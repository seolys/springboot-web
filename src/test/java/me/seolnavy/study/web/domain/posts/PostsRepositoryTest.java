package me.seolnavy.study.web.domain.posts;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @After
    public void cleanup() {
//        postsRepository.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기() {
        //given
        String title = "테스트 게시글";
        String content = "테스트 본문";
        Posts posts = Posts.builder()
                            .title(title)
                            .content(content)
                            .author("seolnavy@naver.com")
                            .build();
        Posts save = postsRepository.save(posts);

        //when
        Posts findPosts = postsRepository.findById(save.getId()).orElse(new Posts());

        //then
        assertThat(findPosts.getTitle()).isEqualTo(title);
        assertThat(findPosts.getContent()).isEqualTo(content);
    }
    
    @Test
    public void BaseTimeEntity_등록() {
        //given
        LocalDateTime now = LocalDateTime.of(2020,4,16,0,0,0);
        Posts posts = Posts.builder()
                            .title("title")
                            .content("content")
                            .author("author")
                            .build();
        Posts savePost = postsRepository.save(posts);

        //when
        Posts findPost = postsRepository.findById(savePost.getId()).orElse(new Posts());

        //then
        System.out.println(String.format("createDate=[%s], modifiedDate=[%s]", findPost.getCreatedDate(), findPost.getModifiedDate()));

        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
    }

}