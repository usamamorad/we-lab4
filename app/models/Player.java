package models;

import java.util.ArrayList;
import java.util.List;

public class Player {
	private JeopardyUser user;
	private int profit = 0;
	private Integer latestProfitChange;
	private Question chosenQuestion;
	private List<Question> answeredQuestions = new ArrayList<>();
	
	public Player() { }
	
	public Player(JeopardyUser user) {
		this.user = user;
	}
	
	public JeopardyUser getUser() {
		return user;
	}
	
	public int getProfit() {
		return profit;
	}
	
	protected void addProfit(int profit) {
		latestProfitChange = profit;
		this.profit += profit;
	}
	
	public Integer getLatestProfitChange() {
		return this.latestProfitChange;
	}

	public void chooseQuestion(Question question) {
		this.chosenQuestion = question;
	}

	public Question getChosenQuestion() {
		return this.chosenQuestion;
	}


	public boolean hasAnsweredQuestion() {
		return getChosenQuestion() == null;
	}
	
	public void answerQuestion(List<Answer> answers) {
		if(answers.size() == chosenQuestion.getCorrectAnswers().size() &&
			answers.containsAll(chosenQuestion.getCorrectAnswers())) 
			addProfit(chosenQuestion.getValue());
		else
			addProfit(-chosenQuestion.getValue());
		answeredQuestions.add(chosenQuestion);
		chosenQuestion = null;
	}

	public List<Question> getAnsweredQuestions() {
		return answeredQuestions;
	}
}
