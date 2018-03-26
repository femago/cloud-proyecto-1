package co.edu.uniandes.cloud.repository.def;

import co.edu.uniandes.cloud.domain.Contest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface ContestRepository extends CrudRepository<Contest, String> {
    Page<Contest> findByUser_Login(Pageable pageable, String userLogin);

    Contest findByUuid(String uuid);
}
