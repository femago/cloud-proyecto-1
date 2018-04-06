package co.edu.uniandes.cloud.repository.jpa;

import co.edu.uniandes.cloud.domain.Contest;
import co.edu.uniandes.cloud.repository.def.ContestRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Contest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContestJpaRepository extends JpaRepository<Contest, String>, ContestRepository {

}
