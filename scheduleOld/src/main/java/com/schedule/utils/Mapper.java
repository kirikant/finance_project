package com.schedule.utils;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Mapper {

      private final ModelMapper modelMapper;

    public Mapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;


//        TypeMap<AccountEntity, AccountDto> propertyMapper = this.modelMapper
//                .createTypeMap(AccountEntity.class,AccountDto.class);
//
//        propertyMapper.addMappings(mapper->mapper.map(x->x.getDtCreate()
//                        .toInstant((ZoneOffset) ZoneOffset.systemDefault()),
//                AccountDto::setDtCreate)
//        );
//        propertyMapper.addMappings(mapper->mapper.map(x->x.getDtUpdate()
//                        .toInstant((ZoneOffset) ZoneOffset.systemDefault()),
//                AccountDto::setDtUpdate));
          }



    public <S,T> T map (S source ,Class<T> tClass){
        return modelMapper.map(source,tClass);
    }

    public <S,T> List<T> mapAll (Collection<? extends S> source, Class<T> tClass){
        return source.stream().map((x)->map(x,tClass)).collect(Collectors.toList());
    }
}
