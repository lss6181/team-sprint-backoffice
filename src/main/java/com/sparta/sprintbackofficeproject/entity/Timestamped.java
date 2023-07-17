package com.sparta.sprintbackofficeproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass  //상속한 엔티티 클래스가 createdAt과 modifiedAt을 인식할 수 있도록?
@EntityListeners(AuditingEntityListener.class) //Audit 기능을 넣어줌
public abstract class Timestamped {

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

//    @LastModifiedDate
//    @Column
//    @Temporal(TemporalType.TIMESTAMP)
//    private LocalDateTime modifiedAt;
}
