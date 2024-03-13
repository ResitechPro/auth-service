package com.taskflow.domain.entity;

import jakarta.persistence.*;
import lombok.*;


@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "permissions")
public class Permission {
    @Id
    private String id;
    private String subject; //entity or resource
    private String action; //create, read, update, delete
    private String description;
}
