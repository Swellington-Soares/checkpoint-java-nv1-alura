package dev.suel.checkpointjavanv1alura.domain.entity.reserva;

import dev.suel.checkpointjavanv1alura.utils.FormatterUtil;
import dev.suel.checkpointjavanv1alura.web.reserva.data.ReservaCanceladaResponse;
import dev.suel.checkpointjavanv1alura.web.reserva.data.ReservaInfoResponse;
import org.springframework.stereotype.Component;

@Component
public class ReservaMapper {



    public ReservaInfoResponse toInfoResponse(Reserva reserva) {
        var usuario = reserva.getUsuario();
        var sala = reserva.getSala();
        return new ReservaInfoResponse(
                reserva.getId(),
                new ReservaInfoResponse.UsuarioInfo(
                        usuario.getCpf(),
                        usuario.getNomeCompleto(),
                        usuario.getEmail(),
                        usuario.getTelefone()
                ),
                new ReservaInfoResponse.SalaInfo(
                        sala.getId(),
                        sala.getNome(),
                        sala.getCapacidade()
                ),
                FormatterUtil.formatarDataHora(reserva.getDataInicio()),
                FormatterUtil.formatarDataHora(reserva.getDataFim()),
                reserva.getSituacao()

        );
    }

    public ReservaCanceladaResponse toInfoCancelamento(Reserva reserva) {
        return new ReservaCanceladaResponse(
                reserva.getId(),
                FormatterUtil.formatarDataHora( reserva.getDataCancelamento() ),
                reserva.getMotivoCancelamento()
        );
    }


}
