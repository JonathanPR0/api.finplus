package com.polarplus.dto.filters.cr;

import org.springframework.web.bind.annotation.RequestParam;

public record FiltersTitulosCRDTO(
        @RequestParam(name = "id_status") Long idStatus,
        @RequestParam(name = "id_cliente") Long idCliente,
        @RequestParam(name = "id_forma_recebimento") Long idFormaRecebimento,
        @RequestParam(name = "id_conta_bancaria") Long idContaBancaria,
        @RequestParam(name = "id_categoria") Long idCategoria,
        String descricao) {
}
