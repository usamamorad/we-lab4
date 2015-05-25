package controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import highscore.Failure;
import highscore.FailureType;
import highscore.PublishHighScoreEndpoint;
import highscore.PublishHighScoreService;
import highscore.data.*;
import models.Category;
import models.JeopardyDAO;
import models.JeopardyGame;
import models.JeopardyUser;
import org.apache.jena.atlas.test.Gen;
import play.Logger;
import play.cache.Cache;
import play.data.DynamicForm;
import play.data.DynamicForm.Dynamic;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.defaultpages.error;
import views.html.jeopardy;
import views.html.question;
import views.html.winner;
import twitter.*;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

@Security.Authenticated(Secured.class)
public class GameController extends Controller {
	
	protected static final int CATEGORY_LIMIT = 5;
	
	@Transactional
	public static Result index() {
		return redirect(routes.GameController.playGame());
	}
	
	@play.db.jpa.Transactional(readOnly = true)
	private static JeopardyGame createNewGame(String userName) {
		return createNewGame(JeopardyDAO.INSTANCE.findByUserName(userName));
	}
	
	@play.db.jpa.Transactional(readOnly = true)
	private static JeopardyGame createNewGame(JeopardyUser user) {
		if(user == null) // name still stored in session, but database dropped
			return null;

		Logger.info("[" + user + "] Creating a new game.");
		List<Category> allCategories = JeopardyDAO.INSTANCE.findEntities(Category.class);
		
		if(allCategories.size() > CATEGORY_LIMIT) {
			// select 5 categories randomly (simple)
			Collections.shuffle(allCategories);
			allCategories = allCategories.subList(0, CATEGORY_LIMIT);
		}
		Logger.info("Start game with " + allCategories.size() + " categories.");
		JeopardyGame game = new JeopardyGame(user, allCategories);
		cacheGame(game);
		return game;
	}
	
	private static void cacheGame(JeopardyGame game) {
		Cache.set(gameId(), game, 3600);
	}
	
	private static JeopardyGame cachedGame(String userName) {
		Object game = Cache.get(gameId());
		if(game instanceof JeopardyGame)
			return (JeopardyGame) game;
		return createNewGame(userName);
	}
	
	private static String gameId() {
		return "game." + uuid();
	}

	private static String uuid() {
		String uuid = session("uuid");
		if (uuid == null) {
			uuid = UUID.randomUUID().toString();
			session("uuid", uuid);
		}
		return uuid;
	}
	
	@Transactional
	public static Result newGame() {
		Logger.info("[" + request().username() + "] Start new game.");
		JeopardyGame game = createNewGame(request().username());
		return ok(jeopardy.render(game));
	}
	
	@Transactional
	public static Result playGame() {
		Logger.info("[" + request().username() + "] Play the game.");
		JeopardyGame game = cachedGame(request().username());
		if(game == null) // e.g., username still in session, but db dropped
			return redirect(routes.Authentication.login());
		if(game.isAnswerPending()) {
			Logger.info("[" + request().username() + "] Answer pending... redirect");
			return ok(question.render(game));
		} else if(game.isGameOver()) {
			Logger.info("[" + request().username() + "] Game over... redirect");

			return ok(winner.render(game));
		}			
		return ok(jeopardy.render(game));
	}
	
	@play.db.jpa.Transactional(readOnly = true)
	public static Result questionSelected() {
		JeopardyGame game = cachedGame(request().username());
		if(game == null || !game.isRoundStart())
			return redirect(routes.GameController.playGame());
		
		Logger.info("[" + request().username() + "] Questions selected.");		
		DynamicForm form = Form.form().bindFromRequest();
		
		String questionSelection = form.get("question_selection");
		
		if(questionSelection == null || questionSelection.equals("") || !game.isRoundStart()) {
			return badRequest(jeopardy.render(game));
		}
		
		game.chooseHumanQuestion(Long.parseLong(questionSelection));
		
		return ok(question.render(game));
	}
	
	@play.db.jpa.Transactional(readOnly = true)
	public static Result submitAnswers() {
		JeopardyGame game = cachedGame(request().username());
		if(game == null || !game.isAnswerPending())
			return redirect(routes.GameController.playGame());
		
		Logger.info("[" + request().username() + "] Answers submitted.");
		Dynamic form = Form.form().bindFromRequest().get();
		
		@SuppressWarnings("unchecked")
		Map<String,String> data = form.getData();
		List<Long> answerIds = new ArrayList<>();
		
		for(String key : data.keySet()) {
			if(key.startsWith("answers[")) {
				answerIds.add(Long.parseLong(data.get(key)));
			}
		}
		game.answerHumanQuestion(answerIds);
		if(game.isGameOver()) {
			return redirect(routes.GameController.gameOver());
		} else {
			return ok(jeopardy.render(game));
		}
	}
	
