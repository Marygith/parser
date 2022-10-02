package com.pet.parser.model;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DataRepository extends JpaRepository<DataEntity, Long> { }
