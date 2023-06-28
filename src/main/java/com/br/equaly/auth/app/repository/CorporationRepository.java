package com.br.equaly.auth.app.repository;

import com.br.equaly.auth.app.model.entity.Corporation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorporationRepository extends CrudRepository<Corporation, Long> {

    @Query(
            "SELECT value FROM CorporationKey corporationKey " +
                    "WHERE corporationKey.isActive = TRUE " +
                    "AND corporationKey.corporation.id = :corporationId " +
                    "ORDER BY RANDOM() LIMIT 1"
    )
    String getValidAppkey(Long corporationId);


    @Query(
            "SELECT isActive FROM CorporationKey corporationKey " +
                    "WHERE corporationKey.value = :appkey " +
                    "AND corporationKey.corporation.id = :corporationId "
    )
    String checkValidAppkey(Long corporationId, String appkey);



}
