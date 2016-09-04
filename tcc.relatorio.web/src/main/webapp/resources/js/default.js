$(function () {
    PrimeFaces.locales['pt'] = {
        closeText: 'Fechar',
        prevText: 'Anterior',
        nextText: 'Próximo',
        monthNames: ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'],
        monthNamesShort: ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez'],
        dayNames: ['Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado'],
        dayNamesShort: ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb'],
        dayNamesMin: ['D', 'S', 'T', 'Q', 'Q', 'S', 'S'],
        weekHeader: 'Semana',
        firstDay: 1,
        isRTL: false,
        showMonthAfterYear: false,
        yearSuffix: '',
        timeOnlyTitle: 'Só Horas',
        timeText: 'Tempo',
        hourText: 'Hora',
        minuteText: 'Minuto',
        secondText: 'Segundo',
        currentText: 'Data Atual',
        ampm: false,
        month: 'Mês',
        week: 'Semana',
        day: 'Dia',
        allDayText: 'Todo Dia'
    };

    sipph.selectOneMenuCheio(200);

});

var sipph = {
    selectOneMenuCheio: function (tempoMili) {
        setTimeout(function () {
            var _item = $(".selectOneMenuCheio");
            _item.css({'width': '100%'});
            _item.find("label, input").css({'width': '96%'});
        }, tempoMili || 0);
    }, toUpper: function (_item) {
        var _$item = $(_item);
        _$item.val((_$item.val() || "").toString().toUpperCase());
    }, toLower: function (_item) {
        var _$item = $(_item);
        _$item.val((_$item.val() || "").toString().toLowerCase());
    }, numero: function (_item) {
        var _$item = $(_item);
        _$item.val((_$item.val() || "").toString().replace(/[^0-9]/g, ''));
    }, max: function (_item, tam) {
        var _$item = $(_item), valor = _$item.val() || "";
        tam = tam || valor.length;
        if (valor.length > tam) {
            _$item.val((valor).toString().substr(0, tam));
        }
    }, onChangeToggle: function (_item, props) {
        var _$item = $(_item);
        props = props || [{'US': 'unidSaud'}, {'UP': 'unidPagadora'}];
        $.each(props, function (i, prop) {
            for (var chave in prop) {
                if (chave === _$item.val()) {
                    try {
                        PF(prop[chave]).show();
                    } catch (error) {
                        console.debug(error);
                    }
                } else {
                    PF(prop[chave]).close();
                }
            }
        });
    }, onEnterKeyup: function (event) {
        var _href = window.location.href;
        if (event.keyCode === 13 && (_href.indexOf('public/login.jsf') === -1 && _href.indexOf('view/home.jsf') === -1)) {
            event.preventDefault();
            return false;
        } else {
            return true;
        }
    }
};