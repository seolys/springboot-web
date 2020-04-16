package me.seolnavy.study.web;

import me.seolnavy.study.web.domain.posts.Posts;
import me.seolnavy.study.web.domain.posts.PostsRepository;
import me.seolnavy.study.web.dto.PostsSaveRequestDto;
import me.seolnavy.study.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @After
    public void tearDown() throws Exception {
//        postsRepository.deleteAll();
    }

    @Test
    public void Posts_등록된다() throws Exception {
        //given
        String title = "title";
        String content = "content";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                                                            .title(title)
                                                            .content(content)
                                                            .author("author")
                                                            .build();
        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

//        List<Posts> all = postsRepository.findAll();
//        Posts posts = all.get(0);
        Posts posts = postsRepository.findById(responseEntity.getBody()).orElse(new Posts());
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }

    @Test
    public void Posts_수정된다() throws Exception {
        //given
        Posts posts = Posts.builder()
                            .title("title")
                            .content("content")
                            .author("author")
                            .build();
        Posts savedPosts = postsRepository.save(posts);

        Long updateId = savedPosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                                                                .title(expectedTitle)
                                                                .content(expectedContent)
                                                                .build();
        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;
        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        //when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);
        Posts findPosts = postsRepository.findById(updateId).orElse(new Posts());
        assertThat(findPosts.getTitle()).isEqualTo(expectedTitle);
        assertThat(findPosts.getContent()).isEqualTo(expectedContent);
    }

    @Test
    public void Posts를_삭제한다() {
        //given
        Posts posts = Posts.builder()
                            .title("title")
                            .content("content")
                            .author("author")
                            .build();
        Posts savePosts = postsRepository.save(posts);
        Long deleteId = savePosts.getId();
        System.out.println("deleteId = " + deleteId);

        //when
        String url = "http://localhost:" + port + "/api/v1/posts/" + deleteId;
        HttpEntity<Long> httpEntity = new HttpEntity<>(deleteId);
        restTemplate.delete(url, deleteId);

        //then
        Posts findPosts = postsRepository.findById(deleteId).orElse(null);
        System.out.println("findPosts = " + findPosts);
        assertThat(findPosts).isNull();
    }

}