package com.app.CitaMed.Controller.MicroControllers;
import com.app.CitaMed.DTO.*;
import com.app.CitaMed.Model.Medico.Medico;
import com.app.CitaMed.Repository.Medico.MedicoRepository;
import com.app.CitaMed.Service.MicroServicios.DashboardService;
import com.app.CitaMed.Util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor

public class DashboardController {
    private final DashboardService dashboardService;
    private final MedicoRepository medicoRepository;

    @GetMapping
    public DashboardDTO stats() {
        if (SecurityUtil.isMedico()) {
            Medico medico = medicoRepository.findByUsuarioUserName(SecurityUtil.getCurrentUsername()).orElse(null);
            if (medico != null) return dashboardService.obtenerStatsPorMedico(medico.getId());
        }
        return dashboardService.obtenerStats();
    }

    @GetMapping("/ultimas-citas")
    public List<UltimaCitaDTO> ultimas() {
        if (SecurityUtil.isMedico()) {
            Medico medico = medicoRepository.findByUsuarioUserName(SecurityUtil.getCurrentUsername()).orElse(null);
            if (medico != null) return dashboardService.ultimasCitasPorMedico(medico.getId());
        }
        return dashboardService.ultimasCitas();
    }

    @GetMapping("/especialidades")
    public List<EspecialidadDTO> especialidades() {
        return dashboardService.especialidades();
    }

    @GetMapping("/agenda")
    public ResponseEntity<List<AgendaHoyDTO>> agendaHoy() {
        if (SecurityUtil.isMedico()) {
            Medico medico = medicoRepository.findByUsuarioUserName(SecurityUtil.getCurrentUsername()).orElse(null);
            if (medico != null) return ResponseEntity.ok(dashboardService.agendaHoyPorMedico(medico.getId()));
        }
        return ResponseEntity.ok(dashboardService.agendaHoy());
    }

    @GetMapping("/medicos")
    public List<MedicoActivoDTO> medicos() {
        if (SecurityUtil.isMedico()) {
            Medico medico = medicoRepository.findByUsuarioUserName(SecurityUtil.getCurrentUsername()).orElse(null);
            if (medico != null) return dashboardService.medicosActivosPorMedico(medico.getId());
        }
        return dashboardService.medicos();
    }

    @GetMapping("/pagos")
    public List<UltimoPagoDTO> pagos() {
        return dashboardService.pagos();
    }
}
