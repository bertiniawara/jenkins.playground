package cm.lao.tenant.repository;

import cm.lao.tenant.domain.Demo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemoRepository extends JpaRepository<Demo, DemoId> {
}
