package com.fime.ratemyprofs.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "professors")
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "professorid")
    private Integer professorId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "title", length = 50)
    private String title;

    @Column(name = "departmentname")
    private String departmentName;

    @Column(name = "photourl", length = 2083)
    private String photoUrl;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "professoruniversities",
        joinColumns = @JoinColumn(name = "professorid"),
        inverseJoinColumns = @JoinColumn(name = "universityid")
    )
    @Builder.Default
    private Set<University> universities = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "professorsubjects",
        joinColumns = @JoinColumn(name = "professorid"),
        inverseJoinColumns = @JoinColumn(name = "subjectid")
    )
    @Builder.Default
    private Set<Subject> subjects = new HashSet<>();
}
