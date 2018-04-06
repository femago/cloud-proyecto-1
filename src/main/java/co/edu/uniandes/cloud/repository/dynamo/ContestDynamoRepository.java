package co.edu.uniandes.cloud.repository.dynamo;

import co.edu.uniandes.cloud.domain.Contest;
import co.edu.uniandes.cloud.repository.ContestRepository;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;
import org.springframework.data.repository.PagingAndSortingRepository;

@EnableScan
@EnableScanCount
public interface ContestDynamoRepository extends PagingAndSortingRepository<Contest, String>, ContestRepository {
}
