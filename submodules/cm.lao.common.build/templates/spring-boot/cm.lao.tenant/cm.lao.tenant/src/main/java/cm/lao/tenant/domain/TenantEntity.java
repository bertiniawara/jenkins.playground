package cm.lao.tenant.domain;

import cm.lao.common.microservice.common.domain.Entity;

import java.io.Serializable;

public interface TenantEntity<T extends Serializable> extends Entity<T> {
}
