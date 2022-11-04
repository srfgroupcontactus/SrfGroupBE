package com.takirahal.srfgroup.modules.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileFavoriteUserDTO {
    private UserDTO user;
    private boolean isMyFavoriteUser;
}
