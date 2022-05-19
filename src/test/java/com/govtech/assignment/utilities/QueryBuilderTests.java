package com.govtech.assignment.utilities;

import com.govtech.assignment.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class QueryBuilderTests {
    @Mock
    EntityManager em;

    String baseQuery = "select * from User";
    String sortKey = "test";
    String queryWithSortKey = baseQuery + " ORDER BY " + sortKey;
    Integer offset = 1;
    Integer limit = 1;

    TypedQuery query = Mockito.mock(TypedQuery.class);

    @Test
    void testCreateWithValidQueryString() {
        QueryBuilder<User> queryBuilder = new QueryBuilder<>("select * from User", em, User.class);
        assertThat(queryBuilder).isNotNull();
    }

    @Test
    void testCreateWithInvalidQueryString() {
        assertThrows(IllegalArgumentException.class, () -> new QueryBuilder<>(null, em, User.class));
        assertThrows(IllegalArgumentException.class, () -> new QueryBuilder<>("", em, User.class));
    }

    @Test
    void testSetValidOrderBy() {
        QueryBuilder<User> queryBuilder = new QueryBuilder<>(baseQuery, em, User.class);
        Mockito.when(em.createQuery(Mockito.anyString(), Mockito.any())).thenReturn(query);

        queryBuilder.setOrderBy(sortKey).build();
        Mockito.verify(em, Mockito.times(1)).createQuery(queryWithSortKey, User.class);
    }

    @Test
    void testSetInvalidOrderBy() {
        QueryBuilder<User> queryBuilder = new QueryBuilder<>(baseQuery, em, User.class);
        Mockito.when(em.createQuery(Mockito.anyString(), Mockito.any())).thenReturn(query);

        queryBuilder.setOrderBy(null).build();
        Mockito.verify(em, Mockito.times(1)).createQuery(baseQuery, User.class);

        queryBuilder.setOrderBy("").build();
        Mockito.verify(em, Mockito.times(2)).createQuery(baseQuery, User.class);
    }

    @Test
    void testSetValidOffset() {
        QueryBuilder<User> queryBuilder = new QueryBuilder<>(baseQuery, em, User.class);
        Mockito.when(em.createQuery(Mockito.anyString(), Mockito.any())).thenReturn(query);

        queryBuilder.setOffset(offset).build();
        Mockito.verify(em, Mockito.times(1)).createQuery(baseQuery, User.class);
        Mockito.verify(query, Mockito.times(1)).setFirstResult(offset);
    }

    @Test
    void testSetInvalidOffset() {
        QueryBuilder<User> queryBuilder = new QueryBuilder<>(baseQuery, em, User.class);
        Mockito.when(em.createQuery(Mockito.anyString(), Mockito.any())).thenReturn(query);

        queryBuilder.setOffset(null).build();
        Mockito.verify(em, Mockito.times(1)).createQuery(baseQuery, User.class);
        Mockito.verify(query, Mockito.never()).setFirstResult(Mockito.anyInt());

        queryBuilder.setOffset(-1).build();
        Mockito.verify(em, Mockito.times(2)).createQuery(baseQuery, User.class);
        Mockito.verify(query, Mockito.never()).setFirstResult(Mockito.anyInt());
    }

    @Test
    void testSetValidLimit() {
        QueryBuilder<User> queryBuilder = new QueryBuilder<>(baseQuery, em, User.class);
        Mockito.when(em.createQuery(Mockito.anyString(), Mockito.any())).thenReturn(query);

        queryBuilder.setLimit(limit).build();
        Mockito.verify(em, Mockito.times(1)).createQuery(baseQuery, User.class);
        Mockito.verify(query, Mockito.times(1)).setMaxResults(limit);
    }

    @Test
    void testSetInvalidLimit() {
        QueryBuilder<User> queryBuilder = new QueryBuilder<>(baseQuery, em, User.class);
        Mockito.when(em.createQuery(Mockito.anyString(), Mockito.any())).thenReturn(query);

        queryBuilder.setLimit(null).build();
        Mockito.verify(em, Mockito.times(1)).createQuery(baseQuery, User.class);
        Mockito.verify(query, Mockito.never()).setMaxResults(Mockito.anyInt());

        queryBuilder.setLimit(-1).build();
        Mockito.verify(em, Mockito.times(2)).createQuery(baseQuery, User.class);
        Mockito.verify(query, Mockito.never()).setMaxResults(Mockito.anyInt());
    }

    @Test
    void testSetValidParam() {
        QueryBuilder<User> queryBuilder = new QueryBuilder<>(baseQuery, em, User.class);
        Mockito.when(em.createQuery(Mockito.anyString(), Mockito.any())).thenReturn(query);

        queryBuilder.setParam(1, 1).build();
        Mockito.verify(em, Mockito.times(1)).createQuery(baseQuery, User.class);
        Mockito.verify(query, Mockito.times(1)).setParameter(1, 1);
    }

    @Test
    void testSetValidParams() {
        QueryBuilder<User> queryBuilder = new QueryBuilder<>(baseQuery, em, User.class);
        Mockito.when(em.createQuery(Mockito.anyString(), Mockito.any())).thenReturn(query);

        queryBuilder.setParam(1, 1)
                .setParam(2, 2).build();
        Mockito.verify(em, Mockito.times(1)).createQuery(baseQuery, User.class);
        Mockito.verify(query, Mockito.times(1)).setParameter(1, 1);
        Mockito.verify(query, Mockito.times(1)).setParameter(2, 2);
    }

    @Test
    void testSetInvalidParam() {
        QueryBuilder<User> queryBuilder = new QueryBuilder<>(baseQuery, em, User.class);
        Mockito.when(em.createQuery(Mockito.anyString(), Mockito.any())).thenReturn(query);

        queryBuilder.setParam(null, 1).build();
        Mockito.verify(em, Mockito.times(1)).createQuery(baseQuery, User.class);
        Mockito.verify(query, Mockito.never()).setParameter(Mockito.anyInt(), Mockito.any());

        queryBuilder.setParam(0, 1).build();
        Mockito.verify(em, Mockito.times(2)).createQuery(baseQuery, User.class);
        Mockito.verify(query, Mockito.never()).setParameter(Mockito.anyInt(), Mockito.any());

        queryBuilder.setParam(1, null).build();
        Mockito.verify(em, Mockito.times(3)).createQuery(baseQuery, User.class);
        Mockito.verify(query, Mockito.never()).setParameter(Mockito.anyInt(), Mockito.any());
    }

    @Test
    void testSetMultiple() {
        QueryBuilder<User> queryBuilder = new QueryBuilder<>(baseQuery, em, User.class);
        Mockito.when(em.createQuery(Mockito.anyString(), Mockito.any())).thenReturn(query);

        queryBuilder.setOrderBy(sortKey)
                .setLimit(limit)
                .setOffset(offset)
                .setParam(1, 1)
                .setParam(2, 2).build();
        Mockito.verify(em, Mockito.times(1)).createQuery(queryWithSortKey, User.class);
        Mockito.verify(query, Mockito.times(1)).setParameter(1, 1);
        Mockito.verify(query, Mockito.times(1)).setParameter(2, 2);
        Mockito.verify(query, Mockito.times(1)).setFirstResult(offset);
        Mockito.verify(query, Mockito.times(1)).setMaxResults(limit);
    }
}
