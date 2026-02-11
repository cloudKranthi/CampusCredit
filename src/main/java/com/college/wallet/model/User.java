package com.college.wallet.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
@Data
@Entity
@Table(name="users")
public class User {
    @Id//ids are necessary for entity
   @GeneratedValue(strategy=GenerationType.IDENTITY)//ids are iterated 
   private Long  id;
    private String  username;
    private  String email;
}

