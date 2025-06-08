package com.segs.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.segs.demo.model.Egcrstt1;
import com.segs.demo.model.Egcrstt1Id;

@Repository
public interface Egcrstt1Repository extends JpaRepository<Egcrstt1, Egcrstt1Id> {

    @Query("SELECT DISTINCT e.id.examtypeId FROM Egcrstt1 e WHERE e.id.tcrid = :tcrid")
    List<Long> findDistinctExamTypeIdsByTcrid(@Param("tcrid") Long tcrid);
}

