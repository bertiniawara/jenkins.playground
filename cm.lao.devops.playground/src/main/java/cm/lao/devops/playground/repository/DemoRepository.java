package cm.lao.devops.playground.repository;

import cm.lao.devops.playground.domain.Demo;
import cm.lao.devops.playground.domain.DemoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemoRepository extends JpaRepository<Demo, DemoId> {
}
