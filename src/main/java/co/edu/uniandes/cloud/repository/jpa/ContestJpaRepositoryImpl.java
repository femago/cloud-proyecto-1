package co.edu.uniandes.cloud.repository.jpa;

import co.edu.uniandes.cloud.domain.Contest;
import co.edu.uniandes.cloud.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ContestJpaRepositoryImpl {

    @Autowired
    private ContestJpaRepository repository;

    Page<Contest> findByUser(Pageable pageable, User user){
        return repository.findByUser_Login(pageable, user.getLogin());
    }
}
