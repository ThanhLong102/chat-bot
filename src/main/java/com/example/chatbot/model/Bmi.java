package com.example.chatbot.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Bmi {
    @Id
    private Long id;

    private String name;

    private Float startValue;

    private Float endValue;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Bmi bmi = (Bmi) o;
        return id != null && Objects.equals(id, bmi.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
