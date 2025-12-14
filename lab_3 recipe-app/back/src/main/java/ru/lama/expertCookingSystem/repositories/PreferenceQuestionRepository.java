package ru.lama.expertCookingSystem.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.lama.expertCookingSystem.entity.PreferenceQuestion;

@Repository
public interface PreferenceQuestionRepository extends JpaRepository<PreferenceQuestion, Long> {

  List<PreferenceQuestion> findAllByOrderBySortOrderAsc();
}
