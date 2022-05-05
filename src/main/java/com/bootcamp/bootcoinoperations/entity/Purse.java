package com.bootcamp.bootcoinoperations.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Purse implements Serializable {

    @Id
    private String id;
    private String typeDocument;
    private String documentNumber;
    private String mobilePhone;
    private String email;
    private String imei;
    private short registrationStatus;
    private Date insertionDate;
    private String fk_insertionUser;
    private String insertionTerminal;

}
