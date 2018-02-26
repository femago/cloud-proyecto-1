package co.edu.uniandes.cloud.repository;

import co.edu.uniandes.cloud.domain.Application;
import co.edu.uniandes.cloud.domain.enumeration.ApplicationState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data JPA repository for the Application entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Page<Application> findByStatusAndContest_Id(Pageable pageable, ApplicationState applicationState, Long contestId);

    Page<Application> findByContest_Id(Pageable pageable, Long contestId);

    List<Application> findByStatusEquals(ApplicationState status);
}
