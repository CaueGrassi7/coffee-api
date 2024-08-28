package com.example.products_api.domain.product;

import org.antlr.v4.runtime.misc.NotNull;

public record ProductRequestDTO(

        String name,

        @NotNull
        Integer price
) {
}
