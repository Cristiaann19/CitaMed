package com.app.CitaMed.Service.Administrativo;
import com.app.CitaMed.DTO.PagoDTO;
import com.app.CitaMed.Enums.EstadoCita;
import com.app.CitaMed.Enums.EstadoPago;
import com.app.CitaMed.Model.Administrativo.Pago;
import com.app.CitaMed.Model.Agenda.Cita;
import com.app.CitaMed.Repository.Administrativo.PagoRepository;
import com.app.CitaMed.Repository.Agenda.CitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor

public class PagoService {
    private final PagoRepository pagoRepository;
    private final CitaRepository citaRepository;

    public List<Pago> findAll() {
        return pagoRepository.findAll();
    }

    public Pago findById(Long id) {
        return pagoRepository.findById(id).orElse(null);
    }

    @Transactional
    public String save(PagoDTO dto) {
        Cita cita = citaRepository.findById(dto.getCitaId()).orElse(null);
        if (cita == null) return "Cita no encontrada";

        if (pagoRepository.existsByCitaId(dto.getCitaId()))
            return "Esta cita ya tiene un pago registrado";

        if (cita.getEstado() == EstadoCita.CANCELADA)
            return "No se puede registrar pago de una cita cancelada";

        Pago pago = new Pago();
        pago.setCita(cita);
        pago.setMonto(dto.getMonto());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setEstado(EstadoPago.PAGADO);
        pago.setFechaPago(LocalDateTime.now());
        pagoRepository.save(pago);
        return "Pago registrado correctamente";
    }

    @Transactional
    public String anular(Long id) {
        Pago pago = pagoRepository.findById(id).orElse(null);
        if (pago == null) return "Pago no encontrado";
        pago.setEstado(EstadoPago.ANULADO);
        pagoRepository.save(pago);
        return "Pago anulado correctamente";
    }
}
