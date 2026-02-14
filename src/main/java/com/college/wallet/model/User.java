package com.college.wallet.model;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import  jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Entity
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id//ids are necessary for entity
   @GeneratedValue(strategy=GenerationType.IDENTITY)//ids are iterated 
   @Column(unique=true)
   private UUID  id;
   @NotBlank(message="Enter username")
   @Column(unique=true,nullable=false)
    private String  username;
@NotBlank(message="Enter email")
@Column(unique=true,nullable=false)
@Email(message="Enter valid fromat email")
    private  String email;
    @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message="Enter password")
    @Column(nullable=false)
    private  String password;
    private String refreshToken;
    @Column(updatable=false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @PrePersist
    protected void oncreate(){
        createdAt=LocalDateTime.now();
        updatedAt=LocalDateTime.now();

    }
    @PreUpdate
    protected void onupdate(){
        updatedAt=LocalDateTime.now();
    }

}


