/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.enumeracao;



/**
 *
 * @author Jose Wdison
 */
public enum TpParentesco {
    Advogado    (1),        Irmão       (2),        Irmã        (3),        Filho       (4),
    Filha       (5),        Neto        (6),        Neta        (7),        Sobrinho    (8),    
    Sobrinha    (9),        Enteado    (10),        Enteada    (11),        Cunhado    (12),     
    Cunhada    (13),        Vizinho    (14),        Vizinha    (15),        Pai        (16),
    Mãe        (17),        Padastro   (18),        Madastra   (19),        Outros     (20);
    
    private int id;

    TpParentesco(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getDescricao() {
        return this.name();
    }

    public static TpParentesco getById(int id) {
        for (TpParentesco tipo : values()) {
            if (tipo.getId() == id) {
                return tipo;
            }
        }
        return null;
    }

    public static TpParentesco getByDescricao(String desc) {
        for (TpParentesco tipo : values()) {
            if (tipo.getDescricao().equals(desc)) {
                return tipo;
            }
        }
        return null;
    }
}
