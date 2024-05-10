package cm.lao.tenant.repository;

import cm.lao.tenant.domain.Demo;
import cm.lao.tenant.domain.DemoId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class DemoRepositoryTest extends TenantDataTest {
    private final String name = "demo_crud_name";
    @Autowired
    DemoRepository objectUnderTest;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void crudTest() {
        final var id = createTest();
        findByIdTest(id);
        findAllTest(id);
        updateTest(id);
        deleteTest(id);
    }

    private void deleteTest(DemoId id) {
        objectUnderTest.deleteById(id);
        assertThat(objectUnderTest.findById(id)).isEmpty();
    }

    private void updateTest(DemoId id) {
        final var demo = objectUnderTest.findById(id).orElseThrow();
        demo.setName(name + "_1");
        objectUnderTest.saveAndFlush(demo);

        assertThat(objectUnderTest.findById(id))
                .isNotEmpty()
                .hasValueSatisfying(
                        it -> assertThat(it)
                                .returns(name + "_1", Demo::getName)
                );
    }

    private void findAllTest(DemoId id) {
        final String deletedAccountId = "4d1503ba-312b-40c3-aa0e-8f0736b0fc49";
        final String notDeletedAccountId = "d91722a8-8e47-43ae-8032-49affbb68065";

        assertThat(jdbcTemplate.queryForList("SELECT * FROM t_demo"))
                .map(stringObjectMap -> stringObjectMap.get("c_id"))
                .contains(
                        notDeletedAccountId,
                        deletedAccountId,
                        id.getValue()
                );

        assertThat(objectUnderTest.findAll())
                .hasSizeGreaterThanOrEqualTo(2)
                .map(Demo::getId)
                .doesNotContain(new DemoId(deletedAccountId))
                .contains(new DemoId(notDeletedAccountId), id);
    }

    private void findByIdTest(DemoId id) {
        Optional<Demo> found = objectUnderTest.findById(id);
        assertThat(found)
                .isNotEmpty()
                .hasValueSatisfying(
                        demo -> assertThat(demo)
                                .returns(name, Demo::getName)
                );
    }

    private DemoId createTest() {
        var demo = Demo.builder()
                .name(name)
                .build();

        var savedItem = objectUnderTest.saveAndFlush(demo);
        assertThat(savedItem).isNotNull();
        assertThat(savedItem.getId()).isNotNull();
        return savedItem.getId();
    }


}