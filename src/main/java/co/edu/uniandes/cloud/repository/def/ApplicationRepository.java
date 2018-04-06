package co.edu.uniandes.cloud.repository.def;

import co.edu.uniandes.cloud.domain.Application;
import co.edu.uniandes.cloud.domain.Contest;
import co.edu.uniandes.cloud.domain.enumeration.ApplicationState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ApplicationRepository extends CrudRepository<Application, String> {
    Page<Application> findByStatusAndContest(Pageable pageable, ApplicationState applicationState, Contest contest);

    List<Application> findByStatus(ApplicationState status);

    Page<Application> findByContest(Pageable pageable, Contest contest);

}
