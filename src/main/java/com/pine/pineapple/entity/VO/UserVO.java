package com.pine.pineapple.entity.VO;

import com.pine.pineapple.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserVO extends User {
    private String token;
}
