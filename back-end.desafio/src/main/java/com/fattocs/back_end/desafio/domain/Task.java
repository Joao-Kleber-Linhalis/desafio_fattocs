package com.fattocs.back_end.desafio.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "tasks")
public class Task implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 180, unique = true)
    private String name;

    @Column(nullable = false)
    private Double cost;
    @Column(name = "limit_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date limitDate;

    @Column(name = "presentation_order", nullable = false)
    private Long presentationOrder;

    public Task() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

        Task task = (Task) o;

        if (!Objects.equals(id, task.id)) return false;
        if (!Objects.equals(name, task.name)) return false;
        if (!Objects.equals(cost, task.cost)) return false;
        if (!Objects.equals(limitDate, task.limitDate)) return false;
        return Objects.equals(presentationOrder, task.presentationOrder);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        result = 31 * result + (limitDate != null ? limitDate.hashCode() : 0);
        result = 31 * result + (presentationOrder != null ? presentationOrder.hashCode() : 0);
        return result;
    }
}
