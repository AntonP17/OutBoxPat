package by.antohakon.outboxpattern.repository;

import by.antohakon.outboxpattern.entity.OutBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxRepository extends JpaRepository<OutBox, Long> {

}
