package com.springboot.firstrestapi.myfirstrestapi.survey;

import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class SurveyService {

    private static List<Survey> surveys = new ArrayList<>();

    static {
        Question question1 = new Question("Question1", "Most popular cloud platform today",
                Arrays.asList("AWS", "Azure", "Google cloud", "Oracle cloud"), "AWS");

        Question question2 = new Question("Question2", "Fast growing cloud platform",
                Arrays.asList("AWS", "Azure", "Google cloud", "Oracle cloud"), "Google cloud");

        Question question3 = new Question("Question3", "Most popular devops tool",
                Arrays.asList("kubernetes", "Docker", "Terraform", "Azure Devops"), "Kubernetes");

        List<Question> questions = new ArrayList<>(Arrays.asList(question1, question2, question3));

        Survey survey = new Survey("Survey1", "My favorite survey", "Description of the Survey", questions);

        surveys.add(survey);
    }

    public List<Survey> retrieveAllSurveys() {
        return surveys;
    }

    public Survey retrieveSurveyById(String surveyId) {
        Predicate<? super Survey> predicate = survey -> survey.getId().equalsIgnoreCase(surveyId);
        Optional<Survey> optionalSurvey = surveys.stream().filter(predicate).findFirst();
        if (optionalSurvey.isEmpty()) return null;
        return optionalSurvey.get();
    }

    public List<Question> retrieveAllSurveyQuestions(String surveyId) {
        Survey survey = retrieveSurveyById(surveyId);
        if (survey == null) return null;
        return survey.getQuestions();
    }

    public Question retrieveSpecificSurveyQuestion(String surveyId, String questionId) {
        List<Question> questions = retrieveAllSurveyQuestions(surveyId);
        if (questions == null) return null;
        Optional<Question> optional = questions.stream().filter(i -> i.getId().equalsIgnoreCase(questionId)).findFirst();
        if (optional.isEmpty()) return null;
        return optional.get();
    }

    public String addNewSurveyQuestion(String surveyId, Question question) {
        List<Question> questions = retrieveAllSurveyQuestions(surveyId);
        question.setId(generateRandomId());
        questions.add(question);
        return question.getId();
    }

    private String generateRandomId(){
        SecureRandom secureRandom = new SecureRandom();
        String randomId = new BigInteger(32, secureRandom).toString();
        return  randomId;
    }

    public String deleteSurveyQuestion(String surveyId, String questionId) {
        List<Question> questions = retrieveAllSurveyQuestions(surveyId);
        if (questions == null) return null;
        Predicate<Question> predicate = (i -> i.getId().equalsIgnoreCase(questionId));
        boolean removed = questions.removeIf(predicate);
        if(!removed) return null;
        return questionId;
    }

    public void updateSurveyQuestion(String surveyId, String questionId, Question question) {
        List<Question> questions = retrieveAllSurveyQuestions(surveyId);
        questions.removeIf(i -> i.getId().equalsIgnoreCase(questionId));
        questions.add(question);
    }
}
