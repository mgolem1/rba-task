package com.rba.common.mappers;

import com.rba.common.dto.UserDTO;
import com.rba.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserMapper {

    public abstract UserDTO userToUserDTO(User entity);
}
