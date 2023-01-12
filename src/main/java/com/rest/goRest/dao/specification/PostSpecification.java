package com.rest.goRest.dao.specification;

import com.rest.goRest.dao.entity.PostEntity;
import com.rest.goRest.dao.entity.UserEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
public class PostSpecification implements Specification<PostEntity> {
    private String bodyText;
    private String titleText;
    private String userName;

    @Override
    public Predicate toPredicate(Root<PostEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        if (bodyText != null) {
            predicates.add(getBodyPredicate(root, cb, bodyText));
        }
        if (titleText != null) {
            predicates.add(getTitlePredicate(root, cb, titleText));
        }
        if (userName != null) {
            predicates.add(getNamePredicate(root, cb, userName));
        }
        return cb.and(predicates.toArray(Predicate[]::new));
    }

    private Predicate getTitlePredicate(Root<PostEntity> root, CriteriaBuilder cb, String title) {
        return cb.like(cb.function("jsonb_extract_path_text", String.class, root.get("data"), cb.literal("title")), title + '%');
    }

    private Predicate getBodyPredicate(Root<PostEntity> root, CriteriaBuilder cb, String body) {
        return cb.like(cb.function("jsonb_extract_path_text", String.class, root.get("data"), cb.literal("body")), body + '%');
    }

    private Predicate getNamePredicate(Root<PostEntity> root, CriteriaBuilder cb, String name) {
        Join<PostEntity, UserEntity> userJoin = root.join("user", JoinType.INNER);
        Expression<String> nameExpression = userJoin.get("name");
        return cb.equal(nameExpression, name);
    }
}
