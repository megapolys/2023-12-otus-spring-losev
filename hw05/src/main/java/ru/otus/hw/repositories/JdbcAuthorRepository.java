package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcAuthorRepository implements AuthorRepository {

	private final NamedParameterJdbcOperations jdbc;

	@Override
	public List<Author> findAll() {
		return jdbc.query("select id, full_name from authors", new AuthorRowMapper());
	}

	@Override
	public Optional<Author> findById(long id) {
		List<Author> authors = jdbc.query(
			"select id, full_name from authors where id = :id",
			new MapSqlParameterSource("id", id),
			new AuthorRowMapper()
		);
		if (authors.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(authors.get(0));
	}

	private static class AuthorRowMapper implements RowMapper<Author> {

		@Override
		public Author mapRow(ResultSet rs, int i) throws SQLException {
			return Author.builder()
				.id(rs.getLong("id"))
				.fullName(rs.getString("full_name"))
				.build();
		}
	}
}
