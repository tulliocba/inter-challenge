package com.gitlab.tulliocba.persistencejpa.uniquenumber;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "unique_number")
@EqualsAndHashCode(of = "id")
public class UniqueNumberEntity {

    @Id
    @GeneratedValue
    private Long id;

    private int concatQuantity;

    private String inputNumber;

    private int uniqueNumberResult;
}
