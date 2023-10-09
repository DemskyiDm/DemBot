package org.BotSasSE.reactController.repository;

import org.BotSasSE.reactController.interfaces.WorkerProjection;
import org.BotSasSE.reactController.tableEntity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Integer> {
    List<Worker> findAll();

}
