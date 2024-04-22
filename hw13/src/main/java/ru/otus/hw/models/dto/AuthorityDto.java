package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import ru.otus.hw.models.entity.Role;

@Data
@AllArgsConstructor
public class AuthorityDto implements GrantedAuthority {

	private long id;

	private String roleName;

	public AuthorityDto(Role role) {
		this.id = role.getId();
		this.roleName = role.getName();
	}

	@Override
	public String getAuthority() {
		return "ROLE_" + roleName;
	}
}
