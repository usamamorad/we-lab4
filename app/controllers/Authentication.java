package controllers;

import models.JeopardyDAO;
import models.JeopardyUser;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.authentication;

public class Authentication extends Controller {

	public static Result login() {
		Logger.info("Go to login page.");
		return ok(authentication.render(Form.form()));
	}

	public static Result logout() {
		Logger.info("Logout.");
		session().clear();
		return redirect(routes.Authentication.login());
	}

	@play.db.jpa.Transactional(readOnly = true)
	public static Result authenticate() {
		Logger.info("Login information has been provided.");
		DynamicForm loginForm = Form.form().bindFromRequest();
		JeopardyUser user = obtainAuthenticatedQuizUser(loginForm);
		if (user != null) {
			Secured.addAuthentication(session(), user);
			return redirect(routes.GameController.index());
		} else {
			loginForm.reject("authentication.unsuccessful");
			return badRequest(authentication.render(loginForm));
		}
	}

	private static JeopardyUser obtainAuthenticatedQuizUser(DynamicForm loginForm) {
		if (!loginForm.hasErrors()) {
			String userName = getUserName(loginForm);
			String password = getPassword(loginForm);
			JeopardyUser user = JeopardyDAO.INSTANCE.findByUserName(userName);
			if (user != null && user.authenticate(password)) {
				return user;
			}
		}
		return null;
	}

	private static String getPassword(DynamicForm loginForm) {
		return loginForm.data().get("password");
	}

	private static String getUserName(DynamicForm loginForm) {
		return loginForm.data().get("userName");
	}

}
