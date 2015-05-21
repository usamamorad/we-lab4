package models;

import java.util.*;

public class QuestionBuilder {
    private static int MINIMUM_RIGHT_CHOICES = 1;
    private static int MINIMUM_WRONG_CHOICES = 1;
    private static int DBPediaQuestionVALUE = 60;

    private Category category;
    private String textEN;
    private String textDE;

    private List<String> rightChoicesEN;
    private List<String> rightChoicesDE;

    private List<String> wrongChoicesEN;
    private List<String> wrongChoicesDE;


    private int minimumRightChoicesUsed = 3;
    private int maximumRightChoicesUsed = 3;

    private int minimumWrongChoicesUsed = 3;
    private int maximumWrongChoicesUsed = 3;

    public QuestionBuilder() {
        rightChoicesEN = new ArrayList<String>();
        wrongChoicesEN = new ArrayList<String>();
        rightChoicesDE = new ArrayList<String>();
        wrongChoicesDE = new ArrayList<String>();
    }

    public QuestionBuilder(String questionTextEN, String questionTextDE) {
        this();
        setText(questionTextEN, questionTextDE);
    }

    public QuestionBuilder(Category category, String questionTextEN, String questionTextDE) {
        this(questionTextEN, questionTextDE);
        setCategory(category);
    }


    public QuestionBuilder setText(String textEN, String textDE) {
        this.textEN = textEN;
        this.textDE = textDE;
        return this;
    }

    public String getTextEN() {
        return textEN;
    }
    public String getTextDE() {
        return textDE;
    }

    public QuestionBuilder addRightChoicesEN(List<String> choices) {
        rightChoicesEN.addAll(choices);
        return this;
    }
    public QuestionBuilder addRightChoicesDE(List<String> choices) {
        rightChoicesDE.addAll(choices);
        return this;
    }

    public QuestionBuilder addWrongChoicesEN(List<String> choices) {
        wrongChoicesEN.addAll(choices);
        return this;
    }
    public QuestionBuilder addWrongChoicesDE(List<String> choices) {
        wrongChoicesDE.addAll(choices);
        return this;
    }

    public List<String> getRightChoicesEN() {
        return rightChoicesEN;
    }

    public List<String> getWrongChoicesEN() {
        return wrongChoicesEN;
    }

    public List<String> getRightChoicesDE() {
        return rightChoicesDE;
    }

    public List<String> getWrongChoicesDE() {
        return wrongChoicesDE;
    }

    public int getMaximumRightChoicesUsed() {
        return maximumRightChoicesUsed;
    }

    public QuestionBuilder setMaximumRightChoicesUsed(int maximumRightChoicesUsed) {
        this.maximumRightChoicesUsed = maximumRightChoicesUsed;
        return this;
    }

    public int getMaximumWrongChoicesUsed() {
        return maximumWrongChoicesUsed;
    }

    public QuestionBuilder setMaximumWrongChoicesUsed(int maximumWrongChoicesUsed) {
        this.maximumWrongChoicesUsed = maximumWrongChoicesUsed;
        return this;
    }

    public int getMinimumRightChoicesUsed() {
        return minimumRightChoicesUsed;
    }

    public int getMinimumWrongChoicesUsed() {
        return minimumWrongChoicesUsed;
    }

    public Category getCategory() {
        return category;
    }

    public QuestionBuilder setCategory(Category category) {
        this.category = category;
        return this;
    }

    public Question createQuestion() {

        if(getCategory() == null)
            throw new IllegalArgumentException("Category can not be empty!");

        if(getRightChoicesEN().size() < MINIMUM_RIGHT_CHOICES ||
                getMaximumRightChoicesUsed() < MINIMUM_RIGHT_CHOICES)
            throw new IllegalArgumentException("At least one correct choice must be presented.");

        if(getWrongChoicesEN().size() < MINIMUM_WRONG_CHOICES ||
                getMaximumWrongChoicesUsed() < MINIMUM_WRONG_CHOICES)
            throw new IllegalArgumentException("At least one wrong choice must be presented.");

        if(getMinimumRightChoicesUsed() < MINIMUM_RIGHT_CHOICES)
            throw new IllegalArgumentException("Can not set number of minimum right choices below " + MINIMUM_RIGHT_CHOICES);

        if(getMinimumWrongChoicesUsed() < MINIMUM_WRONG_CHOICES)
            throw new IllegalArgumentException("Can not set number of minimum wrong choices below " + MINIMUM_WRONG_CHOICES);

        if(getMinimumRightChoicesUsed() > getRightChoicesEN().size())
            System.err.println("Minimum Right Choices set to " + getMinimumRightChoicesUsed() + ", but only " + getRightChoicesEN().size() + " available. Setting will be ignored.");

        if(getMinimumWrongChoicesUsed() > getWrongChoicesEN().size())
            System.err.println("Minimum Wrong Choices set to " + getMinimumWrongChoicesUsed() + ", but only " + getWrongChoicesEN().size() + " available. Setting will be ignored.");

        Random random = new Random();
        int nrRightChoices = Math.min(getRightChoicesEN().size(), getMinimumRightChoicesUsed() +
                random.nextInt(Math.min(getMaximumRightChoicesUsed(), getRightChoicesEN().size())));

        int nrWrongChoices = Math.min(getWrongChoicesEN().size(), getMinimumWrongChoicesUsed() +
                random.nextInt(Math.min(getMaximumWrongChoicesUsed(), getWrongChoicesEN().size())));

        //TODO: shuffeling ist hier ein problem, weil sonst die reihenfolge pro index EN->DE verloren geht
//        Collections.shuffle(getRightChoicesEN());
//        Collections.shuffle(getWrongChoicesEN());
//        Collections.shuffle(getRightChoicesDE());
//        Collections.shuffle(getWrongChoicesDE());

        Question question = new Question();
        question.setCategory(getCategory());
        question.setValue(DBPediaQuestionVALUE);
        question.setTextEN(getTextEN());
        question.setTextDE(getTextDE());

        Answer choice;
        //int choiceId = 0;
        for(int i = 0; i < nrRightChoices; i++) {
            choice = new Answer();
            choice.setText(getRightChoicesEN().get(i), "en");
            choice.setText(getRightChoicesDE().get(i), "de");
            //choice.setId(choiceId++);
            question.addRightAnswer(choice);
        }

        for(int i = 0; i < nrWrongChoices; i++) {
            choice = new Answer();
            choice.setText(getWrongChoicesEN().get(i), "en");
            choice.setText(getWrongChoicesDE().get(i), "de");
            //choice.setId(choiceId++);
            question.addWrongAnswer(choice);
        }

        return question;
    }
}
