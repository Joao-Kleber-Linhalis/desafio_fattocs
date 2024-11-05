package com.fattocs.back_end.desafio.repositories;

import com.fattocs.back_end.desafio.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

    @Query("SELECT COALESCE(MAX(t.presentationOrder), 0) FROM Task t")
    Long findMaxPresentationOrder();

    @Modifying
    @Transactional
    @Query("UPDATE Task t SET t.presentationOrder = t.presentationOrder - 1 WHERE t.presentationOrder > :deletedOrder")
    void updatePresentationOrder(Long deletedOrder);

    @Modifying
    @Transactional
    @Query("UPDATE Task t SET t.presentationOrder = t.presentationOrder + 1 " +
            "WHERE t.presentationOrder >= :newOrder AND t.presentationOrder < :currentOrder")
    void incrementOrderInRange(@Param("newOrder") Long newOrder, @Param("currentOrder") Long currentOrder);

    @Modifying
    @Transactional
    @Query("UPDATE Task t SET t.presentationOrder = t.presentationOrder - 1 " +
            "WHERE t.presentationOrder > :currentOrder AND t.presentationOrder <= :newOrder")
    void decrementOrderInRange(@Param("currentOrder") Long currentOrder, @Param("newOrder") Long newOrder);
}
