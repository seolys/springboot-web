package me.seolnavy.study.web;

import me.seolnavy.study.web.dto.HelloResponseDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void hello가_리턴된다() throws Exception {
        String hello = "hello";
        String responseText = restTemplate.getForObject("/hello", String.class);
        assertThat(responseText).contains(hello);
    }

    @Test
    public void helloDto가_리턴된다() throws Exception {
        String name = "hello";
        int amount = 1000;
        URI uri = UriComponentsBuilder.newInstance()
                .path("/hello/dto")
                .queryParam("name", name)
                .queryParam("amount", String.valueOf(amount))
                .build().encode().toUri();
        HelloResponseDto dto = restTemplate.getForObject(uri, HelloResponseDto.class);
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getAmount()).isEqualTo(amount);
//        mvc.perform(get("/hello/dto")
//                        .param("name", name)
//                        .param("amount", String.valueOf(amount)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name", is(name)))
//                .andExpect(jsonPath("$.amount", is(amount)));
    }
}