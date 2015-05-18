package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import models.JeopardyDAO;
import models.JeopardyUser;
import play.Logger;
import play.data.Form;
import play.data.format.Formatters;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.registration;

public class Registration extends Controller {

	public static Result index() {
		Logger.info("Go to Registration page.");
		return ok(registration.render(Form.form(JeopardyUser.class)));
	}

	@play.db.jpa.Transactional
	public static Result create() {
		Logger.info("Registration info has been submitted.");
		registerDateFormatter();
		Form<JeopardyUser> form = Form.form(JeopardyUser.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest(registration.render(form));
		} else {
			JeopardyUser user = form.get();
			// very simplistic storage, in a production scenario the password should be encrypted
			JeopardyDAO.INSTANCE.persist(user);
			flash("registration.successful", "user.created-successfully");
			return redirect(routes.Authentication.login());
		}
	}
	
	public static void registerDateFormatter() {
		Formatters.register(Date.class, new Formatters.SimpleFormatter<Date>(){
			
			@Override
			public Date parse(String text, Locale locale) throws java.text.ParseException{				
				Date date;
				try {
					date = new SimpleDateFormat("dd.MM.yyyy", locale).parse(text);
				} catch (java.text.ParseException e) {
					// As some browsers print 01/01/2000 but send 2000-01-01, we defined this fallback case.
					try {
						date = new SimpleDateFormat("MM/dd/yyyy", locale).parse(text); 
					} catch (java.text.ParseException ex) {
						date = new SimpleDateFormat("yyyy-MM-dd", locale).parse(text); 
					}
				} 
				return date;
			}

			@Override
			public String print(Date date, Locale locale) {
				return date.toString();
			}
		});
	}

}
