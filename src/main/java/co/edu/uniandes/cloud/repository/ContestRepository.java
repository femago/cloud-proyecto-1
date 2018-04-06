package co.edu.uniandes.cloud.repository;

import co.edu.uniandes.cloud.domain.Contest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ContestRepository extends CrudRepository<Contest, String>, PagingAndSortingRepository<Contest, String> {
    Page<Contest> findByUser_Login(Pageable pageable, String userLogin);

    Contest findByUuid(String uuid);
}
