package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.entity.Author;
import ru.otus.hw.models.entity.Book;
import ru.otus.hw.models.entity.Comment;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.Set;

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
	}

	@ChangeSet(order = "003", id = "createAuthors", author = "dlosev")
	public void createAuthors(AuthorRepository repository) {
		repository.save(new Author("1", "Author_1"));
		repository.save(new Author("2", "Author_2"));
	}

	@ChangeSet(order = "004", id = "createBooks", author = "dlosev")
	public void createBooks(BookRepository repository, AuthorRepository authorRepository) {
		repository.save(new Book("1", "BookTitle_1", authorRepository.findById("1").get(),
			Set.of("Genre_1", "Genre_2")));
		repository.save(new Book("2", "BookTitle_2", authorRepository.findById("1").get(),
			Set.of("Genre_3", "Genre_4")));
		repository.save(new Book("3", "BookTitle_3", authorRepository.findById("2").get(),
			Set.of("Genre_5", "Genre_2", "Genre_6")));
	}

	@ChangeSet(order = "005", id = "createComments", author = "dlosev")
	public void createComments(CommentRepository repository, BookRepository bookRepository) {
		repository.save(new Comment("1", "Comment_1", bookRepository.findById("1").get()));
		repository.save(new Comment("2", "Comment_2", bookRepository.findById("1").get()));
		repository.save(new Comment("3", "Comment_3", bookRepository.findById("3").get()));
	}
}
