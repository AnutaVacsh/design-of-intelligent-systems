package ru.lama.expertCookingSystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.lama.expertCookingSystem.entity.DietType;

@Repository
public interface DietTypeRepository extends JpaRepository<DietType, Long> {

}
