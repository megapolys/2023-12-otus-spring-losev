package ru.otus.hw.testchangelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

	@ChangeSet(order = "000", id = "dropDB", author = "d.losev", runAlways = true)
	public void dropDB(MongoDatabase database) {
		database.drop();
	}

	@ChangeSet(order = "001", id = "createIdStorageDB", author = "d.losev", runAlways = true)
	public void createIdStorageDB(MongoDatabase database) {
		database.createCollection("id_transform");
	}
}
