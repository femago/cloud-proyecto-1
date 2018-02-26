package co.edu.uniandes.cloud.repository;

import co.edu.uniandes.cloud.domain.Contest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Contest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {

    @Query("select contest from Contest contest where contest.user.login = ?#{principal.username}")
    Page<Contest> findByUserIsCurrentUser(Pageable pageable);

    Contest findByUuid(String uuid);
}
