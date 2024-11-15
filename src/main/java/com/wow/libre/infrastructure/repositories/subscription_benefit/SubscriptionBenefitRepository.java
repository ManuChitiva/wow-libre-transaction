package com.wow.libre.infrastructure.repositories.subscription_benefit;

import com.wow.libre.infrastructure.entities.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface SubscriptionBenefitRepository extends CrudRepository<SubscriptionBenefitEntity, Long> {
    Optional<SubscriptionBenefitEntity> findByUserIdAndBenefitId(Long userId, Long benefitId);
}
