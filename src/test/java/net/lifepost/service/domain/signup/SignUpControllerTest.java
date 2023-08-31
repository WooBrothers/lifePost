package net.lifepost.service.domain.signup;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class SignUpControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testPost() throws Exception {
        String requestJson = "{\"name\" : \"김경우\",\"email\" : \"wookim789@gmail.com\",\"password\" : \"1234\",\"nickName\" : \"wookim\",\"phone\" : \"01090422934\",\"marketingSms\" : \"true\",\"marketingEmail\" : \"true\",\"marketingKakao\" : \"true\"}";

//        HttpEntity<String> requestEntity = new HttpEntity<>(requestJson);
//        ResponseEntity<String> response = restTemplate
//            .postForEntity("/sign-up", requestEntity, String.class);
    }
}