package com.takirahal.srfgroup.modules.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOneSignalDTO implements Serializable {

    private Long id;
    private String idOneSignal;
    private String sourceConnectedDevice;
    private Instant registerDate;
    private UserDTO user;
}
