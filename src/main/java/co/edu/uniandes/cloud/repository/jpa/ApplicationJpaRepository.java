package co.edu.uniandes.cloud.repository.jpa;

import co.edu.uniandes.cloud.config.Constants;
import co.edu.uniandes.cloud.domain.Application;
import co.edu.uniandes.cloud.repository.ApplicationRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the Application entity.
 */
@Repository
@Profile(Constants.CLOICE_PROFILE_NO_DYNAMODB)
public interface ApplicationJpaRepository extends JpaRepository<Application, String>, ApplicationRepository {

}
