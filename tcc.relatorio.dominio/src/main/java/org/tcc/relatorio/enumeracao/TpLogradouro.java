package org.tcc.relatorio.enumeracao;


/**
 *
 * @author jwsouza
 */
public enum TpLogradouro {

    AVENIDA(5), RUA(34), ALAMEDA(3), ESTRADA(14), RODOVIA(33), QUADRA(30), TRAVESSA(
            37), RESIDENCIAL(32), AEROPORTO(2), AREA(4), CAMPO(6), CHACARA(7), COLONIA(
                    8), CONDOMINIO(9), CONJUNTO(10), DISTRITO(11), ESPLANADA(12), ESTACAO(
                    13), FAVELA(15), FAZENDA(16), FEIRA(17), JARDIM(18), LADEIRA(19), LAGO(
                    20), LAGOA(21), LARGO(22), LOTEAMENTO(23), MORRO(24), NUCLEO(25), PARQUE(
                    26), PASSARELA(27), PATIO(28), PRACA(29), RECANTO(31), SETOR(35), SITIO(
                    36), TRECHO(38), TREVO(39), VALE(40), VEREDA(41), VIA(42), VIADUTO(
                    43), VIELA(44), VILA(45), OUTROS(1);

    private int id;

    TpLogradouro(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getDescricao() {
        return this.name();
    }

    public static TpLogradouro getById(int id) {
        for (TpLogradouro tipo : values()) {
            if (tipo.getId() == id) {
                return tipo;
            }
        }
        return null;
    }

    public static TpLogradouro getByDescricao(String desc) {
        for (TpLogradouro tipo : values()) {
            if (tipo.getDescricao().equals((""+desc).toUpperCase())) {
                return tipo;
            }
        }
        return TpLogradouro.OUTROS;
    }
}
