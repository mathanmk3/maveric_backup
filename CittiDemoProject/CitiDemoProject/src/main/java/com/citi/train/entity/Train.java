package com.citi.train.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trainid")
    private Long trainId;
    @Column(name = "tainname")
    private String tainName;
    @Column(name = "trainnumber")
    private Long trainNumber;
    @Column(name = "startpoint")
    private String startPoint;
    @Column(name = "endpoint")
    private String endPoint;
    @Column(name = "startdateandtime")
    private String startDateAndTime;
    @Column(name = "destinationdateandtime")
    private String destinationDateAndTime;

}
