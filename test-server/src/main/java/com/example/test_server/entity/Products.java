package com.example.test_server.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Products extends BasicEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long pno;

  @Column(length = 100, nullable = false)
  private String pname;

  @Column(length = 1500, nullable = false)
  private String pmodel;

  @Column(length = 50, nullable = false)
  private String pmaker;

  @Column(length = 50, nullable = false)
  private Integer price;

  public void changePname(String pname) {
    this.pname = pname;
  }
  public void changePmodel(String pmodel) {this.pmodel = pmodel;}
  public void changePmaker(String pmaker) {
    this.pmaker = pmaker;
  }

}