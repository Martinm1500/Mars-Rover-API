package com.martinm1500.marsrover.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@Entity
public class Obstacle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "map_id")
    private Map map;

    @NotNull
    private int x;

    @NotNull
    private int y;

    public Obstacle(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Obstacle(){}

    public boolean isAt(int x, int y){
        return this.x == x && this.y == y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Obstacle obstacle = (Obstacle) o;
        return x == obstacle.x && y == obstacle.y && Objects.equals(id, obstacle.id) && Objects.equals(map, obstacle.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, x, y);
    }

    @Override
    public String toString() {
        return "Obstacle{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
