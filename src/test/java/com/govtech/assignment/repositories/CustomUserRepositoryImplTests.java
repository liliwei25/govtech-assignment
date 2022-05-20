package com.govtech.assignment.repositories;

import com.govtech.assignment.dtos.UserGetRequest;
import com.govtech.assignment.enums.SortKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class CustomUserRepositoryImplTests {
    EntityManager em;
    TypedQuery query;
    CustomUserRepositoryImpl repo = new CustomUserRepositoryImpl();

    @BeforeEach
    public void setUp () {
        em = Mockito.mock(EntityManager.class);
        query = Mockito.mock(TypedQuery.class);
        Mockito.when(em.createQuery(Mockito.anyString(), Mockito.any())).thenReturn(query);
        ReflectionTestUtils.setField(repo, "em", em);
    }

    @Test
    void testFindUsersFromRequest() {
        UserGetRequest request = new UserGetRequest();
        request.setLimit(100);
        request.setMax(100.);
        request.setMin(1.);
        request.setOffset(0);
        request.setSort(SortKey.NAME);
        repo.findUsersFromRequest(request);
        Mockito.verify(query, Mockito.times(1)).getResultList();
    }

    @Test
    void testFindUsersFromPartialRequest() {
        UserGetRequest request = new UserGetRequest();
        request.setLimit(100);
        request.setMax(100.);
        request.setSort(SortKey.NAME);
        repo.findUsersFromRequest(request);
        Mockito.verify(query, Mockito.times(1)).getResultList();
    }

    @Test
    void testFindUsersFromDefaultRequest() {
        UserGetRequest request = new UserGetRequest();
        repo.findUsersFromRequest(request);
        Mockito.verify(query, Mockito.times(1)).getResultList();
    }
}
