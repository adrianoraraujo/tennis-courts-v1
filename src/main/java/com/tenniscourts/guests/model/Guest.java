package com.tenniscourts.guests.model;

import com.tenniscourts.config.persistence.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Guest extends BaseEntity<Long> {

  @Column
  @NotNull
  private String name;

}
