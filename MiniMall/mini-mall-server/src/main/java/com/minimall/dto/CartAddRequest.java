package com.minimall.dto;

import lombok.Data;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class CartAddRequest {
    @NotNull(message = "商品ID不能为空")
    private Integer productId;

    @Min(value = 1, message = "数量至少为1")
    private Integer quantity = 1;
}
