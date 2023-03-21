package com.oracle.oracle.training.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "user_tbl",
        uniqueConstraints = @UniqueConstraint(
                name = "unique_email",
                columnNames = "email"
        )
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(
            name = "email",
            nullable = false
    )
    private String email;
    @Column( nullable = false)
    private String firstName;
    @Column( nullable = false)
    private String lastName;

    @Column( nullable = false)
    private String password;

    @Column( nullable = false)
    private String mobileNo;

    @Lob
    private String profileImage;

    private Integer role;

    @Override
    public String toString(){
        return "User : "+"( id: "+this.getId()+", "+" email: "+this.getEmail()+", firstName: "+this.getFirstName()+", lastName: "+this.getLastName()+", mobileNo: "+
                this.getMobileNo()+", profileImage : "+(profileImage!=null?profileImage.substring(0,10)+"...":"null")+" )";
    }
}
