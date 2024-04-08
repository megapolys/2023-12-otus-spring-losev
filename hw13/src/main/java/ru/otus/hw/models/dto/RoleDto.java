package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import ru.otus.hw.models.entity.Role;

@Data
@AllArgsConstructor
public class RoleDto implements GrantedAuthority {

	private long id;

	private String roleName;

	public RoleDto(Role role) {
		this.id = role.getId();
		this.roleName = role.getName();
	}

	@Override
	public String getAuthority() {
		return "ROLE_" + roleName;
	}
}
