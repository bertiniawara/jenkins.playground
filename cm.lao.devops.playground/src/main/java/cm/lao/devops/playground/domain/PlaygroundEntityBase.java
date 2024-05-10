package cm.lao.devops.playground.domain;

import cm.lao.common.microservice.common.domain.MultiTenantEntity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.MappedSuperclass;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class PlaygroundEntityBase<T extends Serializable>
        extends MultiTenantEntity<T>
        implements PlaygroundEntity<T> {
}
