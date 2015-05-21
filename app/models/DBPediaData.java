package models;

import at.ac.tuwien.big.we.dbpedia.api.DBPediaService;
import at.ac.tuwien.big.we.dbpedia.api.SelectQueryBuilder;
import at.ac.tuwien.big.we.dbpedia.vocabulary.DBPedia;
import at.ac.tuwien.big.we.dbpedia.vocabulary.DBPediaOWL;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import play.Logger;

import java.util.List;
import java.util.Locale;

public class DBPediaData{

    private static QuestionBuilder buildQuestion(Category category, String questionTextEN, String questionTextDE, Model rightChoices, Model wrongChoices) {
        return new QuestionBuilder(category, questionTextEN, questionTextDE)
                .setMaximumRightChoicesUsed(4)
                .setMaximumWrongChoicesUsed(4)
                .addRightChoicesEN(DBPediaService.getResourceNames(rightChoices, "en"))
                .addRightChoicesDE(DBPediaService.getResourceNames(rightChoices, "de"))
                .addWrongChoicesEN(DBPediaService.getResourceNames(wrongChoices, "en"))
                .addWrongChoicesDE(DBPediaService.getResourceNames(wrongChoices, "de"));
    }

    //TODO: Erweitern um 4 andere DBPedia Fragen

    public static Question getQuestionActorsFromDBPedia(Category category){

        final String englishMovieQuestionFormat =
                "On which of the following movies starring %s did %s act as a director?";

        final String germanMovieQuestionFormat =
                "In welchen der folgenden Filme mit %s hat %s Regie gefuehrt?";

        //@TODO: get existing actor and director (locale name must be retrieved manually)
        Resource johnnyDepp = DBPediaService.loadStatements(DBPedia.createResource("Johnny_Depp"));
        Resource timBurton = DBPediaService.loadStatements(DBPedia.createResource("Tim_Burton"));

        String englishJohnnyDepp = DBPediaService.getResourceName(johnnyDepp, Locale.ENGLISH);
        Logger.info(englishJohnnyDepp);

        // get movies
        SelectQueryBuilder queryBuilder = DBPediaService.createQueryBuilder()
                .setLimit(2000)
                .addWhereClause(RDF.type, DBPediaOWL.Film)
                .addPredicateExistsClause(FOAF.name)
                .addPredicateExistsClause(RDFS.label)
                .addWhereClause(DBPediaOWL.starring, johnnyDepp)
                .addMinusClause(DBPediaOWL.director, timBurton)
                .addFilterClause(RDFS.label, Locale.ENGLISH)
                .addFilterClause(RDFS.label, Locale.GERMAN);

        Logger.info(String.valueOf(queryBuilder));

        Model moviesWithoutDirector = DBPediaService.loadStatements(queryBuilder.toQueryString());

        Logger.info(String.valueOf(moviesWithoutDirector));

        List<String> englishNames =
                DBPediaService.getResourceNames(moviesWithoutDirector, Locale.ENGLISH.getLanguage());
        List<String> germanNames =
                DBPediaService.getResourceNames(moviesWithoutDirector, Locale.GERMAN.getLanguage());

        Logger.info("english: " + englishNames.size() + ": " + englishNames);
        Logger.info("german: " + germanNames.size() + ": " + germanNames);

        queryBuilder.removeMinusClause(DBPediaOWL.director, timBurton);
        queryBuilder.addWhereClause(DBPediaOWL.director, timBurton);

        Model moviesWithDirector = DBPediaService.loadStatements(queryBuilder.toQueryString());

        // create question
        String englishQuestionText = String.format(englishMovieQuestionFormat,
                DBPediaService.getResourceName(johnnyDepp, Locale.ENGLISH),
                DBPediaService.getResourceName(timBurton, Locale.ENGLISH));

        String germanQuestionText = String.format(germanMovieQuestionFormat,
                DBPediaService.getResourceName(johnnyDepp, Locale.GERMAN),
                DBPediaService.getResourceName(timBurton, Locale.GERMAN));

        QuestionBuilder questionBuilder = buildQuestion(
                category,
                englishQuestionText,
                germanQuestionText,
                moviesWithDirector,
                moviesWithoutDirector);

        Question question = questionBuilder.createQuestion();

        return question;
    }

}
