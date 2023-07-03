package com.springboot.firstrestapi.myfirstrestapi.survey;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = SurveyResource.class)
@AutoConfigureMockMvc(addFilters = false)
public class SurveyResourceTest {

    @MockBean
    private SurveyService surveyService;

    @Autowired
    private MockMvc mockMvc;

    private static String SPECIFIC_QUESTION_URL = "http://localhost:8081/surveys/Survey1/questions/Question1";
    private static String GENERIC_QUESTION_URL = "http://localhost:8081/surveys/Survey1/questions/";

    @Test
    void retrieveSpecificSurveyQuestion_404Scenario() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(SPECIFIC_QUESTION_URL).accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        System.out.println(mvcResult.getResponse().getStatus());
    }

    @Test
    void retrieveSpecificSurveyQuestion_basicScenario() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(SPECIFIC_QUESTION_URL).accept(MediaType.APPLICATION_JSON);
        Question question = new Question("Question1", "Most popular cloud platform today",
                Arrays.asList("AWS", "Azure", "Google cloud", "Oracle cloud"), "AWS");
        when(surveyService.retrieveSpecificSurveyQuestion("Survey1", "Question1")).thenReturn(question);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String expectedResponse = """
                    {
                        "id":"Question1",
                        "description":"Most popular cloud platform today",
                        "options":["AWS","Azure","Google cloud","Oracle cloud"],
                        "correctAnswer":"AWS"
                    }
                """;
        assertEquals(200, mvcResult.getResponse().getStatus());
        JSONAssert.assertEquals(expectedResponse, mvcResult.getResponse().getContentAsString(), false);
    }

    @Test
    void retrieveAllSurveyQuestion_basicScenario() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(GENERIC_QUESTION_URL)
                .accept(MediaType.APPLICATION_JSON);

        List<Question> questions = new ArrayList<>();
        Question question1 = new Question("Question1", "Most popular cloud platform today",
                Arrays.asList("AWS", "Azure", "Google cloud", "Oracle cloud"), "AWS");
        questions.add(question1);

        when(surveyService.retrieveAllSurveyQuestions("Survey1")).thenReturn(questions);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String expectedResponse = """
                 [
                     {
                        "id":"Question1",
                        "description":"Most popular cloud platform today",
                        "options":["AWS","Azure","Google cloud","Oracle cloud"],
                        "correctAnswer":"AWS"
                    }
                 ]
                """;
        assertEquals(200, mvcResult.getResponse().getStatus());
        JSONAssert.assertEquals(expectedResponse, mvcResult.getResponse().getContentAsString(), false);
    }

    @Test
    void addNewSurveyQuestion_basicScenario() throws Exception {
        String requestBody = """
                {
                    "description": "Your favourite programming language",
                    "options": [
                        "Java",
                        "Python",
                        "JavaScript",
                        "C#"
                    ],
                    "correctAnswer": "Java"
                }
            """;
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(GENERIC_QUESTION_URL)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);

        when(surveyService.addNewSurveyQuestion(anyString(),any())).thenReturn("SOME_ID");
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String locationHeader = mvcResult.getResponse().getHeader("Location");
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertTrue(locationHeader.contains("/surveys/Survey1/questions/SOME_ID"));

    }
}
