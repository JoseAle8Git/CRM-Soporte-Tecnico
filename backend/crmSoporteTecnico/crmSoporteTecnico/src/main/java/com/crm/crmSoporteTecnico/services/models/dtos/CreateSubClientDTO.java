package com.crm.crmSoporteTecnico.services.models.dtos;

import java.math.BigDecimal;

public record CreateSubClientDTO(
        String name,
        BigDecimal billing,
        Boolean active
) {}