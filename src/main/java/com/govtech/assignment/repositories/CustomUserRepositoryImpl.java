package com.govtech.assignment.repositories;

import com.govtech.assignment.dtos.UserGetRequest;
import com.govtech.assignment.entities.User;
import com.govtech.assignment.utilities.QueryBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class CustomUserRepositoryImpl implements CustomUserRepository {
    public static final String QUERIES_FIND_WITH_SALARY_BETWEEN = "SELECT u FROM User u WHERE u.salary BETWEEN ?1 AND ?2";
    @PersistenceContext
    EntityManager em;

    public List<User> findUsersFromRequest(UserGetRequest request) {
        QueryBuilder<User> queryBuilder = new QueryBuilder<>(
                QUERIES_FIND_WITH_SALARY_BETWEEN,
                em,
                User.class);
        if (request.getSort() != null) {
            queryBuilder.setOrderBy(request.getSort().toString());
        }
        TypedQuery<User> query = queryBuilder
                .setParam(1, request.getMin())
                .setParam(2, request.getMax())
                .setOffset(request.getOffset())
                .setLimit(request.getLimit())
                .build();
        return query.getResultList();
    }
}
