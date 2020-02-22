package com.myzuji.backend.domain.system;


import com.myzuji.backend.domain.base.BaseEntity;

import javax.persistence.*;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
@Entity
@Table(name = "bs_sys_log")
@Access(value = AccessType.FIELD)
public class SysLog extends BaseEntity {

    private static final long serialVersionUID = 7049717852199461280L;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "model")
    private String model;

    @Column(name = "success")
    private Boolean success;

    @Column(name = "remark")
    private String remark;

    public SysLog(Long userId, String model, Boolean success, String remark) {
        this.userId = userId;
        this.model = model;
        this.success = success;
        this.remark = remark;
    }

    public Long getUserId() {
        return userId;
    }

    public String getModel() {
        return model;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getRemark() {
        return remark;
    }
}
