package com.github.kay.mmall.domain.account;

import com.github.kay.mmall.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Setter
@Getter
@Entity
public class Address extends BaseEntity {
    private Integer userId;

    @NotEmpty(message = "姓名不允许为空")
    private String receiverName;

    @Pattern(regexp = "1\\d{10}", message = "手机号格式不正确")
    private String phone;

    @NotEmpty(message = "省份不允许为空")
    private String province;

    @NotEmpty(message = "城市不允许为空")
    private String city;

    private String district;

    @NotEmpty(message = "地址不允许为空")
    private String details;

    private String zip;

}
