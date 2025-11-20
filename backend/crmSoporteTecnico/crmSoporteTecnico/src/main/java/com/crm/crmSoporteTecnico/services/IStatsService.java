package com.crm.crmSoporteTecnico.services;

import com.crm.crmSoporteTecnico.services.models.dtos.TechnicianStatsDTO;

import java.util.List;

public interface IStatsService {

    List<TechnicianStatsDTO> getIncidenceCountsByTechnician();

}
