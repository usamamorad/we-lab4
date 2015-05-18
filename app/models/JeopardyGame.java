/**
 * <copyright>
 * 
 * Copyright (c) 2014 http://www.big.tuwien.ac.at All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * </copyright>
 */
package models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import models.JeopardyUser.Gender;


public class JeopardyGame {	
	private static final int NUM_QUESTIONS = 5;

	private Random random = new Random();
	
	private List<Category> categories;
	private Player human, marvin; // marvin == computer opponent	
	
	private Map<Long, Question> idToQuestion = new TreeMap<>();
	private List<Question> openQuestions = new ArrayList<>();
	private List<Question> chosenQuestions = new ArrayList<>();
		

	public JeopardyGame(JeopardyUser human, List<Category> categories) {
		this.human = createHumanPlayer(human);
		this.marvin = createComputerPlayer();
		this.categories = categories;
		initializeQuestions();
	}
	
	public JeopardyGame(String humanName, List<Category> categories) {
		this(createHumanUser(humanName), categories);
	}

	public JeopardyGame(List<Category> categories) {
		this("Spieler 1", categories);
	}
	
	private Player createHumanPlayer(JeopardyUser user) {
		return new Player(user);
	}

	private static JeopardyUser createHumanUser(String name) {
		JeopardyUser user = new JeopardyUser();
		user.setName(name);
		user.setAvatar(Avatar.getRandomAvatar());
		return user;
	}

	private Player createComputerPlayer() {
		JeopardyUser user = new JeopardyUser();
		user.setAvatar(Avatar.getOpponent(getHumanPlayer().getUser().getAvatar()));
		user.setName(user.getAvatar().getName());
		user.setFirstName("Marvin");
		user.setLastName("ParanoidAndroid");
		user.setGender(Gender.male);
		Calendar calendar = Calendar.getInstance();
		calendar.set(1979, 1, 1);
		user.setBirthDate(calendar.getTime());
		return new Player(user);
	}
	
	protected void initializeQuestions() {
		for(Category category : categories) {
			category.sort();
			for(Question question : category.getQuestions()) {
				idToQuestion.put(question.getId(), question);
				openQuestions.add(question);
			}
		}
	}
		
	public List<Category> getCategories() {
		return categories;
	}
	
	protected Random getRandom() {
		return random;
	}
	
	public Player getHumanPlayer() {
		return human;
	}
	
	public Player getMarvinPlayer() {
		return marvin;
	}
	
	public JeopardyUser getHuman() {
		return getHumanPlayer().getUser();
	}
	
	public JeopardyUser getMarvin() {
		return getMarvinPlayer().getUser();
	}
	
	public boolean isHumanPlayer(JeopardyUser user) {
		return getHumanPlayer().getUser() == user;
	}
	
	public boolean isComputerPlayer(JeopardyUser user) {
		return getMarvinPlayer().getUser() == user;
	}
		
	public void chooseHumanQuestion(Long questionId) {
		Question question = idToQuestion.get(questionId);
		if(question == null || chosenQuestions.contains(question)) 
			throw new IllegalArgumentException("Question can not be chosen.");
		
		chooseQuestion(getHumanPlayer(), question);
		
		// if marvin has not chosen a new question yet, he will do so now
		if(getMarvinPlayer().hasAnsweredQuestion())
			autoChooseQuestionMarvin();
	}
	
	public void answerHumanQuestion(List<Long> answers) {
		List<Answer> givenAnswers = new ArrayList<>();
		for(Answer answer : getHumanPlayer().getChosenQuestion().getAnswers()) {
			if(answers.contains(answer.getId()))
				givenAnswers.add(answer);
		}
		getHumanPlayer().answerQuestion(givenAnswers);
		
		autoAnswerQuestionMarvin();
		
		// marvin chooses first if he has less profit than human opponent
		if(getMarvinPlayer().getProfit() < getHumanPlayer().getProfit())
			autoChooseQuestionMarvin();
	}
	
	public int getMaxQuestions() {
		return NUM_QUESTIONS;
	}
	
	protected void chooseQuestion(Player player, Question question) {
		player.chooseQuestion(question);
		chosenQuestions.add(question);
		openQuestions.remove(question);
	}
	
	protected void autoChooseQuestionMarvin() {
		Question question = openQuestions.get(
				getRandom().nextInt(openQuestions.size()));
		chooseQuestion(getMarvinPlayer(), question);	
	}

	private void autoAnswerQuestionMarvin() {
		if(getRandom().nextDouble() < 0.5)
			getMarvinPlayer().answerQuestion( 
				getMarvinPlayer().getChosenQuestion().getCorrectAnswers());
		else
			getMarvinPlayer().answerQuestion( 
					getMarvinPlayer().getChosenQuestion().getAnswers());
	}

	public boolean isGameOver() {
		return getHumanPlayer().getAnsweredQuestions().size() == NUM_QUESTIONS &&
			   getMarvinPlayer().getAnsweredQuestions().size() == NUM_QUESTIONS;
	}

	public boolean isRoundStart() {
		return !isGameOver() &&
				getHumanPlayer().hasAnsweredQuestion();
	}
	
	public boolean isAnswerPending() {
		return !getHumanPlayer().hasAnsweredQuestion();
	}
	
	public Player getLeader() {
		if(human.getProfit() >= marvin.getProfit())
			return human; // advantage human
		return marvin;
	}
	
	public Player getSecond() {
		if(human.getProfit() >= marvin.getProfit())
			return marvin;	
		return human;
	}

	public Player getWinner() {
		if(!isGameOver()) 
			return null;
		
		if(human.getProfit() > marvin.getProfit())
			return human;	
		return marvin; // advantage marvin
	}
	
	public Player getLoser() {
		if(!isGameOver()) 
			return null;
		
		if(human.getProfit() > marvin.getProfit())
			return marvin;	
		return human;
	}

	public boolean hasBeenChosen(Question question) {
		return chosenQuestions.contains(question);
	}
	
}
