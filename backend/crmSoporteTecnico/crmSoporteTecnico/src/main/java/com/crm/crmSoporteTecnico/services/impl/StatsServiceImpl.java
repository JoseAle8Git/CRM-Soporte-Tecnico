package com.crm.crmSoporteTecnico.services.impl;

import com.crm.crmSoporteTecnico.persistence.repositories.IncidenceRepository;
import com.crm.crmSoporteTecnico.services.IStatsService;
import com.crm.crmSoporteTecnico.services.models.dtos.TechnicianStatsDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatsServiceImpl implements IStatsService {

    private final IncidenceRepository incidenceRepository;

    public StatsServiceImpl(IncidenceRepository incidenceRepository) {
        this.incidenceRepository = incidenceRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TechnicianStatsDTO> getIncidenceCountsByTechnician() {

        // Llama al método JPQL que hace el GROUP BY en la base de datos.
        List<Object[]> results = incidenceRepository.countIncidenceByTechnicianName();

        // Mapear los resultados (Object[]) al DTO final.
        return results.stream()
                .map(result -> new TechnicianStatsDTO(
                    (String) result[0],
                    (Long) result[1]
                ))
                .collect(Collectors.toList());

    }

}
