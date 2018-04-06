package co.edu.uniandes.cloud.repository.jpa;

import co.edu.uniandes.cloud.config.Constants;
import co.edu.uniandes.cloud.domain.Contest;
import co.edu.uniandes.cloud.repository.ContestRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Contest entity.
 */
@Repository
@Profile(Constants.CLOICE_PROFILE_NO_DYNAMODB)
public interface ContestJpaRepository extends JpaRepository<Contest, String>, ContestRepository {

}
