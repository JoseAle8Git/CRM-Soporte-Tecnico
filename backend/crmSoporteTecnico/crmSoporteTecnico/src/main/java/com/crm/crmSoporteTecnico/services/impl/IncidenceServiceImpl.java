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
import com.crm.crmSoporteTecnico.services.models.dtos.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        Client client = clientRepository.findById(creatorUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("¡ERROR CRÍTICO! El usuario ID " + creatorUser.getId() + " no existe en la tabla 'client'."));

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
                null,
                client,
                null,
                null
        );

        // Guardar
        Incidence savedIncidence = incidenceRepository.save(newIncidence);

        // 6. Notificar (Si lo tienes activado)
        notificationService.notifyManagerNewIncidence(savedIncidence, request);

        return IncidenceDashboardDTO.fromIncidence(savedIncidence);
    }

    @Override
    public List<IncidenceDashboardDTO> findIncidencesByTechnician(Long technicianId) {
        return incidenceRepository.findByTechnicianId(technicianId)
                .stream()
                .map(IncidenceDashboardDTO::fromIncidence)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public IncidenceDashboardDTO updateIncidenceStatus(Long incidenceId, IncidenceStatus newStatus, Long technicianId) {

        Incidence incidence = incidenceRepository.findById(incidenceId)
                .orElseThrow(() -> new IllegalArgumentException("Incidencia no encontrada."));

        if (incidence.getTechnician() == null ||
                !incidence.getTechnician().getId().equals(technicianId)) {
            throw new IllegalArgumentException("No tienes permiso para modificar esta incidencia.");
        }

        incidence.setStatus(newStatus);

        // Si el técnico resuelve o cierra la incidencia, registramos fecha de finalización.
        if (newStatus == IncidenceStatus.RESOLVED || newStatus == IncidenceStatus.CLOSED) {
            incidence.setCloseDate(LocalDateTime.now());
        }

        Incidence updated = incidenceRepository.save(incidence);

        return IncidenceDashboardDTO.fromIncidence(updated);
    }

    @Override
    public TechnicianPersonalStatsDTO getTechnicianPersonalStats(Long technicianId) {

        List<Incidence> incidences = incidenceRepository.findByTechnicianId(technicianId);

        long pending = incidences.stream().filter(i -> i.getStatus() == IncidenceStatus.PENDING).count();
        long inProgress = incidences.stream().filter(i -> i.getStatus() == IncidenceStatus.IN_PROGRESS).count();
        long resolved = incidences.stream().filter(i -> i.getStatus() == IncidenceStatus.RESOLVED).count();
        long closed = incidences.stream().filter(i -> i.getStatus() == IncidenceStatus.CLOSED).count();

        return new TechnicianPersonalStatsDTO(pending, inProgress, resolved, closed);

    }

    @Override
    public List<Incidence> findIncidencesForTechnicianEntity(Long technicianId) {
        return incidenceRepository.findByTechnicianId(technicianId);
    }

    @Override
    public TechnicianIncidenceDTO updateIncidenceStatusForTechnician(Long incidenceId, IncidenceStatus newStatus, Long technicianId) {

        Incidence incidence = incidenceRepository.findById(incidenceId)
                .orElseThrow(() -> new IllegalArgumentException("Incidencia no encontrada"));

        if (incidence.getTechnician() == null ||
                !incidence.getTechnician().getId().equals(technicianId)) {
            throw new IllegalArgumentException("No tienes permiso para modificar esta incidencia.");
        }

        incidence.setStatus(newStatus);

        if (newStatus == IncidenceStatus.RESOLVED || newStatus == IncidenceStatus.CLOSED) {
            incidence.setCloseDate(LocalDateTime.now());
        }

        Incidence updated = incidenceRepository.save(incidence);

        return TechnicianIncidenceDTO.fromIncidence(updated);
    }


}
