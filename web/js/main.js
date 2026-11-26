/**
 * Banco Academico UDB - Funciones auxiliares de interfaz y validaciones client-side
 */

document.addEventListener('DOMContentLoaded', function () {
    // Auto-ocultar alertas despues de 6 segundos si tienen la clase .auto-dismiss
    const alerts = document.querySelectorAll('.alert.auto-dismiss');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            alert.style.transition = 'opacity 0.5s ease';
            alert.style.opacity = '0';
            setTimeout(function () { alert.remove(); }, 500);
        }, 6000);
    });

    // Formateador de DUI salvadoreño: 00000000-0
    const duiInputs = document.querySelectorAll('input[data-format="dui"]');
    duiInputs.forEach(function (input) {
        input.addEventListener('input', function (e) {
            let val = e.target.value.replace(/\D/g, '');
            if (val.length > 9) val = val.substring(0, 9);
            if (val.length > 8) {
                e.target.value = val.substring(0, 8) + '-' + val.substring(8);
            } else {
                e.target.value = val;
            }
        });
    });

    // Formateador de Telefono: 0000-0000
    const telInputs = document.querySelectorAll('input[data-format="telefono"]');
    telInputs.forEach(function (input) {
        input.addEventListener('input', function (e) {
            let val = e.target.value.replace(/\D/g, '');
            if (val.length > 8) val = val.substring(0, 8);
            if (val.length > 4) {
                e.target.value = val.substring(0, 4) + '-' + val.substring(4);
            } else {
                e.target.value = val;
            }
        });
    });

    // Confirmacion en botones de accion critica (e.g. eliminar o bloquear)
    const confirmButtons = document.querySelectorAll('[data-confirm]');
    confirmButtons.forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            const message = btn.getAttribute('data-confirm') || '¿Está seguro de realizar esta acción?';
            if (!confirm(message)) {
                e.preventDefault();
            }
        });
    });
});
