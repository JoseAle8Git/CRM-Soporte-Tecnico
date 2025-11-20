package com.crm.crmSoporteTecnico.services.impl;

import com.crm.crmSoporteTecnico.persistence.entities.AppUser;
import com.crm.crmSoporteTecnico.persistence.entities.Incidence;
import com.crm.crmSoporteTecnico.persistence.enums.IncidenceStatus;
import com.crm.crmSoporteTecnico.persistence.repositories.IncidenceRepository;
import com.crm.crmSoporteTecnico.persistence.repositories.UserRepository;
import com.crm.crmSoporteTecnico.services.IIncidenceService;
import com.crm.crmSoporteTecnico.services.INotificationService;
import com.crm.crmSoporteTecnico.services.models.dtos.IncidenceAssignmentRequest;
import com.crm.crmSoporteTecnico.services.models.dtos.IncidenceDashboardDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IncidenceServiceImpl implements IIncidenceService {

    private final IncidenceRepository incidenceRepository;
    private final UserRepository userRepository;
    private final INotificationService notificationService;

    public IncidenceServiceImpl(IncidenceRepository incidenceRepository, UserRepository userRepository, INotificationService notificationService) {
        this.incidenceRepository = incidenceRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    /**
     * Retorna todas las incidencias para la tabla del Dashboard.
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<IncidenceDashboardDTO> findAllIncidencesForDashboard() {
        return incidenceRepository.findAll().stream()
                .map(IncidenceDashboardDTO::fromIncidence).collect(Collectors.toList());
    }

    /**
     * Asigna la incidencia y notifica al técnico.
     * @param request
     * @return
     */
    @Override
    @Transactional
    public IncidenceDashboardDTO assignTechnician(IncidenceAssignmentRequest request) {

        // Buscar incidencia y técnico.
        Incidence incidence = incidenceRepository.findById(request.incidenceId())
                .orElseThrow(() -> new IllegalArgumentException("Incidencia no encontrada."));
        AppUser technician = userRepository.findById(request.technicianId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        // Actualizar la incidencia.
        incidence.setTechnician(technician);
        incidence.setStatus(IncidenceStatus.IN_PROGRESS);
        incidence.setAssignmentDate(LocalDateTime.now());

        Incidence updatedIncidence = incidenceRepository.save(incidence);

        // Notificación al técnico de la incidencia.
        notificationService.notifyTechnicianAssignment(updatedIncidence);

        return IncidenceDashboardDTO.fromIncidence(updatedIncidence);

    }

}
