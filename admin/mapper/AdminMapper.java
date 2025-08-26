package com.redbooks.admin.mapper;

import com.redbooks.admin.dto.AdminDto;
import com.redbooks.admin.entity.Admin;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminMapper {
	private final ModelMapper mapper;

	public AdminDto toDto(Admin entity) {
		return mapper.map(entity, AdminDto.class);
	}

	public Admin toEntity(AdminDto dto) {
		return mapper.map(dto, Admin.class);
	}
}
