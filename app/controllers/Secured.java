package controllers;

import models.JeopardyUser;
import play.mvc.Http.Context;
import play.mvc.Http.Session;
import play.mvc.Result;
import play.mvc.Security;

public class Secured extends Security.Authenticator {

	private static final String USER_NAME = "userName";

	public static void addAuthentication(Session session, JeopardyUser user) {
		session.clear();
		session.put(USER_NAME, user.getUserName());
	}
	
	public static String getAuthentication(Session session) {
		return session.get(USER_NAME);
	}

	@Override
	public String getUsername(Context ctx) {
		return ctx.session().get(USER_NAME);
	}

	@Override
	public Result onUnauthorized(Context ctx) {
		return redirect(routes.Authentication.login());
	}
}