	@play.db.jpa.Transactional(readOnly = true)
	public static Result gameOver() {
		JeopardyGame game = cachedGame(request().username());
		if(game == null || !game.isGameOver())
			return redirect(routes.GameController.playGame());

		Logger.info("***********request to server now..");
		String uuid = handleRequest(game);

		Logger.info("**************tweeting now...");
		tweetResult(game, uuid);

		Logger.info("[" + request().username() + "] Game over.");		
		return ok(winner.render(game));
	}


	public static String handleRequest(JeopardyGame game){
		Logger.info("**********I sent a request to the Server");

		ObjectFactory objectFactory = new ObjectFactory();
		UserDataType userDataType = objectFactory.createUserDataType();

		if(game.getHumanPlayer() == game.getWinner()) {
			userDataType.setWinner(configHumanWinnerUserType(game,objectFactory.createUserType()));
			userDataType.setLoser(configMarvinUserType(game,objectFactory.createUserType()));
		}else{
			userDataType.setWinner( configMarvinUserType(game,objectFactory.createUserType()));
			userDataType.setLoser(configHumanLoserUserType(game,objectFactory.createUserType()));
		}

		HighScoreRequestType highScoreRequestType = objectFactory.createHighScoreRequestType();
		highScoreRequestType.setUserData(userDataType);
		highScoreRequestType.setUserKey("3ke93-gue34-dkeu9");

		PublishHighScoreService service = new PublishHighScoreService();
		PublishHighScoreEndpoint port =  service.getPublishHighScorePort();
		String uuid = "";
		try{
			uuid = port.publishHighScore(highScoreRequestType);
			Logger.info(uuid);
		}catch (Failure f){
			Logger.error("response failed : " + f.getLocalizedMessage(), f);
		}
		return uuid;
	}

	/**
	 *
	 * @param game the current game
	 * @param human the human plajer that need to be configurated
	 * @return the human parameter congigurated as Loser
	 */
	public static UserType configHumanLoserUserType(JeopardyGame game, UserType human){
		human.setFirstName(game.getLoser().getUser().getFirstName());
		human.setLastName(game.getLoser().getUser().getLastName());
		human.setPassword("");
		human.setPoints(game.getLoser().getProfit());

		if (game.getLoser().getUser().getGender().equals("male")) {
			human.setGender(GenderType.MALE);
		} else {
			human.setGender(GenderType.FEMALE);
		}

		GregorianCalendar gcal2 = new GregorianCalendar();
		if (game.getLoser().getUser().getBirthDate() != null) {
			gcal2.setTime(game.getLoser().getUser().getBirthDate());
		}
		XMLGregorianCalendar xgcal2 = null;
		try {
			xgcal2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal2);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		human.setBirthDate(xgcal2);

		return human;
	}
	/**
	 *
	 * @param game the current game
	 * @param human the computer plajer that need to be configurated
	 * @return the computer parameter congigurated with rational assumptions
	 */
	private static UserType configMarvinUserType(JeopardyGame game, UserType pc) {
		pc.setFirstName("Ana-Maria");
		pc.setLastName("Patrascu");
		pc.setPassword("");
		if(game.getWinner() == game.getMarvinPlayer()) {
			pc.setPoints(game.getWinner().getProfit());
		}else{
			pc.setPoints(game.getLoser().getProfit());
		}
		pc.setGender(GenderType.FEMALE);

		GregorianCalendar gcal = new GregorianCalendar();
//		Date date = new Date();
//		date.setYear(1992);
//		date.setMonth(4);
//		date.setDate(3);
//		gcal.setTime(date);
		gcal.set(1992,4,3);
		XMLGregorianCalendar xgcal = null;
		try {
			xgcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		pc.setBirthDate(xgcal);
		return pc;
	}

	/**
	 *
	 * @param game the current game
	 * @param human the human plajer that need to be configurated
	 * @return the human parameter congigurated as Winner
	 */
	private static UserType configHumanWinnerUserType(JeopardyGame game, UserType human) {
		human.setFirstName(game.getWinner().getUser().getFirstName());
		human.setLastName(game.getWinner().getUser().getLastName());
		human.setPassword("");
		human.setPoints(game.getWinner().getProfit());

		if (game.getWinner().getUser().getGender().equals("male")) {
			human.setGender(GenderType.MALE);
		} else {
			human.setGender(GenderType.FEMALE);
		}

		GregorianCalendar gcal = new GregorianCalendar();
		if (game.getWinner().getUser().getBirthDate() != null) {
			gcal.setTime(game.getWinner().getUser().getBirthDate());
		}
		XMLGregorianCalendar xgcal = null;
		try {
			xgcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		human.setBirthDate(xgcal);
		return human;
	}

	private static void tweetResult(JeopardyGame game,String uuid){
		Logger.info("************I entered the tweet method()");
		String from = game.getWinner().getUser().getName();
		Date date = new Date();
		//String uuid = "DummyUUID";
		TwitterStatusMessage message = new TwitterStatusMessage(from,uuid,date);

		try{
		TwitterClient twitterClient = new TwitterClient();
		twitterClient.publishUuid(message);
		}catch(Exception e){
			Logger.error("failed to publish tweet : " + e.getMessage());
		}

	}
}
