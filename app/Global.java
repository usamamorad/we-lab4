import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import models.Category;
import models.DBPediaData;
import models.JeopardyDAO;
import models.Question;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.Play;
import play.db.jpa.JPA;
import play.libs.F.Function0;
import data.JSONDataInserter;

public class Global extends GlobalSettings {
	
	@play.db.jpa.Transactional
	public static void insertJSonData() throws IOException {
		String file = Play.application().configuration().getString("questions.filePath");		
		Logger.info("Data from: " + file);
		InputStream is = Play.application().resourceAsStream(file);
		List<Category> categories = JSONDataInserter.insertData(is);

		//insert Questions from DBPedia
		insertDBPediaData(categories);

		Logger.info(categories.size() + " categories from json file '" + file + "' inserted.");
	}

	@play.db.jpa.Transactional
	private static void insertDBPediaData(List<Category> categories) {

		//TODO: hier fuege ich eine Frage der Kategorie 0 hinzu, es muss zu jeder kategorie noch eines gemacht werden(erweitern von DBPediaData.java)
		Question q = DBPediaData.getQuestionActorsFromDBPedia(categories.get(0));
		categories.get(0).addQuestion(q);

		//TODO: merge with existing Category
		JeopardyDAO.INSTANCE.persist(categories.get(0));

	}

	public void onStart(Application app) {
       try {
    	   JPA.withTransaction(new Function0<Boolean>() {

			@Override
			public Boolean apply() throws Throwable {
				insertJSonData();
				return true;
			}
			   
			});
       } catch (Throwable e) {
    	   e.printStackTrace();
       }
    }

    public void onStop(Application app) {
        Logger.info("Application shutdown...");
    }

}