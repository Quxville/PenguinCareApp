package model;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@Entity( name = "Penguins")

public class Penguin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String penguinName;
    double penguinAge;

    @ManyToOne
    private User user;

    @Override
    public String toString() {
        return "Penguin{" +
                "id=" + id +
                ", penguinName='" + penguinName + '\'' +
                ", penguinAge=" + penguinAge +
                ", user=" + user +
                '}';
    }
}
