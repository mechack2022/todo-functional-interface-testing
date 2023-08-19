package com.fragile.todofunctionalinterface.entity;

import com.fragile.todofunctionalinterface.constants.Priority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name="tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private Priority priority;
    private boolean completed;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdatedDate;
    @JoinColumn(name="category_id")
    @ManyToOne
    private Category category;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
}
