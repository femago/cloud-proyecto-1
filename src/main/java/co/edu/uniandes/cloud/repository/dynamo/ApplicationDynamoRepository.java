package co.edu.uniandes.cloud.repository.dynamo;

import co.edu.uniandes.cloud.domain.Application;
import co.edu.uniandes.cloud.repository.def.ApplicationRepository;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;
import org.springframework.data.repository.PagingAndSortingRepository;

@EnableScan
@EnableScanCount
public interface ApplicationDynamoRepository extends PagingAndSortingRepository<Application, String>, ApplicationRepository {

}
