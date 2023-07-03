package com.springboot.firstrestapi.myfirstrestapi.survey;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.jupiter.api.Assertions.fail;

public class JsonAssertTest {

    @Test
    void jsonAssert_learningBasics() throws JSONException {

        String expectedResponse = """
                {
                    "id":"Question1",
                    "description":"Most popular cloud platform today",
                    "correctAnswer":"AWS"
                }
                """;

        String actualResponse =
                """
                    {
                        "id":"Question1",
                        "description":"Most popular cloud platform today",
                        "options":["AWS","Azure","Google cloud","Oracle cloud"],
                        "correctAnswer":"AWS"
                    }
                """;

        JSONAssert. assertEquals(expectedResponse,actualResponse,true);
    }
}
