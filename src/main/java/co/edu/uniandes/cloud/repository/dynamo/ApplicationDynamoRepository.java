package co.edu.uniandes.cloud.repository.dynamo;

import co.edu.uniandes.cloud.domain.Application;
import co.edu.uniandes.cloud.repository.def.ApplicationRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ApplicationDynamoRepository extends PagingAndSortingRepository<Application, String>, ApplicationRepository {

}
