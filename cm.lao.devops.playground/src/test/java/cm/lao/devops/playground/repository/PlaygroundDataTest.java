package cm.lao.devops.playground.repository;

import cm.lao.common.microservice.repository.DataTestWithMultiTenancySupport;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.test.mock.mockito.MockBean;

abstract class PlaygroundDataTest extends DataTestWithMultiTenancySupport {
    @MockBean
    protected HazelcastInstance hazelcastInstance;
}
