package cm.lao.tenant.repository;

import cm.lao.common.microservice.repository.DataTestWithMultiTenancySupport;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.test.mock.mockito.MockBean;

abstract class TenantDataTest extends DataTestWithMultiTenancySupport {
    @MockBean
    protected HazelcastInstance hazelcastInstance;
}
