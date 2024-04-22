package ru.otus.hw.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;

@ChangeLog(order = "001")
public class InitMongoDbDropChangeLog {

	@ChangeSet(order = "000", id = "dropDB", author = "d.losev", runAlways = true)
	public void dropDB(MongoDatabase database) {
		database.drop();
	}
}
