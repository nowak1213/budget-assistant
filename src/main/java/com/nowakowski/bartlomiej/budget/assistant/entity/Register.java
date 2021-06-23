package com.nowakowski.bartlomiej.budget.assistant.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

import java.util.Objects;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Register {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RegisterType type;

    private Double value;

    public Register(RegisterType type, Double value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Register register = (Register) o;
        return Objects.equals(id, register.id) && type == register.type && Objects.equals(value, register.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, value);
    }
}
