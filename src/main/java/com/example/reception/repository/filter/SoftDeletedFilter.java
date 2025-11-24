package com.example.reception.repository.filter;

import jakarta.persistence.EntityManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class SoftDeletedFilter {

    private final EntityManager entityManager;

    @Getter
    @Setter
    @Accessors(fluent = true)
    private String filterName;

    @Getter
    @Setter
    @Accessors(fluent = true)
    private String filterParameter;

    public <T> T apply(Supplier<T> supplier, boolean deleted) {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter(filterName).setParameter(filterParameter, deleted);
        var result = supplier.get();
        session.disableFilter(filterName);
        return result;
    }
}
