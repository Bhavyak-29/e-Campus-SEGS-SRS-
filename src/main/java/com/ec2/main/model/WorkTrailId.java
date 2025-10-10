package com.ec2.main.model;

import java.io.Serializable;
import java.util.Objects;

public class WorkTrailId implements Serializable {
    private Long work_id;
    private Long node_number;
    private Long interation_number;

    public WorkTrailId() {}

    public WorkTrailId(Long work_id, Long node_number, Long interation_number) {
        this.work_id = work_id;
        this.node_number = node_number;
        this.interation_number = interation_number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkTrailId)) return false;
        WorkTrailId that = (WorkTrailId) o;
        return Objects.equals(work_id, that.work_id)
            && Objects.equals(node_number, that.node_number)
            && Objects.equals(interation_number, that.interation_number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(work_id, node_number, interation_number);
    }
}
