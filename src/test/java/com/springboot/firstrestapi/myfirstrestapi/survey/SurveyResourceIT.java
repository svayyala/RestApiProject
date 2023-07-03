package com.springboot.firstrestapi.myfirstrestapi.survey;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SurveyResourceIT {

    private static String SPECIFIC_QUESTION_URL = "/surveys/Survey1/questions/Question1";

    private static String GENERIC_QUESTION_URL = "/surveys/Survey1/questions/";

    private static String GENERIC_SURVEY_URL = "/surveys/";

    private static String SPECIFIC_SURVEY_URL = "/surveys/Survey1/";

    @Autowired
    private TestRestTemplate template;

    @Test
    void retrieveSpecificSurveyQuestion_basicScenario() throws JSONException {
        HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(null,headers);
        ResponseEntity<String> responseEntity = template.exchange(SPECIFIC_QUESTION_URL, HttpMethod.GET, httpEntity, String.class);
        String expectedResponse = """
                {
                    "id":"Question1",
                    "description":"Most popular cloud platform today",
                    "correctAnswer":"AWS"
                }
                """;
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));
        JSONAssert.assertEquals(expectedResponse,responseEntity.getBody(),false);

    }

    @Test
    void retrieveAllSurveyQuestion_basicScenario() throws JSONException {
        HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(null,headers);
        ResponseEntity<String> responseEntity = template.exchange(GENERIC_QUESTION_URL, HttpMethod.GET, httpEntity, String.class);

        String expectedResponse = """
                [
                    {
                        "id": "Question1"
                    },
                    {
                        "id": "Question2"
                    },
                    {
                        "id": "Question3"
                    }
                ]
                """;
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));
        JSONAssert.assertEquals(expectedResponse,responseEntity.getBody(),false);

    }

    @Test
    void retrieveAllSurveys_basicScenario() throws JSONException {
        HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(null,headers);
        ResponseEntity<String> responseEntity = template.exchange(GENERIC_SURVEY_URL, HttpMethod.GET, httpEntity, String.class);

        String expectedResponse = """
                [
                     {
                         "id": "Survey1"
                     }
                 ]
                """;
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));
        JSONAssert.assertEquals(expectedResponse,responseEntity.getBody(),false);

    }

    @Test
    void retrieveSpecificSurveys_basicScenario() throws JSONException {
        HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(null,headers);
        ResponseEntity<String> responseEntity = template.exchange(SPECIFIC_SURVEY_URL, HttpMethod.GET, httpEntity, String.class);

        String expectedResponse = """
                {
                         "id": "Survey1"
                }
                """;
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));
        JSONAssert.assertEquals(expectedResponse,responseEntity.getBody(),false);

    }

//    @Test
//    void addNewSurveyQuestion_basicScenario(){
//        String requestBody = """
//                {
//                    "description": "Your favourite programming language",
//                    "options": [
//                        "Java",
//                        "Python",
//                        "JavaScript",
//                        "C#"
//                    ],
//                    "correctAnswer": "Java"
//                }
//            """;
//        HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
//        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody,headers);
//        ResponseEntity<String> responseEntity = template.exchange(GENERIC_QUESTION_URL, HttpMethod.POST, httpEntity, String.class);
//        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
//        String locationHeader = responseEntity.getHeaders().get("Location").get(0);
//        assertTrue(locationHeader.contains("/surveys/Survey1/questions/"));
//        ResponseEntity<String> responseEntityDelete = template.exchange(locationHeader, HttpMethod.DELETE, httpEntity, String.class);
//        assertTrue(responseEntityDelete.getStatusCode().is2xxSuccessful());
//     //   template.delete(locationHeader);
//    }

    private HttpHeaders createHttpContentTypeAndAuthorizationHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Basic " + performBasicAuthEncoding("admin", "password"));
        return headers;
    }

    String performBasicAuthEncoding(String user, String password){
        String combined = user + ":" + password;
        byte[] encodedBytes = Base64.getEncoder().encode(combined.getBytes());
        return new String(encodedBytes);
    }
}
