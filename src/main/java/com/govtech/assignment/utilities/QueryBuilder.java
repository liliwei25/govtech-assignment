package com.govtech.assignment.utilities;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.Map;

public class QueryBuilder<T> {
    private final StringBuilder queryString;
    private final EntityManager em;
    private final Class<T> objectClass;
    private final Map<Integer, Object> params = new HashMap<>();

    private String orderBy;
    private Integer offset;
    private Integer limit;

    public QueryBuilder(String baseQuery, EntityManager em, Class<T> objectClass) {
        if (baseQuery == null || baseQuery.length() == 0) {
            throw new IllegalArgumentException("Base query should not be null or empty");
        }
        this.queryString = new StringBuilder(baseQuery);
        this.em = em;
        this.objectClass = objectClass;
    }

    public QueryBuilder<T> setOrderBy(String property) {
        if (property != null && property.length() > 0) {
            this.orderBy = property;
        }
        return this;
    }

    public QueryBuilder<T> setOffset(Integer offset) {
        if (offset != null && offset >= 0) {
            this.offset = offset;
        }
        return this;
    }

    public QueryBuilder<T> setLimit(Integer limit) {
        if (limit != null && limit >= 0) {
            this.limit = limit;
        }
        return this;
    }

    public QueryBuilder<T> setParam(Integer paramId, Object param) {
        if (paramId != null && paramId > 0 && param != null) {
            this.params.put(paramId, param);
        }
        return this;
    }

    public TypedQuery<T> build() {
        if (this.orderBy != null) {
            this.queryString.append(" ORDER BY ").append(this.orderBy);
        }
        TypedQuery<T> query = em.createQuery(this.queryString.toString(), this.objectClass);
        this.params.forEach(query::setParameter);
        if (this.offset != null) {
            query.setFirstResult(this.offset);
        }
        if (this.limit != null) {
            query.setMaxResults(this.limit);
        }
        return query;
    }
}
