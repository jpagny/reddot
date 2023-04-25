package com.elysium.reddot.ms.user.infrastructure.outbound.persistence.repository;

import com.elysium.reddot.ms.user.infrastructure.outbound.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
}