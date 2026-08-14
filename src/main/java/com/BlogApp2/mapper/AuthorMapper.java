package com.BlogApp2.mapper;

import com.BlogApp2.dto.response.AuthorSummaryDto;
import com.BlogApp2.model.User;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {
    public AuthorSummaryDto toAuthorSummary(User user){
        return new AuthorSummaryDto(
                user.getId(),
                user.getName()
        );
    }
}
