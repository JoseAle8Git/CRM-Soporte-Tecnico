package com.crm.crmSoporteTecnico.services.impl;

import com.crm.crmSoporteTecnico.persistence.entities.AppUser;
import com.crm.crmSoporteTecnico.persistence.entities.Client;
import com.crm.crmSoporteTecnico.persistence.entities.Incidence;
import com.crm.crmSoporteTecnico.persistence.enums.IncidencePriority;
import com.crm.crmSoporteTecnico.persistence.enums.IncidenceStatus;
import com.crm.crmSoporteTecnico.persistence.repositories.ClientRepository;
import com.crm.crmSoporteTecnico.persistence.repositories.IncidenceRepository;
import com.crm.crmSoporteTecnico.persistence.repositories.UserRepository;
import com.crm.crmSoporteTecnico.services.IIncidenceService;
import com.crm.crmSoporteTecnico.services.INotificationService;
import com.crm.crmSoporteTecnico.services.models.dtos.CreateIncidenceDTO;
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
    private final ClientRepository clientRepository;

    public IncidenceServiceImpl(IncidenceRepository incidenceRepository, UserRepository userRepository, INotificationService notificationService, ClientRepository clientRepository) {
        this.incidenceRepository = incidenceRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.clientRepository = clientRepository;
    }

    /**
     * Retorna todas las incidencias para la tabla del Dashboard.
     *
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
     *
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

    /**
     * Implementación de la creación de incidencia.
     */
    @Override
    @Transactional
    // AHORA RECIBIMOS EL USUARIO 'creatorUser' (El que está logueado)
    public IncidenceDashboardDTO createIncidence(CreateIncidenceDTO request, AppUser creatorUser) {

        if (creatorUser.getClient() == null) {
            throw new IllegalArgumentException("El usuario no tiene empresa asignada.");
        }

        IncidencePriority priority = IncidencePriority.valueOf(request.priority().toUpperCase());
        // 1. Crear la entidad Incidence
        Incidence newIncidence = new Incidence(
                null,
                request.title(),
                request.description(),
                priority,
                IncidenceStatus.PENDING,
                LocalDateTime.now(),
                null,
                creatorUser.getClient(),
                null,
                null
        );

        // Guardar
        Incidence savedIncidence = incidenceRepository.save(newIncidence);

        // 6. Notificar (Si lo tienes activado)
        notificationService.notifyManagerNewIncidence(savedIncidence, request);

        return IncidenceDashboardDTO.fromIncidence(savedIncidence);
    }


}
