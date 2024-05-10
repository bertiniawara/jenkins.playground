package cm.lao.tenant.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "t_demo")
public class Demo extends TenantEntityBase<DemoId> {

    @Builder.Default
    @EmbeddedId
    @AttributeOverride(
            name = "value",
            column = @Column(name = "c_id")
    )
    private DemoId id = new DemoId();

    @Column(name = "c_name")
    private String name;
}
