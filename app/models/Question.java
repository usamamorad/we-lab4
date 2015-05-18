package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a question, which is stored in the DB
 */
public class Question extends BaseEntity {

    private String textDE;
    private String textEN;
    private int value;

    //The category to which this question belongs to
    private Category category;


    //A list of right choices in this category
    private List<Answer> answers = new ArrayList<Answer>();


    /**
     * Add a wrong choice
     * @param choice
     */
    public void addWrongAnswer(Answer choice) {
        choice.setQuestion(this);
        choice.setCorrectAnswer(Boolean.FALSE);
        answers.add(choice);
    }


    /**
     * Add a right choice
     * @param choice
     */
    public void addRightAnswer(Answer choice) {
        choice.setQuestion(this);
        choice.setCorrectAnswer(Boolean.TRUE);
        answers.add(choice);
    }


    /**
     * Set the text attribute based on the given language. Default to English if no or an invalid language is passed
     * @param name
     * @param lang
     */
    public void setText(String name, String lang) {
        if ("de".equalsIgnoreCase(lang)) {
            this.textDE = name;
        }
        else {
            this.textEN = name;
        }
    }

    /**
     * Get the text attribute based on the given language. Default to English if no or an invalid language is passed
     * @param lang
     * @return
     */
    public String getText(String lang) {
        if ("de".equalsIgnoreCase(lang)) {
            return this.textDE;
        }
        else {
            return this.textEN;
        }
    }



    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getTextDE() {
        return textDE;
    }

    public void setTextDE(String textDE) {
        this.textDE = textDE;
    }

    public String getTextEN() {
        return textEN;
    }

    public void setTextEN(String textEN) {
        this.textEN = textEN;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> choices) {
        this.answers = choices;
    }
    
    public List<Answer> getCorrectAnswers() {
    	List<Answer> correct = new ArrayList<Answer>();
    	for(Answer c : answers)
    		if(c.isRight())
    			correct.add(c);
    	return correct;
    }
    
    public List<Answer> getWrongAnswers() {
    	List<Answer> wrong = new ArrayList<Answer>();
    	for(Answer c : answers)
    		if(c.isWrong())
    			wrong.add(c);
    	return wrong;
    }
    
    public List<Answer> getShuffledAnswers() {
    	List<Answer> answers = new ArrayList<>(getAnswers());
    	Collections.shuffle(answers);
    	return answers;
    }
}
