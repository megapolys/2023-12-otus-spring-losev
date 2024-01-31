package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {

	private final NamedParameterJdbcOperations jdbc;

	@Override
	public List<Genre> findAll() {
		return jdbc.query("select id, name from genres", new GnreRowMapper());
	}

	@Override
	public List<Genre> findAllByIds(Set<Long> ids) {
		return jdbc.query(
			"select id, name from genres where id in (:ids)",
			new MapSqlParameterSource("ids", ids),
			new GnreRowMapper()
		);
	}

	private static class GnreRowMapper implements RowMapper<Genre> {

		@Override
		public Genre mapRow(ResultSet rs, int i) throws SQLException {
			return Genre.builder()
				.id(rs.getLong("id"))
				.name(rs.getString("name"))
				.build();
		}
	}
}
