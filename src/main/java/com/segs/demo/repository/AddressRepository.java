package com.segs.demo.repository;

import com.segs.demo.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("SELECT a FROM Address a WHERE a.adrid IN (:adrIds) ORDER BY CASE WHEN a.adrid = :curr THEN 1 WHEN a.adrid = :prmt THEN 2 END")
    List<Address> findAddressesByPriority(@Param("adrIds") List<Long> adrIds, @Param("curr") Long currAdrId, @Param("prmt") Long prmtAdrId);
}