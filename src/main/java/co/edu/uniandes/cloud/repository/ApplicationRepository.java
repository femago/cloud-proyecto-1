package co.edu.uniandes.cloud.repository;

import co.edu.uniandes.cloud.domain.Application;
import co.edu.uniandes.cloud.domain.Contest;
import co.edu.uniandes.cloud.domain.enumeration.ApplicationState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ApplicationRepository extends CrudRepository<Application, String>, PagingAndSortingRepository<Application, String> {
    Page<Application> findByStatusAndContest(Pageable pageable, ApplicationState applicationState, Contest contest);

    List<Application> findByStatus(ApplicationState status);

    Page<Application> findByContest(Pageable pageable, Contest contest);

}
