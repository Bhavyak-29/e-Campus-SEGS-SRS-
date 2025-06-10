package com.segs.demo.repository;

import com.segs.demo.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    /**
     * Finds a list of addresses by their IDs and sorts them with a specific priority:
     * 1. Current Address
     * 2. Permanent Address
     * 3. All other addresses (e.g., Emergency)
     * This single method covers all your address sorting needs.
     */
    @Query("SELECT a FROM Address a WHERE a.adrid IN :addressIds " +
           "ORDER BY CASE WHEN a.adrid = :currAdrId THEN 1 WHEN a.adrid = :prmtAdrId THEN 2 ELSE 3 END")
    List<Address> findAddressesByIdsWithCustomOrder(
        @Param("addressIds") List<Long> addressIds,
        @Param("currAdrId") Long currAdrId,
        @Param("prmtAdrId") Long prmtAdrId
    );
}