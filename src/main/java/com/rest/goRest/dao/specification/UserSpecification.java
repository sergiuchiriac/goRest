package com.rest.goRest.dao.specification;

import com.rest.goRest.dao.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
@Data
public class UserSpecification {
    private static String statusString = "active";

    public static Specification<UserEntity> isStatus(Boolean status) {

        if (!status) {
            statusString = "inactive";
        }
        return (root, query, builder) -> builder.equal(root.get("status"), statusString);
    }
}
