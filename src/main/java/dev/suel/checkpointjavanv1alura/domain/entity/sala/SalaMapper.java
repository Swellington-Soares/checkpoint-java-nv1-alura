package dev.suel.checkpointjavanv1alura.domain.entity.sala;


import dev.suel.checkpointjavanv1alura.utils.FormatterUtil;
import dev.suel.checkpointjavanv1alura.web.sala.data.SalaInfoResponse;
import org.springframework.stereotype.Component;

@Component
public class SalaMapper {
    public SalaInfoResponse toInfoResponse(Sala sala) {
        return new  SalaInfoResponse(
                sala.getId(),
                sala.getNome(),
                FormatterUtil.formatarDataHora(sala.getDataRegistro()),
                sala.getCapacidade(),
                sala.isReservada() ? "Sim" : "Não"
        );
    }


}
