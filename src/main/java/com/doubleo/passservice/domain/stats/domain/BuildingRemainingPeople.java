package com.doubleo.passservice.domain.stats.domain;

import com.doubleo.passservice.domain.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "building_remaining_people")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BuildingRemainingPeople extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String buildingCode;

    private Long remainingPeopleNum;
}
