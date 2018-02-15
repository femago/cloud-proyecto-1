package co.edu.uniandes.cloud.repository;

import co.edu.uniandes.cloud.domain.Contest;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Contest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {

    @Query("select contest from Contest contest where contest.user.login = ?#{principal.username}")
    List<Contest> findByUserIsCurrentUser();

}
