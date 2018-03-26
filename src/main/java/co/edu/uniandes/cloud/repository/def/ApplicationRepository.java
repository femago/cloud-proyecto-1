package co.edu.uniandes.cloud.repository.def;

import co.edu.uniandes.cloud.domain.Application;
import co.edu.uniandes.cloud.domain.enumeration.ApplicationState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ApplicationRepository extends CrudRepository<Application, String> {
    Page<Application> findByStatusAndContest_Id(Pageable pageable, ApplicationState applicationState, String contestId);

    Page<Application> findByContest_Id(Pageable pageable, String contestId);

    List<Application> findByStatusEquals(ApplicationState status);

}
