package com.enesincekara.organizationservice.repository;

import com.enesincekara.organizationservice.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    boolean existsOrganizationByOrganizationCode(String organizationCode);

    Optional<Organization> getOrganizationByOrganizationCode(String organizationCode);
}
