package com.ec2.main.repository;

import java.math.BigDecimal;
import java.util.List;

public interface CustomStudentSemesterResultRepository {
    List<Object[]> findSpiCpiListBySpecification(
        Long semesterId,
        String cpiOperator, BigDecimal cpiFromValue, BigDecimal cpiToValue,
        String spiOperator, BigDecimal spiFromValue, BigDecimal spiToValue,
        String condition,
        String orderBy, String orderType
    );
}