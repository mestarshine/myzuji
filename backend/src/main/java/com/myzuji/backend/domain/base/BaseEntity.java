package com.myzuji.backend.domain.base;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
@Entity
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 5479867594878656479L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime = LocalDateTime.now();

    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime = LocalDateTime.now();

    @Column(name = "version")
    private Long version = 0L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public Long getVersion() {
        return version;
    }
}
