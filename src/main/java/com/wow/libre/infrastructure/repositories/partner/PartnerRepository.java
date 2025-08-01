package com.wow.libre.infrastructure.repositories.partner;

import com.wow.libre.infrastructure.entities.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface PartnerRepository extends CrudRepository<PartnerEntity, Long> {
    Optional<PartnerEntity> findByRealmId(Long realmId);
}
