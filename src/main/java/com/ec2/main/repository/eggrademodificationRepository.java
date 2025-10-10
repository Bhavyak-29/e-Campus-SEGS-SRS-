package com.ec2.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ec2.main.model.eggrademodification;

@Repository
public interface eggrademodificationRepository extends JpaRepository<eggrademodification, Long> {
    @Query("SELECT MAX(e.gmdid) FROM eggrademodification e")
    Long findMaxId();

    List<eggrademodification> findByGmdcreateby(String facultyUsername);

    List<eggrademodification> findByGmdcreatebyAndGmdtcrid(String gmdcreateby, Long gmdtcrid);

}
