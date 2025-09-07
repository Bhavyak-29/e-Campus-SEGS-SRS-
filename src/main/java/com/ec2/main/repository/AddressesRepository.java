package com.ec2.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ec2.main.model.Addresses;

@Repository
public interface AddressesRepository extends JpaRepository<Addresses, Long> {
    @Query("SELECT a FROM Addresses a WHERE a.adrid IN :addressIds " +
           "ORDER BY CASE WHEN a.adrid = :currAdrId THEN 1 WHEN a.adrid = :prmtAdrId THEN 2 ELSE 3 END")
    List<Addresses> findAddressesByIdsWithCustomOrder(
        @Param("addressIds") List<Long> addressIds,
        @Param("currAdrId") Long currAdrId,
        @Param("prmtAdrId") Long prmtAdrId
    );
}
