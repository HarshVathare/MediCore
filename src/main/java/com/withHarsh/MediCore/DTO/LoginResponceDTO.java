package com.withHarsh.MediCore.DTO;

import com.withHarsh.MediCore.Entity.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponceDTO {

    private String accessToken;
    private String refreshToken;
    private Long user_Id;
    private String email;


}
