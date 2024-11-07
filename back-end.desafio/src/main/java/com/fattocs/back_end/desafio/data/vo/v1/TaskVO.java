package com.fattocs.back_end.desafio.data.vo.v1;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@JsonPropertyOrder({"id", "name", "cost", "limitDate", "presentationOrder"})
public class TaskVO extends RepresentationModel<TaskVO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Mapping("id")
    @JsonProperty("id")
    private Long key;

    private String name;

    private Double cost;

    private Date limitDate;

    private Long presentationOrder;

    public TaskVO() {
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Date getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(Date limitDate) {
        this.limitDate = limitDate;
    }

    public Long getPresentationOrder() {
        return presentationOrder;
    }

    public void setPresentationOrder(Long presentationOrder) {
        this.presentationOrder = presentationOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TaskVO taskVO = (TaskVO) o;

        if (!Objects.equals(key, taskVO.key)) return false;
        if (!Objects.equals(name, taskVO.name)) return false;
        if (!Objects.equals(cost, taskVO.cost)) return false;
        if (!Objects.equals(limitDate, taskVO.limitDate)) return false;
        return Objects.equals(presentationOrder, taskVO.presentationOrder);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        result = 31 * result + (limitDate != null ? limitDate.hashCode() : 0);
        result = 31 * result + (presentationOrder != null ? presentationOrder.hashCode() : 0);
        return result;
    }
}
