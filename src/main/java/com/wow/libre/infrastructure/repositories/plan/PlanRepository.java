package com.wow.libre.infrastructure.repositories.plan;

import com.wow.libre.infrastructure.entities.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface PlanRepository extends CrudRepository<PlanEntity, Long> {

    Optional<PlanEntity> findByStatusIsTrue();
}
