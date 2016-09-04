package org.tcc.relatorio.util;

/**
 *
 * @author eloy
 */
public class Campo {
    String nome;
    String titulo;
    String alinhamento;
    boolean obrigatorio;
    boolean exportavel;
    boolean visivel;
    String[] descricao;
    String funcao;
    int largura;
    int ordem;

    /**
     * Método para iniciar a classe destinada a tela de importação de <b>Task´s</b> e <b>CR´s</b>.
     * @param nome é o nome da propriedade da entidade principal
     * @param obrigatorio indica a obrigatoriedade do campo
     * @param largura largura da coluna
     * @param descricao é um <b>Array</b> contendo nomes de campos para a importação, pelo menos um item
     * deste array deverá estar presente, será usado mais que uma descrição quando existirem arquivos de
     * importação com nomes de cabeçalhos de colunas diversificados. Exemplo: o arquivo de importação de
     * Task´s e CR´s possuem dois formatos diferentes, um deles a coluna que mostra o nome do projeto tem
     * o cabeçalho com o nome <b>NOME DO PROJETO</b>, mas existe também um outro arquivo de importação
     * para a mesma finalidade, porém o nome do cabeçalho da coluna projeto é <b>PROJETO</b>, neste caso
     * este parâmetro deverá ser: <b>new String[]{"NOME DO PROJETO","PROJETO"}</b>
     * @param alinhamento é o alinhamento do campo, poderá ser: <b>left</b>, <b>right</b> ou <b>center</b>.
     */
    public Campo(String nome, boolean obrigatorio, int largura, String[] descricao, String alinhamento) {
        this.nome = nome;
        this.obrigatorio = obrigatorio;
        this.descricao = descricao;
        this.largura = largura;
        this.alinhamento = alinhamento;
        this.funcao = "";
    }

    /**
     * Metodo para iniciar a classe destinada a tela de criação da <b>Grade</b>, onde será
     * necessário inicializa-la com um array da classe <b>Campo</b>.
     * @param nome é o nome da propriedade da entidade principal
     * @param obrigatorio indica a obrigatoriedade do campo
     * @param largura largura da coluna
     * @param titulo nome para o cabeçalho da coluna
     * @param ordem número que classificara a ordem de apresentação
     * @param alinhamento é o alinhamento do campo, poderá ser: <b>left</b>, <b>right</b> ou <b>center</b>.
     * @param funcao função para tratamento da informação do campo, esta função deverá seguir o seguinte padrão:
     * 1) o tipo de retorno deverá sempre ser do tipo <b>Object</b>; 2) o nome da função é livre, e deverá ser passado
     * neste parâmetro como <b>String</b>; 3) a função deverá sempre ter dois parâmetros, o primeiro do tipo <b>Object</b>,
     * que será a informação propriamente dita, e o segundo parâmetro deverá ser o Enum: <b>Tipo</b>, que poderão ser:
     * <b>HINT</b>: tipo de retorno para ser mostrado ao passar o mouse sobre a celula, <b>SORT</b>: informação usada na ordenação
     * da coluna, e <b>GRID</b>: é a informação que será apresentada nas celulas da grade.
     * @param exportavel indica se a coluna será exportada para arquivos excel, csv e xml.
     * @param visivel indica se a coluna será exibida ou ficará oculta na grade.
     */
    public Campo(String nome, boolean obrigatorio, int largura, String titulo, int ordem, String alinhamento, String funcao, boolean exportavel, boolean visivel) {
        this.nome = nome;
        this.obrigatorio = obrigatorio;
        this.titulo = titulo;
        this.ordem = ordem;
        this.largura = largura;
        this.alinhamento = alinhamento;
        this.funcao = funcao;
        this.exportavel = exportavel;
        this.visivel = visivel;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isObrigatorio() {
        return obrigatorio;
    }

    public void setObrigatorio(boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }

    public String[] getDescricao() {
        return descricao;
    }

    public void setDescricao(String[] descricao) {
        this.descricao = descricao;
    }

    public int getLargura() {
        return largura;
    }

    public void setLargura(int largura) {
        this.largura = largura;
    }

    public String getAlinhamento() {
        return alinhamento;
    }

    public void setAlinhamento(String alinhamento) {
        this.alinhamento = alinhamento;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public boolean getExportavel() {
        return exportavel;
    }

    public void setExportavel(boolean exportavel) {
        this.exportavel = exportavel;
    }

    public boolean getVisivel() {
        return visivel;
    }

    public void setVisivel(boolean visivel) {
        this.visivel = visivel;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
