package co.edu.uniandes.cloud.repository.jpa;

import co.edu.uniandes.cloud.domain.Application;
import co.edu.uniandes.cloud.repository.def.ApplicationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the Application entity.
 */
@Repository
public interface ApplicationJpaRepository extends JpaRepository<Application, String>, ApplicationRepository {

}
