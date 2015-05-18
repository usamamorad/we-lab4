package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a category, which is stored in the DB
 */
public class Category extends BaseEntity {


    private String nameDE;
    private String nameEN;


    //A list of questions in this category
    private List<Question> questions = new ArrayList<Question>();
    
    /**
     * Add a new question to the category
     * @param question
     */
    public void addQuestion(Question question) {
        question.setCategory(this);
        questions.add(question);
    }

    /**
     * Set the name attribute based on the given language. Default to English if no or an invalid language is passed
     * @param name
     * @param lang
     */
    public void setName(String name, String lang) {
        if ("de".equalsIgnoreCase(lang)) {
            this.nameDE = name;
        }
        else {
            this.nameEN = name;
        }
    }

    /**
     * Get the name attribute based on the given language. Default to English if no or an invalid language is passed
     * @param lang
     * @return
     */
    public String getName(String lang) {
        if ("de".equalsIgnoreCase(lang)) {
            return this.nameDE;
        }
        else {
            return this.nameEN;
        }
    }

    public String getNameDE() {
        return nameDE;
    }

    public void setNameDE(String nameDE) {
        this.nameDE = nameDE;
    }

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
    
    public void sort() {
    	Collections.sort(questions, new Comparator<Question>() {
			@Override
			public int compare(Question questionA, Question questionB) {
				return Integer.compare(questionA.getValue(), questionB.getValue());
			}

		});
    }
}
