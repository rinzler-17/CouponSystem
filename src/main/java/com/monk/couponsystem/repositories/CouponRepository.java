package com.monk.couponsystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monk.couponsystem.models.CouponEntity;

@Repository
public interface CouponRepository extends JpaRepository<CouponEntity, Long> {
}
