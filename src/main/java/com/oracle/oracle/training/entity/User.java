package com.oracle.oracle.training.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.oracle.oracle.training.entity.post.ResourcePost;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    @JsonIgnore
    private String password;

    @Column( nullable = false)
    private String mobileNo;


    @Lob
    private String profileImage;

    private Integer role;

    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.EAGER,orphanRemoval = true)
    @JoinColumn(name = "pc_fid", referencedColumnName = "id")
    private List<Address> addresses = new ArrayList<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user",cascade = CascadeType.ALL)
    private List<ResourcePost> resourcePostList;

    //contains comma seperated ids of the liked posts and saved posts
    @Column(length = 500)
    private String likedPostsList;

    @Column(length = 500)
    private String savedPostsList;


    @Override
    public String toString(){
        return "User : "+"( id: "+this.getId()+", "+" email: "+this.getEmail()+", firstName: "+this.getFirstName()+", lastName: "+this.getLastName()+", mobileNo: "+
                this.getMobileNo()+", profileImage : "+(profileImage!=null?profileImage.substring(0,10)+"...":"null")+" )";
    }
}
