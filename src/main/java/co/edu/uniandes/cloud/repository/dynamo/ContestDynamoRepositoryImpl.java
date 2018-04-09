package co.edu.uniandes.cloud.repository.dynamo;

import co.edu.uniandes.cloud.domain.Contest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ContestDynamoRepositoryImpl {

    @Autowired
    private ContestDynamoRepository repository;

    public Page<Contest> findAll(Pageable pageable) {
        return repository.findByEqkey("CONTEST", pageable);
    }
}
