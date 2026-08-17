package com.BlogApp2.graphql;

import com.BlogApp2.exception.CommentNotFoundException;
import com.BlogApp2.exception.CommentPostMismatchException;
import com.BlogApp2.exception.DuplicateEmailException;
import com.BlogApp2.exception.DuplicateReviewException;
import com.BlogApp2.exception.DuplicateTagException;
import com.BlogApp2.exception.PostNotFoundException;
import com.BlogApp2.exception.ReviewNotFoundException;
import com.BlogApp2.exception.ReviewPostMismatchException;
import com.BlogApp2.exception.TagInUseException;
import com.BlogApp2.exception.TagNotFoundException;
import com.BlogApp2.exception.UserNotFoundException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * The GraphQL counterpart to GlobalExceptionHandler. REST exceptions are turned into
 * clean HTTP status codes by @RestControllerAdvice, which has no effect on GraphQL at
 * all, GraphQL execution has its own separate error-handling pipeline. Without this,
 * every one of our custom exceptions would surface identically as an opaque
 * INTERNAL_ERROR, regardless of whether the real cause was a bad request or a missing
 * record.
 */
@Component
public class GraphQLExceptionResolver extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof UserNotFoundException
                || ex instanceof PostNotFoundException
                || ex instanceof CommentNotFoundException
                || ex instanceof TagNotFoundException
                || ex instanceof ReviewNotFoundException) {
            return buildError(ex.getMessage(), ErrorType.NOT_FOUND, env);
        }

        if (ex instanceof DuplicateEmailException
                || ex instanceof DuplicateTagException
                || ex instanceof DuplicateReviewException
                || ex instanceof CommentPostMismatchException
                || ex instanceof ReviewPostMismatchException
                || ex instanceof TagInUseException) {
            return buildError(ex.getMessage(), ErrorType.BAD_REQUEST, env);
        }

        if (ex instanceof ConstraintViolationException constraintViolationException) {
            String message = constraintViolationException.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            return buildError(message, ErrorType.BAD_REQUEST, env);
        }

        // anything else falls through to Spring GraphQL's default INTERNAL_ERROR handling
        return null;
    }

    private GraphQLError buildError(String message, ErrorType type, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError(env)
                .errorType(type)
                .message(message)
                .build();
    }
}
