package com.crm.crmSoporteTecnico.services.impl;

import com.crm.crmSoporteTecnico.persistence.entities.Client;
import com.crm.crmSoporteTecnico.persistence.repositories.ClientRepository;
import com.crm.crmSoporteTecnico.persistence.repositories.IncidenceRepository;
import com.crm.crmSoporteTecnico.services.IStatsService;
import com.crm.crmSoporteTecnico.services.models.dtos.PackageCountDTO;
import com.crm.crmSoporteTecnico.services.models.dtos.TechnicianStatsDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatsServiceImpl implements IStatsService {

    private static final Map<String, Double> PACKAGES_PRICE = Map.of(
            "Plan Esencial", 99.0,
            "Plan Profesional", 299.0,
            "Plan Corporativo", 499.0
    );

    private final IncidenceRepository incidenceRepository;
    private final ClientRepository clientRepository;

    public StatsServiceImpl(IncidenceRepository incidenceRepository,  ClientRepository clientRepository) {
        this.incidenceRepository = incidenceRepository;
        this.clientRepository = clientRepository;
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

    /**
     * Calcula el total de ingresos mensuales proyectados.
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Double getProjectMonthlyRevenue() {
        List<Client> activeClients = clientRepository.findByActive(true);

        return activeClients.stream()
                .mapToDouble(c -> PACKAGES_PRICE.getOrDefault(c.getServicePackage(), 0.0))
                .sum();
    }

    /**
     * Conteo de clientes activos agrupados por paquete.
     */
    @Override
    @Transactional(readOnly = true)
    public List<PackageCountDTO> getActiveClientCountsByPackage() {
        List<Client> activeClients = clientRepository.findByActive(true);
        Map<String, Long> counts = activeClients.stream()
                .collect(Collectors.groupingBy(Client::getServicePackage, Collectors.counting()));

        return counts.entrySet().stream()
                .map(entry -> new PackageCountDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

}
