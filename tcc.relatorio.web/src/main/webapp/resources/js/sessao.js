var tempoTimeout = new Number(), tempoTimeoutInicial = new Number(), temporario = new Number();
$(function () {
    // Tempo em segundos
    tempoTimeout = $("#tempo").text() || 10;
    tempoTimeoutInicial = tempoTimeout;
// Chama a função ao carregar a tela
    startCountdown(tempoTimeout);

    $(document).on("ajaxComplete pfAjaxComplete ajaxError pfAjaxError ajaxSuccess pfAjaxSuccess", function (event, xhr, options) {
        if(((xhr||{'responseText':'ajax_timeout'}).responseText||"").indexOf("ajax_timeout")===-1){
            temporario = 0;
            var timeOut = 60000;
            setTimeout(function(){
                tempoTimeout = (temporario > timeOut ? (temporario - timeOut) : temporario);
            }, timeOut);
        }else{
            tempoTimeout = tempoTimeoutInicial;
            temporario = tempoTimeout;
        }
    });
});

function startCountdown() {
    tempoTimeout = tempoTimeout || 0;
    // Se o tempo não for zerado
    if ((tempoTimeout - 1) >= 0) {
        // Define que a função será executada novamente em 1000ms = 1 segundo

        // diminui o tempo
        setTimeout('startCountdown();', 1000);

        // Pega a parte inteira dos minutos
        var min = parseInt(tempoTimeout / 60);
        // Calcula os segundos restantes
        var seg = tempoTimeout % 60;

        // Formata o número menor que dez, ex: 08, 07, ...
        if (min < 10) {
            min = "0" + min;
            min = min.substr(0, 2);
        }
        if (seg <= 9) {
            seg = "0" + seg;
        }

        // Cria a variável para formatar no estilo hora/cronômetro
        horaImprimivel = min + ':' + seg;
        //JQuery pra setar o valor

        if (min < 1) {
            horaImprimivel = seg;
            $("#frase_sessao").css({'color': 'red', 'background-color': 'yellow'});
            setTimeout(function () {
                $("#frase_sessao").css({'color': 'green', 'background-color': 'white'});
            }, 300);
        }
        $("#tempo").html(horaImprimivel);

        // Quando o contador chegar a zero faz esta ação
        tempoTimeout--;
    } else {
        $("#frase_sessao").html("Sessão expirada.").css('color', 'red');
    }
}