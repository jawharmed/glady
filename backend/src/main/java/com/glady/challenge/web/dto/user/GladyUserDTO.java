package com.glady.challenge.web.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GladyUserDTO implements Serializable {

    private Long id;

    @NotEmpty
    private String username;

    @NotNull
    private Long companyId;

}
