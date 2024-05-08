package ru.otus.hw.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.entity.Author;
import ru.otus.hw.models.entity.Genre;

@ChangeLog
public class DatabaseChangelog {

	@ChangeSet(order = "001", id = "dropDb", author = "dlosev", runAlways = true)
	public void dropDb(MongoDatabase db) {
		db.drop();
	}

	@ChangeSet(order = "002", id = "createCollections", author = "dlosev")
	public void createCollections(MongoDatabase db) {
		db.createCollection("authors");
		db.createCollection("books");
		db.createCollection("comments");
		db.createCollection("genres");
	}

	@ChangeSet(order = "003", id = "createAuthors", author = "dlosev")
	public void createAuthors(MongockTemplate template) {
		template.save(new Author("Author_1"));
		template.save(new Author("Author_2"));
	}

	@ChangeSet(order = "004", id = "createGenres", author = "dlosev")
	public void createGenres(MongockTemplate template) {
		template.save(new Genre("Genre_1"));
		template.save(new Genre("Genre_2"));
		template.save(new Genre("Genre_3"));
		template.save(new Genre("Genre_4"));
	}
}
