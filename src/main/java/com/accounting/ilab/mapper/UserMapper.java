package com.accounting.ilab.mapper;


import com.accounting.ilab.entity.User;
import com.accounting.ilab.model.request.RegisterRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {

    @Mapping(target = "userName", source = "signUpRequest.email")
    User fromDtoToEntity(final RegisterRequestDto signUpRequest);


}
