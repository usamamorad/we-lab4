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

    public static Question getQuestionWhichArtistHasEmmyAward(Category category, int value){

        String germanQuestionText = "Welche der folgenden Künstler haben schon einen Emmy Award?";
        String englishQuestionText = "Which of the following artists already has a Emmy Award?";

        Resource award = DBPediaService.loadStatements(DBPedia.createResource("Emmy_Award"));

        SelectQueryBuilder queryBuilder = DBPediaService.createQueryBuilder()
                .setLimit(4)
                .addWhereClause(RDF.type, DBPediaOWL.Person)
                .addPredicateExistsClause(FOAF.name)
                .addPredicateExistsClause(DBPediaOWL.award)
                .addWhereClause(DBPediaOWL.award, award)
                .addFilterClause(RDFS.label, Locale.GERMAN)
                .addFilterClause(RDFS.label, Locale.ENGLISH);

        Model ArtistsWithAward = DBPediaService.loadStatements(queryBuilder.toQueryString());

        queryBuilder.removeWhereClause(DBPediaOWL.award, award);
        queryBuilder.addMinusClause(DBPediaOWL.award, award);

        Model noArtistsWithAward = DBPediaService.loadStatements(queryBuilder.toQueryString());

        QuestionBuilder questionBuilder = buildQuestion(
                category,
                englishQuestionText,
                germanQuestionText,
                ArtistsWithAward,
                noArtistsWithAward);

        Question question = questionBuilder.createQuestion();
        question.setValue(value);

        return question;

    }

    public static Question getQuestionMoviesMusicFromAuthor(Category category, String composerName, int value){

        final String englishMovieQuestionFormat =
                "On which of the following movies did %s compose the music?";

        final String germanMovieQuestionFormat =
                "In welchen der folgenden Filme hat %s die Musik komponiert?";

        Resource composer = DBPediaService.loadStatements(DBPedia.createResource(composerName));

        // get movies
        SelectQueryBuilder queryBuilder = DBPediaService.createQueryBuilder()
                .setLimit(4) // at most four statements
                .addWhereClause(RDF.type, DBPediaOWL.Film)
                .addPredicateExistsClause(FOAF.name)
                .addWhereClause(DBPediaOWL.musicComposer, composer)
                .addFilterClause(RDFS.label, Locale.GERMAN)
                .addFilterClause(RDFS.label, Locale.ENGLISH);

        Model moviesWithComposer = DBPediaService.loadStatements(queryBuilder.toQueryString());

        queryBuilder.removeWhereClause(DBPediaOWL.musicComposer, composer);
        queryBuilder.addMinusClause(DBPediaOWL.musicComposer, composer);

        Model moviesWithoutComposer = DBPediaService.loadStatements(queryBuilder.toQueryString());

        // create question
        String englishQuestionText = String.format(englishMovieQuestionFormat,
                DBPediaService.getResourceName(composer, Locale.ENGLISH));

        String germanQuestionText = String.format(germanMovieQuestionFormat,
                DBPediaService.getResourceName(composer, Locale.GERMAN));

        QuestionBuilder questionBuilder = buildQuestion(
                category,
                englishQuestionText,
                germanQuestionText,
                moviesWithComposer,
                moviesWithoutComposer);

        Question question = questionBuilder.createQuestion();
        question.setValue(value);

        return question;
    }

    public static Question getQuestionActorsWithDirectorsFromDBPedia(Category category, String actorName, String directorName, int value){

        final String englishMovieQuestionFormat =
                "On which of the following movies starring %s did %s act as a director?";

        final String germanMovieQuestionFormat =
                "In welchen der folgenden Filme mit %s hat %s Regie geführt?";

        Resource actor = DBPediaService.loadStatements(DBPedia.createResource(actorName));
        Resource director = DBPediaService.loadStatements(DBPedia.createResource(directorName));

        // get movies
        SelectQueryBuilder queryBuilder = DBPediaService.createQueryBuilder()
                .setLimit(2000)
                .addWhereClause(RDF.type, DBPediaOWL.Film)
                .addPredicateExistsClause(FOAF.name)
                .addPredicateExistsClause(RDFS.label)
                .addWhereClause(DBPediaOWL.starring, actor)
                .addMinusClause(DBPediaOWL.director, director)
                .addFilterClause(RDFS.label, Locale.ENGLISH)
                .addFilterClause(RDFS.label, Locale.GERMAN);

        Model moviesWithoutDirector = DBPediaService.loadStatements(queryBuilder.toQueryString());

        queryBuilder.removeMinusClause(DBPediaOWL.director, director);
        queryBuilder.addWhereClause(DBPediaOWL.director, director);

        Model moviesWithDirector = DBPediaService.loadStatements(queryBuilder.toQueryString());

        // create question
        String englishQuestionText = String.format(englishMovieQuestionFormat,
                DBPediaService.getResourceName(actor, Locale.ENGLISH),
                DBPediaService.getResourceName(director, Locale.ENGLISH));

        String germanQuestionText = String.format(germanMovieQuestionFormat,
                DBPediaService.getResourceName(actor, Locale.GERMAN),
                DBPediaService.getResourceName(director, Locale.GERMAN));

        QuestionBuilder questionBuilder = buildQuestion(
                category,
                englishQuestionText,
                germanQuestionText,
                moviesWithDirector,
                moviesWithoutDirector);

        Question question = questionBuilder.createQuestion();
        question.setValue(value);

        return question;
    }

}
