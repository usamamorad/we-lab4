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
package data;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import play.db.jpa.Transactional;
import models.Answer;
import models.Category;
import models.JeopardyDAO;
import models.Question;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class JSONDataInserter {

	private static Gson createGson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Category.class, new CategoryDeserializer());
		gsonBuilder.registerTypeAdapter(Question.class,	new QuestionDeserializer());
		gsonBuilder.registerTypeAdapter(Answer.class,	new AnswerDeserializer());
		return gsonBuilder.create();
	}

	public static List<Category> loadCategoryData(InputStream inputStream) {
		Gson gson = createGson();
		Type collectionType = new TypeToken<List<Category>>() {}.getType();
		
		List<Category> categories = gson.fromJson(
				new InputStreamReader(inputStream, Charsets.UTF_8), collectionType);
		
		return categories;
	}
	
	@Transactional
	public static List<Category> insertData(InputStream inputStream) {
		List<Category> jsonCategories = loadCategoryData(inputStream);
		for(Category category : jsonCategories)
			JeopardyDAO.INSTANCE.persist(category);
		return jsonCategories;
	}
}

class CategoryDeserializer implements JsonDeserializer<Category> {

	@Override
	public Category deserialize(JsonElement json, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		
		Category category = new Category();
		JsonObject object = json.getAsJsonObject();
		
		category.setNameDE(object.get("nameDE").getAsString());
		category.setNameEN(object.get("nameEN").getAsString());
		
		for (JsonElement jsonQuestion : object.get("questions").getAsJsonArray()) {
			Question question = context.deserialize(jsonQuestion,
					new TypeToken<Question>() {}.getType());
			category.addQuestion(question);
		}

		return category;
	}

}

class QuestionDeserializer implements JsonDeserializer<Question> {

	@Override
	public Question deserialize(JsonElement json, Type type,
			JsonDeserializationContext context) throws JsonParseException {

		Question question = new Question();

		JsonObject object = json.getAsJsonObject();
		question.setTextDE(object.get("textDE").getAsString());
		question.setTextEN(object.get("textEN").getAsString());
		question.setValue(object.get("value").getAsInt());

		for (JsonElement wrongAnswer : object.get("wrongAnswers").getAsJsonArray()) {
			Answer answer = context.deserialize(wrongAnswer,
					new TypeToken<Answer>() {}.getType());
			question.addWrongAnswer(answer);
		}
		
		for (JsonElement correctAnswer : object.get("rightAnswers")
				.getAsJsonArray()) {
			Answer answer = context.deserialize(correctAnswer,
					new TypeToken<Answer>() {}.getType());
			question.addRightAnswer(answer);
		}

		return question;
	}
}

class AnswerDeserializer implements JsonDeserializer<Answer> {

	@Override
	public Answer deserialize(JsonElement json, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		
		Answer answer = new Answer();
		JsonObject object = json.getAsJsonObject();
		answer.setTextDE(object.get("textDE").getAsString());
		answer.setTextEN(object.get("textEN").getAsString());
		
		return answer;
	}

}
