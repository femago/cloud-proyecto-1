package co.edu.uniandes.cloud.repository;

import co.edu.uniandes.cloud.domain.Contest;
import co.edu.uniandes.cloud.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ContestRepository extends CrudRepository<Contest, String>, PagingAndSortingRepository<Contest, String> {
    Page<Contest> findByUser(Pageable pageable, User user);

    Contest findByUuid(String uuid);
}
