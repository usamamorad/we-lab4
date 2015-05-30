import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.big.we.dbpedia.api.DBPediaService;
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

		//create new Category
		Category category = new Category();
		category.setNameDE("Filme");
		category.setNameEN("Movies");
		categories.add(category);

		//insert Questions from DBPedia into new Category
		insertDBPediaData(categories);

		Logger.info(categories.size() + " categories from json file '" + file + "' inserted.");
	}

	@play.db.jpa.Transactional
	private static void insertDBPediaData(List<Category> categories) {
		if(!DBPediaService.isAvailable()) {
			Logger.info("DBpedia is currently not available.");
			return;
		}

		categories.get(5).addQuestion(DBPediaData.getQuestionActorsWithDirectorsFromDBPedia(categories.get(5), "Johnny_Depp", "Tim_Burton", 50));
		categories.get(5).addQuestion(DBPediaData.getQuestionActorsWithDirectorsFromDBPedia(categories.get(5), "Ryan_Gosling", "Derek_Cianfrance", 40));
		categories.get(5).addQuestion(DBPediaData.getQuestionActorsWithDirectorsFromDBPedia(categories.get(5), "Bradley_Cooper", "Todd_Phillips", 30));
		categories.get(5).addQuestion(DBPediaData.getQuestionMoviesMusicFromAuthor(categories.get(5), "John_Williams", 20));
		categories.get(5).addQuestion(DBPediaData.getQuestionWhichArtistHasEmmyAward(categories.get(5), 10));

		JeopardyDAO.INSTANCE.merge(categories.get(5));

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